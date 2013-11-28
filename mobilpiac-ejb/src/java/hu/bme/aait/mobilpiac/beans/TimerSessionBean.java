/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.beans;

import hu.bme.aait.mobilpiac.entities.Advertisement;
import hu.bme.aait.mobilpiac.entities.Bids;
import java.util.Date;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;

/**
 *
 * @author Péter
 */
@Stateless
@LocalBean
public class TimerSessionBean {

    private final int port = 587;
    private final String host = "smtp.gmail.com";
    private final String from = "ivanszkypeter@gmail.com";
    private final boolean auth = true;
    private final String username = "ivanszkypeter@gmail.com";
    private final String password = "pydcyooztpkaoxlg";
    private final boolean debug = false;
    
    @PersistenceContext
    EntityManager em;
    
    // Ellenőrzés, hogy lejár-e már az adott hirdetés
    @Schedule(minute = "*/10", second = "*", dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*")
    public void myTimer() {
       System.out.println("E-mail küldés!"+new Date());
       setAdsFinished();
    }
    
    private void setAdsFinished() {
        //int finishDeadline = 1000*60*60*24*7*3; // 3 HÉT
        int finishDeadline = 1000*60*60*24*7; // 3 NAP
        Date publishDeadlineDate = new Date(new Date().getTime()-finishDeadline);
        System.out.println("Ettől a dátumtól zárom le a hirdetéseket: " + publishDeadlineDate);
        TypedQuery<Advertisement> query = em.createQuery("SELECT p FROM Advertisement p WHERE p.published < :publishDeadLine AND p.finished = '0'", Advertisement.class);
        query.setParameter("publishDeadLine", publishDeadlineDate, TemporalType.DATE);
        List<Advertisement> adsList = query.getResultList();
        boolean finishAd = false;
        for(Advertisement ad : adsList)
        {
            System.out.println("Lejárt hirdetés azonosítója: " + ad.getId());
            ad.setFinished("1");
            em.persist(ad);
            finishAd = true;
            sendNotificationEmail(ad);
        }
        if (!finishAd)
        {
            System.out.println("Nem volt olyan hirdetés, amely lejárt volna" );
        }
    }
    
    private void sendNotificationEmail(Advertisement ad) {
        String message = "";
        String phoneViewUrl = "http://localhost:8080/mobilpiac-war/phone_view.html?ad_id="+ad.getId();
        String phoneName = ad.getFkPhoneType().getTypeName();
        String phoneLink = "<a href=\""+phoneViewUrl+"\">"+phoneName+"</a>";
        String sellerName = ad.getFkUser().getLoginName();
        String sellerEmail = ad.getFkUser().getEmailAddress();
        String sellerEmailLink = "<a href=\"mailto:"+sellerEmail+"\">"+sellerEmail+"</a>";
        String winnerBidderName = getWinnerBid().getBidderUserId().getLoginName();
        String winnerBidderEmail = getWinnerBid().getBidderUserId().getEmailAddress();
        String winnerBidderEmailLink = "<a href=\"mailto:"+winnerBidderEmail+"\">"+winnerBidderEmail+"</a>";
        Integer price = getWinnerBid().getPrice();
        
        // Email küldése a hirdetőnek
        String emailTemplate = getEmailTemplate();
        emailTemplate = emailTemplate.replace("[LOGIN_NAME]", sellerName);

        if (ad.getBidsList().isEmpty())
        {
            message = "Örömmel értesítünk, hogy a következ&#337; telefonodat sikeresen megvették: "+phoneLink+"<br/>";
            message += "A nyertes felhasználót is értesítjük, de javasoljuk, hogy vedd fel vele te a kapcsolatot!<br/><br/>";
            message += "- Felhasználóneve: " + winnerBidderName + "<br/>";
            message += "- E-mail címe: " + winnerBidderEmailLink + "<br/>";
            message += "- Az eladási ár: " + price.toString() + "<br/>";
        }
        else
        {
            message += "Sajnálattal értesítünk, hogy a következő telefonod hirdetése lejárt és ezen idő alatt nem licitált rá senki:<br/>";
            message += "- " + phoneLink;
        }
        
        emailTemplate = emailTemplate.replace("[MESSAGE]", message); 
        
        sendEmail("ivanszkypeter@gmail.com", "[MobilPiactér] Értesítő", emailTemplate);
        
        System.out.println("A hirdetőnek a levélküldés sikeres!");
        
        // Email küldése a nyertesnek
        if (!ad.getBidsList().isEmpty())
        {
            emailTemplate = getEmailTemplate();
            emailTemplate = emailTemplate.replace("[LOGIN_NAME]", winnerBidderName);
            
            message = "Örömmel értesítünk, hogy a következ&#337; telefont sikeresen megvetted: "+phoneLink+"<br/>";
            message += "Az eladót is értesítjük, de javasoljuk, hogy vedd fel vele te a kapcsolatot!<br/><br/>";
            message += "- Felhasználóneve: " + sellerName + "<br/>";
            message += "- E-mail címe: " + sellerEmailLink + "<br/>";
            message += "- A vételár: " + price.toString() + "<br/>";
            
            emailTemplate = emailTemplate.replace("[MESSAGE]", message); 
            
            sendEmail("ivanszkypeter@gmail.com;tothdaniel1990@gmail.com", "[MobilPiactér] Értesítő", emailTemplate);
            
            System.out.println("A nyertesnek a levélküldés sikeres!");
        }
        else
        {
            System.out.println("Nem volt nyertes, ezért levelet sem küldtem!");
        }

    }
    
    private Bids getWinnerBid() {
        return em.createQuery("SELECT p FROM Bids p WHERE p.price = (SELECT MAX(u.price) FROM Bids u)", Bids.class).getSingleResult();
    }
    
    private String getEmailTemplate() {
        return "<html>\n" +
"<head>\n" +
"    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">\n" +
"    <title>[TITLE]</title>\n" +
"</head>\n" +
"<body>\n" +
"   <h1>Kedves [LOGIN_NAME]!</h1>\n" +
"   <p>[MESSAGE]</p>\n"+
"   Üdvözlettel,<br/>\n"+
"   MobilPiactér\n"+
"</body>\n" +
"</html>";
    }
        
    public void sendEmail(String to, String subject, String body) {
        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtps.auth", true);
        props.put("mail.smtp.starttls.enable", true);

        Authenticator authenticator = null;
        if (auth) {
            props.put("mail.smtp.auth", true);
            authenticator = new Authenticator() {
                private PasswordAuthentication pa = new PasswordAuthentication(username, password);
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    return pa;
                }
            };
        }

        Session session = Session.getInstance(props, authenticator);
        session.setDebug(debug);

        MimeMessage message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(to)};
            message.setRecipients(Message.RecipientType.TO, address);
            message.setSubject(subject);
            message.setSentDate(new Date());
            //message.setText(body);
            Multipart multipart = new MimeMultipart("alternative");
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Szerezz be egy HTML alapú e-mail klienst!");
            MimeBodyPart htmlPart = new MimeBodyPart();
            String htmlContent = body;
            htmlPart.setContent(htmlContent, "text/html");
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(htmlPart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
    }

 }

    