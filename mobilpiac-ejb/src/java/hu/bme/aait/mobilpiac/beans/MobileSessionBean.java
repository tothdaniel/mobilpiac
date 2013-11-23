/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.beans;

import hu.bme.aait.mobilpiac.entities.Advertisement;
import hu.bme.aait.mobilpiac.entities.Manufacturer;
import hu.bme.aait.mobilpiac.entities.MobileNetwork;
import hu.bme.aait.mobilpiac.entities.PhoneType;
import hu.bme.aait.mobilpiac.entities.Sim;
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
public class MobileSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;
    
    public JSONObject getJSONObjectWithLowDetails(PhoneType p) {
        JSONObject obj = new JSONObject();
        obj.put("id", p.getId());
        obj.put("type_name", p.getTypeName());
        obj.put("manufacturer", p.getFkManufacturer().getManufacturerName());
        obj.put("os_version", p.getFkOsVersion().getVersionName());
        obj.put("os_name", p.getFkOsVersion().getFkOs().getOsName());
        obj.put("processor_clock", p.getFkProcessor().getClock());
        obj.put("processor_number_of_cores", p.getFkProcessor().getNumberOfCores());
        obj.put("rear_camera", p.getRearCamera());
        obj.put("image_url", p.getImageUrl());
        obj.put("published", p.getPublished());
        obj.put("ram", p.getRam());
        obj.put("rom", p.getRom());
        obj.put("res_x", p.getResX());
        obj.put("res_y", p.getResY());
        return obj;
    }

    public JSONObject getJSONObject(PhoneType p, int cheapest) {
        JSONObject obj = new JSONObject();
        obj.put("id", p.getId());
        obj.put("type_name", p.getTypeName());
        obj.put("display_inches", p.getDisplayInches());
        obj.put("dpi", p.getDpi());
        obj.put("gpu", p.getFkGpu().getGpuName());
        obj.put("manufacturer", p.getFkManufacturer().getManufacturerName());
        obj.put("os_version", p.getFkOsVersion().getVersionName());
        obj.put("os_name", p.getFkOsVersion().getFkOs().getOsName());
        obj.put("processor_family", p.getFkProcessor().getFamily());
        obj.put("processor_clock", p.getFkProcessor().getClock());
        obj.put("processor_number_of_cores", p.getFkProcessor().getNumberOfCores());
        obj.put("sim_type", p.getFkSim().getSimType());
        obj.put("front_camera", p.getFrontCamera());
        obj.put("rear_camera", p.getRearCamera());
        obj.put("image_url", p.getImageUrl());
        obj.put("microsd_enabled", p.getMicrosdEnabled());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String published = formatter.format(p.getPublished());
        obj.put("published", published);
        obj.put("ram", p.getRam());
        obj.put("rom", p.getRom());
        obj.put("res_x", p.getResX());
        obj.put("res_y", p.getResY());
        if(cheapest != 1000000)
        {
            obj.put("cheapest_price",""+cheapest);
        }
        else
        {
            obj.put("cheapest_price","N/A");
        }
        return obj;
    }
    
    public JSONArray listAllMobiles(){
        List<PhoneType> phonesList = em.createQuery("SELECT p FROM PhoneType p"    
            + " ORDER BY P.fkManufacturer.manufacturerName,p.typeName"
        ).getResultList();
        JSONArray jarray = new JSONArray();
        for(PhoneType p:phonesList)
        {
            List<Advertisement> advertisementList = em.createQuery("SELECT "
            + "a FROM Advertisement a WHERE a.fkPhoneType.id="+p.getId()
            ).getResultList();
            int cheapest = 1000000;
            for(Advertisement a:advertisementList)
            {
                if(a.getMinPrice()+a.getLastBid()*1000 < cheapest)
                {
                    cheapest = a.getMinPrice()+a.getLastBid()*1000;
                }
            }
            jarray.add(getJSONObject(p,cheapest));
        }
        return jarray;
    }
    
    public JSONArray listALlMobileNetworks(){
        List<MobileNetwork> networksList = em.createQuery("SELECT m FROM MobileNetwork m").getResultList();
        JSONArray jarray = new JSONArray();
        for(MobileNetwork m:networksList)
        {
            JSONObject obj = new JSONObject();
            obj.put("id", m.getId());
            obj.put("network_name", m.getNetworkName());
            jarray.add(obj);
        }
        return jarray;
    }
    
    
    public JSONArray listSimTypes(){
        List<Sim> simsList = em.createQuery("SELECT s FROM Sim s").getResultList();
        JSONArray jarray = new JSONArray();
        for(Sim s:simsList)
        {
            JSONObject obj = new JSONObject();
            obj.put("id", s.getId());
            obj.put("sim_type", s.getSimType());
            jarray.add(obj);
        }
        return jarray;
    }
    
    public JSONArray listManufacturers(){
        List<Manufacturer> manufacturersList = em.createQuery("SELECT m FROM Manufacturer m").getResultList();
        JSONArray jarray = new JSONArray();
        for(Manufacturer m:manufacturersList)
        {
            JSONObject obj = new JSONObject();
            obj.put("id", m.getId());
            obj.put("manufacturer_name", m.getManufacturerName());
            jarray.add(obj);
        }
        return jarray;
    }
}
