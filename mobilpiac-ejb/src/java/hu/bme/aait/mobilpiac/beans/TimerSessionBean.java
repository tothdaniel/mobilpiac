/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.beans;

import hu.bme.aait.mobilpiac.entities.Advertisement;
import hu.bme.aait.mobilpiac.entities.PhoneType;
import hu.bme.aait.mobilpiac.entities.Users;
import java.util.Date;
import java.util.List;
import javax.ejb.Schedule;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

/**
 *
 * @author Péter
 */
@Stateless
@LocalBean
public class TimerSessionBean {

    @PersistenceContext
    EntityManager em;
    
    // Ellenőrzés, hogy lejár-e már az adott hirdetés
    @Schedule(minute = "*/5", second = "*", dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*")
    public void myTimer() {
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
        }
        if (!finishAd)
        {
            System.out.println("Nem volt olyan hirdetés, amely lejárt volna" );
        }
    }

}