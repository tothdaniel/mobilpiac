/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.bme.aait.mobilpiac.beans;

import hu.bme.aait.mobilpiac.entities.Role;
import hu.bme.aait.mobilpiac.entities.Users;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Daniel
 */
@Stateless
@LocalBean
public class UserSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    public JSONObject getJSONObject(Users u) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", u.getId());
            obj.put("login_name", u.getLoginName());

            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(u.getPassword().getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1, digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            obj.put("password", hashtext);
            obj.put("role", u.getFkRoleId().getRoleName());
            obj.put("email", u.getEmailAddress());
            obj.put("enabled", u.getEnabled());
            obj.put("last_login", (u.getLastVisited().getYear() + 1900) + "." + (u.getLastVisited().getMonth() + 1) + "." + u.getLastVisited().getDate());
            obj.put("registered_date", (u.getRegistrationDate().getYear() + 1900) + "." + (u.getRegistrationDate().getMonth() + 1) + "." + u.getRegistrationDate().getDate());
        } catch (NoSuchAlgorithmException e) {
            obj.put("error", e.getMessage());
        }
        return obj;
    }

    public List<String> checkAdmin(String loginName, String password) {
        List<String> result = new ArrayList<>();

        TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.loginName = :name AND u.password = :password AND u.fkRoleId.roleName = 'admin'", Users.class);
        query.setParameter("name", loginName);
        query.setParameter("password", password);
        List<Users> usersList = query.getResultList();

        if (!usersList.isEmpty()) {
            result.add("true");
            result.add("Belépés engedélyezve.");
            return result;
        }
        result.add("false");
        result.add("Ide csak az adminisztrátor léphet be.");
        return result;
    }

    public JSONArray listAllUsers() {
        List<Users> usersList = em.createQuery("SELECT users FROM Users users").getResultList();
        JSONArray jarray = new JSONArray();
        for (Users u : usersList) {
            jarray.add(getJSONObject(u));
        }
        return jarray;
    }

    public List<String> login(String loginName, String password) {
        Users user = null;
        List<String> result = new ArrayList<>();
        TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.loginName = :name AND u.password = :password", Users.class);
        query.setParameter("name", loginName);
        query.setParameter("password", password);
        List<Users> usersList = query.getResultList();
        if (!usersList.isEmpty()) {
            result.add("true");
            result.add("Sikeresen bejelentkezett, mint "+loginName+".");
            return result;
        }
        result.add("false");
        result.add("Hibás felhasználónév/jelszó");
        return result;
    }

    public List<String> resetPassword(String loginName, String email, String password) {
        TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.loginName = :name AND u.emailAddress = :email", Users.class);
        query.setParameter("name", loginName);
        query.setParameter("email", email);
        List<Users> usersList = query.getResultList();
        List<String> result = new ArrayList<>();
        if (!usersList.isEmpty()) {
            usersList.get(0).setPassword(password);
            em.persist(usersList.get(0));

            result.add("A jelszavát sikeresen megváltoztatta.");
            result.add("true");
            return result;
        }
        result.add("Nem található ez a felhasználónév - email-cím páros.");
        result.add("false");
        return result;
    }

    public List<String> registration(String loginName, String password, String emailAddress) {
        List<String> resultList = new ArrayList<>();
        if (loginName != null && loginName.length() > 3) {
            if (password != null && loginName.length() > 3) {
                if (emailAddress != null && emailAddress.length() > 3 && emailAddress.contains("@")) {
                    TypedQuery<Users> query = em.createQuery("SELECT u FROM Users u WHERE u.loginName = :name", Users.class);
                    query.setParameter("name", loginName);
                    List<Users> users = query.getResultList();

                    if (!users.isEmpty()) {
                        resultList.add("Ez a felhasználónév már regisztrálva van");
                        resultList.add("false");
                        return resultList;
                    }

                    Users user = new Users();
                    user.setLoginName(loginName);
                    user.setPassword(password);
                    user.setEmailAddress(emailAddress);
                    short enabled = 1;
                    user.setEnabled(enabled);
                    user.setLastVisited(new Date());
                    user.setRegistrationDate(new Date());
                    
                    Role role = em.createQuery("SELECT r FROM Role r WHERE r.roleName = 'user'",Role.class).getSingleResult();
                    user.setFkRoleId(role);
                    try {
                        em.persist(user);
                    } catch (Exception e) {
                        resultList.add("Hiba lépett fel a regisztráció során. Próbálkozzon később.");
                        resultList.add("false");
                        return resultList;
                    }

                    resultList.add("Sikeresen regisztrált.");
                    resultList.add("true");
                    return resultList;

                } else {
                    resultList.add("Az email címnek legalább 4 karakterből kell állnia és tartalmaznia kell a @karaktert.");
                    resultList.add("false");
                    return resultList;
                }
            } else {
                resultList.add("A jelszónak legalább 4 karakterből kell állnia.");
                resultList.add("false");
                return resultList;
            }
        } else {
            resultList.add("A felhasználónévnek legalább 4 karakterből kell állnia.");
            resultList.add("false");
            return resultList;
        }
    }
}
