/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.bme.aait.mobilpiac.beans;

import hu.bme.aait.mobilpiac.entities.Advertisement;
import hu.bme.aait.mobilpiac.entities.Bids;
import hu.bme.aait.mobilpiac.entities.Manufacturer;
import hu.bme.aait.mobilpiac.entities.MobileNetwork;
import hu.bme.aait.mobilpiac.entities.PhoneType;
import hu.bme.aait.mobilpiac.entities.Sim;
import hu.bme.aait.mobilpiac.entities.Users;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import org.json.JSONException;
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

    public JSONObject getJSONObjectWithLowDetails(Advertisement a, int actPrice) {
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
        obj.put("actual_price", actPrice);
        obj.put("seller_user", a.getFkUser().getLoginName());
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String published = formatter.format(a.getPublished());
        obj.put("published", published);
        return obj;
    }

    public JSONObject getJSONObject(String id) {

        TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a WHERE a.id = :pid", Advertisement.class);
        query.setParameter("pid", Long.parseLong(id));
        Advertisement a = query.getSingleResult();

        TypedQuery<Bids> query2 = em.createQuery("SELECT b FROM Bids b WHERE b.advertisementId.id = :pid", Bids.class);
        query2.setParameter("pid", Long.parseLong(id));
        List<Bids> bidList = query2.getResultList();
        int actPrice = a.getMinPrice();
        for (Bids b : bidList) {
            if (b.getPrice() > actPrice) {
                actPrice = b.getPrice();
            }
        }

        JSONObject obj = new JSONObject();
        obj.put("id", a.getId());
        obj.put("description", a.getDescription());
        obj.put("type_name", a.getFkPhoneType().getTypeName());
        obj.put("manufacturer", a.getFkPhoneType().getFkManufacturer().getManufacturerName());
        obj.put("os_version", a.getFkPhoneType().getFkOsVersion().getVersionName());
        obj.put("os_name", a.getFkPhoneType().getFkOsVersion().getFkOs().getOsName());
        obj.put("processor_clock", a.getFkPhoneType().getFkProcessor().getClock());
        obj.put("processor_number_of_cores", a.getFkPhoneType().getFkProcessor().getNumberOfCores());
        obj.put("rear_camera", a.getFkPhoneType().getRearCamera());
        obj.put("front_camera", a.getFkPhoneType().getFrontCamera());
        if (a.getFkPhoneType().getMicrosdEnabled() == 1) {
            obj.put("microsd_enabled", "bővíthető");
        } else {
            obj.put("microsd_enabled", "nem bővíthető");
        }
        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String published = formatter.format(a.getFkPhoneType().getPublished());
        obj.put("phone_published", published);
        obj.put("image_url", a.getFkPhoneType().getImageUrl());
        obj.put("published", a.getFkPhoneType().getPublished());
        obj.put("ram", a.getFkPhoneType().getRam());
        obj.put("rom", a.getFkPhoneType().getRom());
        obj.put("res_x", a.getFkPhoneType().getResX());
        obj.put("res_y", a.getFkPhoneType().getResY());
        obj.put("gpu", a.getFkPhoneType().getFkGpu().getGpuName());
        obj.put("display_inches", a.getFkPhoneType().getDisplayInches());
        obj.put("dpi", a.getFkPhoneType().getDpi());
        obj.put("processor_family", a.getFkPhoneType().getFkProcessor().getFamily());
        obj.put("processor_chipset", a.getFkPhoneType().getFkProcessor().getChipset());
        obj.put("sim_type", a.getFkPhoneType().getFkSim().getSimType());
        obj.put("actual_price", actPrice);
        obj.put("seller_user", a.getFkUser().getLoginName());
        Format formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String published2 = formatter.format(a.getPublished());
        obj.put("published", published2);
        return obj;
    }

    public JSONArray listAllAds(org.json.JSONObject jobj) {
        List<Advertisement> adsList = em.createQuery(
                "SELECT a FROM Advertisement a WHERE a.finished = 0 "
                + "ORDER BY a.fkPhoneType.fkManufacturer.manufacturerName,"
                + "a.fkPhoneType.typeName").getResultList();
        JSONArray jarray = new JSONArray();

        int minPrice = jobj.getInt("min_price");
        int maxPrice = jobj.getInt("max_price");
        org.json.JSONArray mobileNetworks = jobj.getJSONArray("mobile_networks");
        List<Integer> mobileNetworkList = new ArrayList<>();
        for (int i = 0; i < mobileNetworks.length(); i++) {
            mobileNetworkList.add(Integer.parseInt(mobileNetworks.get(i).toString()));
        }
        if (mobileNetworkList.isEmpty()) {
            List<MobileNetwork> mnList = em.createQuery("SELECT mn FROM MobileNetwork mn").getResultList();
            for (MobileNetwork m : mnList) {
                mobileNetworkList.add(Integer.parseInt(m.getId().toString()));
            }
        }
        org.json.JSONArray manufacturers = jobj.getJSONArray("manufacturers");
        List<Integer> manufacturerList = new ArrayList<>();
        for (int i = 0; i < manufacturers.length(); i++) {
            manufacturerList.add(Integer.parseInt(manufacturers.get(i).toString()));
        }
        if (manufacturerList.isEmpty()) {
            List<Manufacturer> mnList = em.createQuery("SELECT mn FROM Manufacturer mn").getResultList();
            for (Manufacturer m : mnList) {
                manufacturerList.add(Integer.parseInt(m.getId().toString()));
            }
        }
        org.json.JSONArray sims = jobj.getJSONArray("sims");
        List<Integer> simList = new ArrayList<>();
        for (int i = 0; i < sims.length(); i++) {
            simList.add(Integer.parseInt(sims.get(i).toString()));
        }
        if (simList.isEmpty()) {
            List<Sim> mnList = em.createQuery("SELECT s FROM Sim s").getResultList();
            for (Sim s : mnList) {
                simList.add(Integer.parseInt(s.getId().toString()));
            }
        }

        for (Advertisement a : adsList) {
            //a tovabbi szureseket nem jpql-ben, hanem entitasonkent vegzem, nem akartam egy listaval (for... or)
            //osszehasonlitani
            TypedQuery<Bids> query = em.createQuery("SELECT b FROM Bids b WHERE b.advertisementId.id = :id", Bids.class);
            query.setParameter("id", a.getId());
            List<Bids> bidList = query.getResultList();
            int actPrice = a.getMinPrice();
            for (Bids b : bidList) {
                if (b.getPrice() > actPrice) {
                    actPrice = b.getPrice();
                }
            }
            //aktualis ar beleesik-e az intervallumba
            if (actPrice >= minPrice && actPrice <= maxPrice) {
                for (int mn : mobileNetworkList) {
                    if (mn == a.getFkNetworkLock().getId()) {
                        for (int mf : manufacturerList) {
                            if (mf == a.getFkPhoneType().getFkManufacturer().getId()) {
                                for (int sim : simList) {
                                    if (sim == a.getFkPhoneType().getFkSim().getId()) {
                                        jarray.add(getJSONObjectWithLowDetails(a, actPrice));
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

    public List<String> addBid(String json) {
        List<String> result = new ArrayList<>();

        org.json.JSONObject obj = new org.json.JSONObject(json);
        String loginName = obj.getString("bid_user");
        Long adId = obj.getLong("ad_id");
        Integer bidPrice = obj.getInt("bid_price");

        if (loginName == null || loginName.isEmpty()) {
            result.add("false");
            result.add("Meg kell adnia a licitáló felhasználó nevét.");
            return result;
        }

        if (obj.getString("ad_id") == null || obj.getString("ad_id").isEmpty()) {
            result.add("false");
            result.add("Meg kell adnia a hirdetés azonosítóját.");
            return result;
        }
        if (obj.getInt("bid_price") == 0) {
            result.add("false");
            result.add("Meg kell adnia a licit összegét.");
            return result;
        }

        TypedQuery<Users> query3 = em.createQuery("SELECT u FROM Users u WHERE u.loginName = :name", Users.class);
        query3.setParameter("name", loginName);
        List<Users> users = query3.getResultList();

        if (users.isEmpty()) {
            result.add("false");
            result.add("Nincs ilyen nevű felhasználó.");
            return result;
        } else {
            Users u = users.get(0);
            TypedQuery<Advertisement> query2 = em.createQuery("SELECT a FROM Advertisement a WHERE a.id = :pid", Advertisement.class);
            query2.setParameter("pid", adId);
            List<Advertisement> ads = query2.getResultList();
            if (ads.isEmpty()) {
                result.add("false");
                result.add("Nincs ilyen azonosítójú hirdetés.");
                return result;
            } else {
                Advertisement a = ads.get(0);
                TypedQuery<Bids> query = em.createQuery("SELECT b FROM Bids b WHERE b.advertisementId.finished = '0' AND b.advertisementId.id = :pid", Bids.class);
                query.setParameter("pid", a.getId());
                List<Bids> bids = query.getResultList();

                if (bids.isEmpty()) {
                    if (bidPrice > a.getMinPrice()) {
                        Long id = em.createQuery("SELECT MAX(b.id) FROM Bids b", Long.class).getSingleResult();
                        if (id == null) {
                            id = new Long(1);
                        } else {
                            id = id + 1;
                        }
                        try {
                            Bids bid = new Bids();
                            bid.setId(id);

                            bid.setAdvertisementId(a);
                            bid.setDateOfBid(new Date());
                            bid.setBidderUserId(u);
                            bid.setPrice(bidPrice);

                            em.persist(bid);

                            result.add("true");
                            result.add("Sikeresen licitált.");
                            return result;
                        } catch (Exception e) {
                            result.add("false");
                            result.add("Hiba lépett fel a mentés során. Próbálkozzon később.");
                            return result;
                        }
                    } else {
                        result.add("false");
                        result.add("A licitje nem haladja meg az eddigi legnagyobb értéket.");
                        return result;
                    }
                } else {

                    Integer maxPrice = a.getMinPrice();
                    for (Bids b : bids) {
                        if (maxPrice < b.getPrice()) {
                            maxPrice = b.getPrice();
                        }
                    }

                    if (maxPrice >= bidPrice) {
                        result.add("false");
                        result.add("A licitje nem haladja meg az eddigi legnagyobb értéket.");
                        return result;
                    } else {
                        Long id = em.createQuery("SELECT MAX(b.id) FROM Bids b", Long.class).getSingleResult();
                        if (id == null) {
                            id = new Long(1);
                        } else {
                            id = id + 1;
                        }
                        Bids bid = new Bids();
                        bid.setId(id);

                        bid.setAdvertisementId(a);
                        bid.setDateOfBid(new Date());
                        bid.setBidderUserId(u);
                        bid.setPrice(bidPrice);

                        em.persist(bid);

                        result.add("true");
                        result.add("Sikeresen licitált.");
                        return result;
                    }
                }
            }
        }
    }

    public List<String> addAdvertisement(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject obj = new org.json.JSONObject(json);
        String manufacturer = null;
        String typeName = null;
        Integer minPrice = null;
        String network = null;
        String description = null;;
        String loginName = null;
        
        try{
            manufacturer = obj.getString("manufacturer");
            typeName = obj.getString("type_name");
            minPrice = obj.getInt("min_price");
            network = obj.getString("network");
            description = obj.getString("description");
            loginName = obj.getString("login_name");
        }catch(JSONException e){
            result.add("false");
            result.add("Szükséges adat hiányzik. Amennyiben nem áll rendelkezésre, az adott márkához nem tud jelenleg feltölteni.");
            return result;
        }

       if (manufacturer == null || manufacturer.isEmpty()) {
            result.add("false");
            result.add("Nem adta meg a készülék gyártóját.");
            return result;
        }

        if (typeName == null || typeName.isEmpty()) {
            result.add("false");
            result.add("Nem adta meg a készülék típusát.");
            return result;
        }

        if (minPrice == 0) {
            result.add("false");
            result.add("Nem adta meg a készülék minimálárát.");
            return result;
        }

        if (description == null || description.isEmpty()) {
            result.add("false");
            result.add("Nem adta meg a készülék leírását.");
            return result;
        }

        if (network == null || network.isEmpty()) {
            result.add("false");
            result.add("Nem adta meg a készülék hálózatfüggőségét.");
            return result;
        }
        
        if (loginName == null || loginName.isEmpty()) {
            result.add("false");
            result.add("Ehhez a funkcióhoz be kell jelentkeznie.");
            return result;
        }
        


        TypedQuery<PhoneType> rquery = em.createQuery("SELECT p FROM PhoneType p WHERE p.typeName = :name and p.fkManufacturer.manufacturerName = :mname", PhoneType.class);
        rquery.setParameter("name", typeName);
        rquery.setParameter("mname", manufacturer);
        List<PhoneType> pList = rquery.getResultList();
        if(!pList.isEmpty()){
            TypedQuery<MobileNetwork> mquery = em.createQuery("SELECT m FROM MobileNetwork m WHERE m.networkName = :name", MobileNetwork.class);
            mquery.setParameter("name", network);
            List<MobileNetwork> mList = mquery.getResultList();
            if(!mList.isEmpty()){
                TypedQuery<Users> uquery = em.createQuery("SELECT u FROM Users u WHERE u.loginName = :name", Users.class);
                uquery.setParameter("name", loginName);
                List<Users> uList = uquery.getResultList();
                if(!uList.isEmpty()){
                    
                    Advertisement a = new Advertisement();
                    Long id = em.createQuery("SELECT MAX(a.id) FROM Advertisement a", Long.class).getSingleResult();
                    if (id == null) {
                        id = new Long(1);
                    } else {
                        id = id + 1;
                    }
                    a.setId(id);
                    a.setDescription(description);
                    a.setFinished("0");
                    a.setFkNetworkLock(mList.get(0));
                    a.setFkPhoneType(pList.get(0));
                    a.setFkUser(uList.get(0));
                    a.setMinPrice(minPrice);
                    a.setPublished(new Date());
                    
                    em.persist(a);
                    
                    result.add("true");
                    result.add("Sikeresen feladta a hirdetést.");
                    return result;
                }else{
                    result.add("false");
                    result.add("Ez a felhasználó található. Kérem, jelentkezzen be.");
                    return result;
                }
                
            }else{
                result.add("false");
                result.add("Ez a hálózat nem található. Kérem, ellenőrizze, hogy helyesen adta-e meg.");
                return result;
            }
        }else{
            result.add("false");
            result.add("Ez a telefongyártó-típus páros nem található. Kérem, ellenőrizze, hogy helyesen adta-e meg.");
            return result;
        }
    }
    
    public JSONArray listBids(String id){
        JSONArray array = new JSONArray();
        
        TypedQuery<Bids> query = em.createQuery("SELECT b FROM Bids b WHERE b.advertisementId.id = :pid",Bids.class);
        query.setParameter("pid", Long.parseLong(id));
        List<Bids> bidsList = query.getResultList();
        for(Bids b:bidsList)
        {
            JSONObject obj = new JSONObject();
            obj.put("id",b.getId());
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String published = formatter.format(b.getDateOfBid());
            obj.put("date_of_bid", published);
            obj.put("price",b.getPrice());
            obj.put("bidder_user_id",b.getBidderUserId().getLoginName());
            array.add(obj);
        }
        return array;
    }
}
