/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.beans;

import hu.bme.aait.mobilpiac.entities.Advertisement;
import hu.bme.aait.mobilpiac.entities.Manufacturer;
import hu.bme.aait.mobilpiac.entities.MobileNetwork;
import hu.bme.aait.mobilpiac.entities.Sim;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    
    public JSONArray listAllAds(org.json.JSONObject jobj){
        List<Advertisement> adsList = em.createQuery("SELECT a FROM Advertisement a ORDER BY a.fkPhoneType.fkManufacturer.manufacturerName,a.fkPhoneType.typeName").getResultList();
        JSONArray jarray = new JSONArray();

        int minPrice = jobj.getInt("min_price");
        int maxPrice = jobj.getInt("max_price");
        org.json.JSONArray mobileNetworks = jobj.getJSONArray("mobile_networks");
        List<Integer> mobileNetworkList = new ArrayList<Integer>();
        for (int i = 0; i < mobileNetworks.length(); i++) {
            mobileNetworkList.add(Integer.parseInt(mobileNetworks.get(i).toString()));
        }
        if(mobileNetworkList.isEmpty())
        {
            List<MobileNetwork> mnList = em.createQuery("SELECT mn FROM MobileNetwork mn").getResultList();
            for(MobileNetwork m:mnList)
            {
                mobileNetworkList.add(Integer.parseInt(m.getId().toString()));
            }
        }
        org.json.JSONArray manufacturers = jobj.getJSONArray("manufacturers");
        List<Integer> manufacturerList = new ArrayList<Integer>();
        for (int i = 0; i < manufacturers.length(); i++) {
            manufacturerList.add(Integer.parseInt(manufacturers.get(i).toString()));
        }
        if(manufacturerList.isEmpty())
        {
            List<Manufacturer> mnList = em.createQuery("SELECT mn FROM Manufacturer mn").getResultList();
            for(Manufacturer m:mnList)
            {
                manufacturerList.add(Integer.parseInt(m.getId().toString()));
            }
        }
        org.json.JSONArray sims = jobj.getJSONArray("sims");
        List<Integer> simList = new ArrayList<Integer>();
        for (int i = 0; i < sims.length(); i++) {
            simList.add(Integer.parseInt(sims.get(i).toString()));
        }
        if(simList.isEmpty())
        {
            List<Sim> mnList = em.createQuery("SELECT s FROM Sim s").getResultList();
            for(Sim s:mnList)
            {
                simList.add(Integer.parseInt(s.getId().toString()));
            }
        }
        
        for(Advertisement a:adsList)
        {
            if((a.getMinPrice()+a.getLastBid()*1000) >= minPrice && (a.getMinPrice()+a.getLastBid()*1000) <= maxPrice)
            {
                for(int mn:mobileNetworkList)
                {
                    if(mn == a.getFkNetworkLock().getId())
                    {
                        for(int mf:manufacturerList)
                        {
                            if(mf == a.getFkPhoneType().getFkManufacturer().getId())
                            {
                                for(int sim:simList)
                                {
                                    if(sim == a.getFkPhoneType().getFkSim().getId())
                                    {
                                        jarray.add(getJSONObjectWithLowDetails(a));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return jarray;
    }
}
