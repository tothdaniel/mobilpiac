/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.beans;

import hu.bme.aait.mobilpiac.entities.Users;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
        try{
            obj.put("id", u.getId());
            obj.put("login_name", u.getLoginName());
            
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.reset();
            m.update(u.getPassword().getBytes());
            byte[] digest = m.digest();
            BigInteger bigInt = new BigInteger(1,digest);
            String hashtext = bigInt.toString(16);
            // Now we need to zero pad it if you actually want the full 32 chars.
            while(hashtext.length() < 32 ){
                hashtext = "0"+hashtext;
            }
            
            obj.put("password", hashtext);
            obj.put("role", u.getFkRoleId().getRoleName());
            obj.put("email", u.getEmailAddress());
            obj.put("enabled", u.getEnabled());  
            obj.put("last_login", (u.getLastVisited().getYear()+1900)+"."+(u.getLastVisited().getMonth()+1)+"."+u.getLastVisited().getDate());
            obj.put("registered_date", (u.getRegistrationDate().getYear()+1900)+"."+(u.getRegistrationDate().getMonth()+1)+"."+u.getRegistrationDate().getDate());
        }
        catch(Exception e)
        {
            obj.put("error",e.getMessage());
        }
        return obj;
    }
    
    public JSONArray listAllUsers(){
        List<Users> usersList = em.createQuery("SELECT users FROM Users users").getResultList();
        JSONArray jarray = new JSONArray();
        for(Users u:usersList)
        {
            jarray.add(getJSONObject(u));
        }
        return jarray;
    }
    
    public Users login(String loginName, String password){
        Users user = null;
        List<Users> usersList = em.createQuery("SELECT u FROM Users u").getResultList();
        for(Users u:usersList)
        {
            if(u.getLoginName().equals(loginName) && u.getPassword().equals(password))
            {
                user = u;
            }
        }
        return user;
    }
    
    public String registration(String loginName, String password, String emailAddress)
    {
        if(loginName != null && loginName.length()>3)
        {
            if(password != null && loginName.length()>3)
            {
                if(emailAddress != null && emailAddress.length()>3 && emailAddress.contains("@"))
                {
                    List<Users> users = em.createQuery("SELECT u FROM Users u").getResultList();
                    for(Users u:users)
                    {
                       if(u.getLoginName().equals(loginName))
                       {
                           return "Ez a felhasználónév már regisztrálva van";
                       }
                    }
                    
                    em.getTransaction().begin();

                    Users user = new Users();
                    user.setLoginName(loginName);
                    user.setPassword(password);
                    user.setEmailAddress(emailAddress);

                    em.persist(user);

                    em.getTransaction().commit();
                    return "Sikeresen regisztrált.";
                    
                    
                    
                }
                else
                {
                    return "Az email címnek legalább 4 karakterből kell állnia és tartalmaznia kell a @karaktert.";
                }
            }
            else
            {
                return "A jelszónak legalább 4 karakterből kell állnia.";
            }
        }
        else
        {
            return "A felhasználónévnek legalább 4 karakterből kell állnia.";
        }
    }
    
}
