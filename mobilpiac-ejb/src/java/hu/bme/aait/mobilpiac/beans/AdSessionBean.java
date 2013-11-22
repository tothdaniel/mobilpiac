/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.beans;

import hu.bme.aait.mobilpiac.entities.Advertisement;
import java.text.Format;
import java.text.SimpleDateFormat;
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
public class AdSessionBean {

    @PersistenceContext
    EntityManager em;
    
    public JSONObject getJSONObjectWithLowDetails(Advertisement a) {
        JSONObject obj = new JSONObject();
        obj.put("id", a.getId());
        obj.put("type_name", a.getFkPhoneType().getTypeName());
        obj.put("manufacturer", a.getFkPhoneType().getFkManufacturer().getManufacturerName());
        obj.put("os_version", a.getFkPhoneType().getFkOsVersion().getVersionName());
        obj.put("os_name", a.getFkPhoneType().getFkOsVersion().getFkOs().getOsName());
        obj.put("processor_clock", a.getFkPhoneType().getFkProcessor().getClock());
        obj.put("processor_number_of_cores", a.getFkPhoneType().getFkProcessor().getNumberOfCores());
        obj.put("rear_camera", a.getFkPhoneType().getRearCamera());
        obj.put("image_url", a.getFkPhoneType().getImageUrl());
        obj.put("published", a.getFkPhoneType().getPublished());
        obj.put("ram", a.getFkPhoneType().getRam());
        obj.put("rom", a.getFkPhoneType().getRom());
        obj.put("res_x", a.getFkPhoneType().getResX());
        obj.put("res_y", a.getFkPhoneType().getResY());
        int actual_price = a.getMinPrice()+a.getLastBid()*1000;
        obj.put("actual_price", actual_price);
        obj.put("seller_user",a.getFkUser().getLoginName());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String published = formatter.format(a.getPublished());
        obj.put("published",published);
        return obj;
    }
    
    public JSONArray listAllAds(JSONObject obj){
        List<Advertisement> adsList = em.createQuery("SELECT a FROM Advertisement a").getResultList();
        JSONArray jarray = new JSONArray();
        for(Advertisement a:adsList)
        {
            jarray.add(getJSONObjectWithLowDetails(a));
        }
        return jarray;
    }
}
