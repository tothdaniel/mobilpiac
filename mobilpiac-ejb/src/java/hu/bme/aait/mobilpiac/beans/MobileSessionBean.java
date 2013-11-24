/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.beans;

import hu.bme.aait.mobilpiac.entities.Advertisement;
import hu.bme.aait.mobilpiac.entities.Bids;
import hu.bme.aait.mobilpiac.entities.Gpu;
import hu.bme.aait.mobilpiac.entities.Manufacturer;
import hu.bme.aait.mobilpiac.entities.MobileNetwork;
import hu.bme.aait.mobilpiac.entities.OperationSystem;
import hu.bme.aait.mobilpiac.entities.OsVersion;
import hu.bme.aait.mobilpiac.entities.PhoneType;
import hu.bme.aait.mobilpiac.entities.Processor;
import hu.bme.aait.mobilpiac.entities.Sim;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    
    public JSONObject getJSONObject(String id) {
        List<PhoneType> phoneList = em.createQuery("SELECT p FROM PhoneType p WHERE p.id="+id).getResultList();
        PhoneType p = phoneList.get(0);
        
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
        obj.put("processor_chipset", p.getFkProcessor().getChipset());
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
        obj.put("manufacturer_id", p.getFkManufacturer().getId());
        obj.put("os_version", p.getFkOsVersion().getVersionName());
        obj.put("os_name", p.getFkOsVersion().getFkOs().getOsName());
        obj.put("processor_family", p.getFkProcessor().getFamily());
        obj.put("processor_clock", p.getFkProcessor().getClock());
        obj.put("processor_number_of_cores", p.getFkProcessor().getNumberOfCores());
        obj.put("processor_chipset", p.getFkProcessor().getChipset());
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
        int cntr = 0;
        for(PhoneType p:phonesList)
        {
            //az elso 12-t listazza csak ki, ezek a legnezettebbek
            if(cntr<12)
            {
                List<Advertisement> advertisementList = em.createQuery("SELECT "
                + "a FROM Advertisement a WHERE a.fkPhoneType.id="+p.getId()
                ).getResultList();
                int cheapest = 1000000;
                for(Advertisement a:advertisementList)
                {
                    List<Bids> bidList = em.createQuery("SELECT b FROM Bids b WHERE b.advertisementId.fkPhoneType.id ="+p.getId()).getResultList();
                    
                    int actPrice = a.getMinPrice();
                    for(Bids b:bidList)
                    {
                        if(b.getPrice() > actPrice)
                        {
                            actPrice = b.getPrice();
                        }
                    }
                    
                    if(actPrice < cheapest)
                    {
                        cheapest = actPrice;
                    }
                }
                jarray.add(getJSONObject(p,cheapest));
                cntr++;
            }
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
    
    public JSONArray listGpus(){
        List<Gpu> gpusList = em.createQuery("SELECT g FROM Gpu g ORDER BY g.gpuName").getResultList();
        JSONArray jarray = new JSONArray();
        for(Gpu g:gpusList)
        {
            JSONObject obj = new JSONObject();
            obj.put("id", g.getId());
            obj.put("gpu_name", g.getGpuName());
            jarray.add(obj);
        }
        return jarray;
    }
    
    public JSONArray listOperationSystems(){
    List<OperationSystem> osList = em.createQuery("SELECT o FROM OperationSystem o").getResultList();
        JSONArray jarray = new JSONArray();
        for(OperationSystem o:osList)
        {
            JSONObject obj = new JSONObject();
            obj.put("id", o.getId());
            obj.put("os_name", o.getOsName());
            jarray.add(obj);
        }
        return jarray;
    }
    
    public JSONArray listOsVersions(String jobj){
        org.json.JSONObject myobj = new org.json.JSONObject(jobj);
        List<OperationSystem> oList = em.createQuery("SELECT o FROM OperationSystem o").getResultList();
        JSONArray jarray = new JSONArray();
        
        OperationSystem os = null;
        for(OperationSystem o:oList)
        {
            if(o.getOsName().equals(myobj.get("operation_system")))
            {
                os = o;
            }
        }
        if(os == null)
        {
            List<OsVersion> osvList = em.createQuery("SELECT o FROM OsVersion o WHERE o.fkOs.id=1 ORDER BY o.versionName").getResultList();
            for(OsVersion osv:osvList)
            {
                JSONObject obj = new JSONObject();
                obj.put("id", osv.getId());
                obj.put("version_name", osv.getVersionName());
                jarray.add(obj);
            }
        }
        else
        {
            for(OsVersion osv:os.getOsVersionList())
            {
                JSONObject obj = new JSONObject();
                obj.put("id", osv.getId());
                obj.put("version_name", osv.getVersionName());
                jarray.add(obj);
            }
        }
        return jarray;
    }
    
    public JSONArray listProcessors(){
        List<Processor> pList = em.createQuery("SELECT p FROM Processor p").getResultList();
        JSONArray jarray = new JSONArray();
        for(Processor p:pList)
        {
            JSONObject obj = new JSONObject();
            obj.put("id", p.getId());
            obj.put("chipset", p.getChipset());
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
    
    public List<String> addOperationSystem(String json)
    {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);
        
        
        if(jobj.getString("os_name") == null || jobj.getString("os_name").isEmpty())
        {
            result.add("false");
            result.add("A hozzáadandó operációs rendszer nevét nem adta meg.");
            return result;
        }

        final String qstring = "SELECT o FROM OperationSystem o WHERE o.osName = :name";
        TypedQuery<OperationSystem> query = em.createQuery(qstring,OperationSystem.class);
        query.setParameter("name", jobj.getString("os_name"));
        List<OperationSystem> tos = query.getResultList();
        
        if(!tos.isEmpty())
        {
            result.add("false");
            result.add("Már létezik ilyen nevű operációs rendszer.");
            return result;
        }
        else
        {
            OperationSystem os = new OperationSystem();
            
            Short id = em.createQuery("SELECT MAX(o.id) FROM OperationSystem o",Short.class).getSingleResult();
            if(id==null)
            {
                id = 1;
            }
            else
            {
                id = id++;
            }
            os.setId(id);
            os.setOsName(jobj.getString("os_name"));
            em.persist(os);

            result.add("true");
            result.add("Sikeresen hozzáadta a " + os.getOsName() + " nevű operációs rendszert.");
            return result;
        }
    }
}
