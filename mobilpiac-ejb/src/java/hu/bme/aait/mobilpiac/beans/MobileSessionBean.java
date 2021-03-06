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
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
public class MobileSessionBean {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    EntityManager em;

    public boolean isInteger(String str) {
        return str.matches("^-?[0-9]+(\\.[0-9]+)?$");
    }

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
        TypedQuery<PhoneType> query = em.createQuery("SELECT p FROM PhoneType p WHERE p.id = :pid AND p.deleted = 0", PhoneType.class);
        query.setParameter("pid", Long.parseLong(id));
        List<PhoneType> pList = query.getResultList();

        JSONObject obj = new JSONObject();
        if (!pList.isEmpty()) {
            PhoneType p = pList.get(0);
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
        return null;
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
        if (cheapest != 1000000) {
            obj.put("cheapest_price", "" + cheapest);
        } else {
            obj.put("cheapest_price", "N/A");
        }
        return obj;
    }

    public JSONArray listMobiles(String json) {
        org.json.JSONObject myobj = new org.json.JSONObject(json);
        JSONArray jarray = new JSONArray();
        if (myobj.getString("manufacturer") != null) {
            TypedQuery<Manufacturer> query = em.createQuery("SELECT m FROM Manufacturer m WHERE m.manufacturerName = :name", Manufacturer.class);
            query.setParameter("name", myobj.getString("manufacturer"));
            List<Manufacturer> mList = query.getResultList();
            if (!mList.isEmpty()) {

                TypedQuery<PhoneType> query2 = em.createQuery("SELECT p FROM PhoneType p WHERE p.fkManufacturer.manufacturerName = :name AND p.deleted = 0", PhoneType.class);
                query2.setParameter("name", mList.get(0).getManufacturerName());
                List<PhoneType> phonesList = query2.getResultList();
                for (PhoneType p : phonesList) {
                    jarray.add(getJSONObject(p, 0));
                }
            }
        }
        return jarray;
    }

    public JSONArray listAllMobiles() {
        List<PhoneType> phonesList = em.createQuery("SELECT p FROM PhoneType p WHERE p.deleted = 0"
                + " ORDER BY P.fkManufacturer.manufacturerName,p.typeName"
        ).getResultList();
        JSONArray jarray = new JSONArray();
        int cntr = 0;
        for (PhoneType p : phonesList) {
            //az elso 12-t listazza csak ki, ezek a legnezettebbek
            if (cntr < 12) {
                TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a WHERE a.fkPhoneType.id = :pid AND a.finished = 0", Advertisement.class);
                query.setParameter("pid", p.getId());
                List<Advertisement> advertisementList = query.getResultList();

                int cheapest = 1000000;
                for (Advertisement a : advertisementList) {
                    TypedQuery<Bids> query2 = em.createQuery("SELECT b FROM Bids b WHERE b.advertisementId.fkPhoneType.id = :pid", Bids.class);
                    query2.setParameter("pid", p.getId());
                    List<Bids> bidList = query2.getResultList();

                    int actPrice = a.getMinPrice();
                    for (Bids b : bidList) {
                        if (b.getPrice() > actPrice) {
                            actPrice = b.getPrice();
                        }
                    }

                    if (actPrice < cheapest) {
                        cheapest = actPrice;
                    }
                }
                jarray.add(getJSONObject(p, cheapest));
                cntr++;
            }
        }
        return jarray;
    }

    public JSONArray listALlMobileNetworks() {
        List<MobileNetwork> networksList = em.createQuery("SELECT m FROM MobileNetwork m").getResultList();
        JSONArray jarray = new JSONArray();
        for (MobileNetwork m : networksList) {
            JSONObject obj = new JSONObject();
            obj.put("id", m.getId());
            obj.put("network_name", m.getNetworkName());
            jarray.add(obj);
        }
        return jarray;
    }

    public JSONArray listSimTypes() {
        List<Sim> simsList = em.createQuery("SELECT s FROM Sim s").getResultList();
        JSONArray jarray = new JSONArray();
        for (Sim s : simsList) {
            JSONObject obj = new JSONObject();
            obj.put("id", s.getId());
            obj.put("sim_type", s.getSimType());
            jarray.add(obj);
        }
        return jarray;
    }

    public JSONArray listGpus() {
        List<Gpu> gpusList = em.createQuery("SELECT g FROM Gpu g ORDER BY g.gpuName").getResultList();
        JSONArray jarray = new JSONArray();
        for (Gpu g : gpusList) {
            JSONObject obj = new JSONObject();
            obj.put("id", g.getId());
            obj.put("gpu_name", g.getGpuName());
            jarray.add(obj);
        }
        return jarray;
    }

    public JSONArray listOperationSystems() {
        List<OperationSystem> osList = em.createQuery("SELECT o FROM OperationSystem o").getResultList();
        JSONArray jarray = new JSONArray();
        for (OperationSystem o : osList) {
            JSONObject obj = new JSONObject();
            obj.put("id", o.getId());
            obj.put("os_name", o.getOsName());
            jarray.add(obj);
        }
        return jarray;
    }

    public JSONArray listOsVersions(String jobj) {
        org.json.JSONObject myobj = new org.json.JSONObject(jobj);
        List<OperationSystem> oList = em.createQuery("SELECT o FROM OperationSystem o").getResultList();
        JSONArray jarray = new JSONArray();

        OperationSystem os = null;
        for (OperationSystem o : oList) {
            if (o.getOsName().equals(myobj.get("operation_system"))) {
                os = o;
            }
        }
        if (os == null) {
            List<OsVersion> osvList = em.createQuery("SELECT o FROM OsVersion o WHERE o.fkOs.id=1 ORDER BY o.versionName").getResultList();
            for (OsVersion osv : osvList) {
                JSONObject obj = new JSONObject();
                obj.put("id", osv.getId());
                obj.put("version_name", osv.getVersionName());
                jarray.add(obj);
            }
        } else {
            for (OsVersion osv : os.getOsVersionList()) {
                JSONObject obj = new JSONObject();
                obj.put("id", osv.getId());
                obj.put("version_name", osv.getVersionName());
                jarray.add(obj);
            }
        }
        return jarray;
    }

    public JSONArray listProcessors() {
        List<Processor> pList = em.createQuery("SELECT p FROM Processor p").getResultList();
        JSONArray jarray = new JSONArray();
        for (Processor p : pList) {
            JSONObject obj = new JSONObject();
            obj.put("id", p.getId());
            obj.put("chipset", p.getChipset());
            jarray.add(obj);
        }
        return jarray;
    }

    public JSONArray listManufacturers() {
        List<Manufacturer> manufacturersList = em.createQuery("SELECT m FROM Manufacturer m").getResultList();
        JSONArray jarray = new JSONArray();
        for (Manufacturer m : manufacturersList) {
            JSONObject obj = new JSONObject();
            obj.put("id", m.getId());
            obj.put("manufacturer_name", m.getManufacturerName());
            jarray.add(obj);
        }
        return jarray;
    }

    public List<String> addOperationSystem(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);

        if (jobj.getString("os_name") == null || jobj.getString("os_name").isEmpty()) {
            result.add("false");
            result.add("A hozzáadandó operációs rendszer nevét nem adta meg.");
            return result;
        }

        final String qstring = "SELECT o FROM OperationSystem o WHERE o.osName = :name";
        TypedQuery<OperationSystem> query = em.createQuery(qstring, OperationSystem.class);
        query.setParameter("name", jobj.getString("os_name"));
        List<OperationSystem> tos = query.getResultList();

        if (!tos.isEmpty()) {
            result.add("false");
            result.add("Már létezik ilyen nevű operációs rendszer.");
            return result;
        } else {
            OperationSystem os = new OperationSystem();

            Short id = em.createQuery("SELECT MAX(o.id) FROM OperationSystem o", Short.class).getSingleResult();
            if (id == null) {
                id = 1;
            } else {
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

    public List<String> addOsVersion(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);

        if (jobj.getString("os_name") == null || jobj.getString("os_name").isEmpty()) {
            result.add("false");
            result.add("A hozzáadandó operációs rendszert nem választotta ki.");
            return result;
        }

        if (jobj.getString("os_version") == null || jobj.getString("os_version").isEmpty()) {
            result.add("false");
            result.add("A hozzáadandó verzió nevét nem adta meg.");
            return result;
        }

        final String qstring = "SELECT o FROM OperationSystem o WHERE o.osName = :name";
        TypedQuery<OperationSystem> query = em.createQuery(qstring, OperationSystem.class);
        query.setParameter("name", jobj.getString("os_name"));
        List<OperationSystem> tos = query.getResultList();

        if (tos.isEmpty()) {
            result.add("false");
            result.add("Nincs " + jobj.getString("os_name") + " nevű operációs rendszer. Válasszon a felsoroltakból.");
            return result;
        }

        TypedQuery<OsVersion> query2 = em.createQuery("SELECT osv FROM OsVersion osv WHERE osv.versionName = :name AND osv.fkOs.osName = :osname", OsVersion.class);
        query2.setParameter("name", jobj.getString("os_version"));
        query2.setParameter("osname", jobj.getString("os_name"));
        List<OsVersion> osvs = query2.getResultList();

        if (!osvs.isEmpty()) {
            result.add("false");
            result.add("Már létezik " + jobj.getString("os_version") + " nevű verzió a(z) " + jobj.getString("os_name") + " operációs rendszerhez.");
            return result;
        } else {
            OsVersion os = new OsVersion();
            try {
                Long id = em.createQuery("SELECT MAX(o.id) FROM OsVersion o", Long.class).getSingleResult();
                if (id == null) {
                    id = Long.parseLong("1");
                } else {
                    id = id++;
                }

                os.setId(id);
                os.setVersionName(jobj.getString("os_version"));
                TypedQuery<OperationSystem> queryOS = em.createQuery("SELECT os FROM OperationSystem os WHERE os.osName = :osname", OperationSystem.class);
                queryOS.setParameter("osname", jobj.getString("os_name"));
                OperationSystem thisOS = queryOS.getSingleResult();
                os.setFkOs(thisOS);

                em.persist(os);
                result.add("true");
                result.add("Sikeresen hozzáadta a " + os.getVersionName() + " verziót a(z) " + os.getFkOs().getOsName() + " operációs rendszerhez.");
                return result;
            } catch (Exception e) {
                result.add("false");
                result.add(e.getMessage());
                return result;
            }
        }
    }

    public List<String> addManufacturer(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);

        if (jobj.getString("manufacturer_name") == null || jobj.getString("manufacturer_name").isEmpty()) {
            result.add("false");
            result.add("A hozzáadandó gyártó nevét nem adta meg.");
            return result;
        }

        final String qstring = "SELECT m FROM Manufacturer m WHERE m.manufacturerName = :name";
        TypedQuery<Manufacturer> query = em.createQuery(qstring, Manufacturer.class);
        query.setParameter("name", jobj.getString("manufacturer_name"));
        List<Manufacturer> ms = query.getResultList();

        if (!ms.isEmpty()) {
            result.add("false");
            result.add("Már létezik ilyen nevű gyártó.");
            return result;
        } else {
            Manufacturer m = new Manufacturer();
            Short id = em.createQuery("SELECT MAX(m.id) FROM Manufacturer m", Short.class).getSingleResult();
            if (id == null) {
                id = 1;
            } else {
                id = id++;
            }
            m.setId(id);
            m.setManufacturerName(jobj.getString("manufacturer_name"));
            em.persist(m);

            result.add("true");
            result.add("Sikeresen hozzáadta a " + m.getManufacturerName() + " nevű gyártót.");
            return result;
        }
    }

    public List<String> addGpu(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);

        if (jobj.getString("gpu_name") == null || jobj.getString("gpu_name").isEmpty()) {
            result.add("false");
            result.add("A hozzáadandó GPU nevét nem adta meg.");
            return result;
        }

        final String qstring = "SELECT g FROM Gpu g WHERE g.gpuName = :name";
        TypedQuery<Gpu> query = em.createQuery(qstring, Gpu.class);
        query.setParameter("name", jobj.getString("gpu_name"));
        List<Gpu> gs = query.getResultList();

        if (!gs.isEmpty()) {
            result.add("false");
            result.add("Már létezik ilyen nevű gpu.");
            return result;
        } else {
            Gpu g = new Gpu();
            Long id = em.createQuery("SELECT MAX(g.id) FROM Gpu g", Long.class).getSingleResult();
            if (id == null) {
                id = Long.parseLong("1");
            } else {
                id = id++;
            }
            g.setId(id);
            g.setGpuName(jobj.getString("gpu_name"));
            em.persist(g);

            result.add("true");
            result.add("Sikeresen hozzáadta a " + g.getGpuName() + " nevű gpu-t.");
            return result;
        }
    }

    public List<String> addSim(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);

        if (jobj.getString("sim_type") == null || jobj.getString("sim_type").isEmpty()) {
            result.add("false");
            result.add("A hozzáadandó sim típus nevét nem adta meg.");
            return result;
        }

        final String qstring = "SELECT s FROM Sim s WHERE s.simType = :name";
        TypedQuery<Sim> query = em.createQuery(qstring, Sim.class);
        query.setParameter("name", jobj.getString("sim_type"));
        List<Sim> sims = query.getResultList();

        if (!sims.isEmpty()) {
            result.add("false");
            result.add("Már létezik ilyen nevű sim típus.");
            return result;
        } else {
            Sim s = new Sim();

            Short id = em.createQuery("SELECT MAX(s.id) FROM Sim s", Short.class).getSingleResult();
            if (id == null) {
                id = 1;
            } else {
                id = id++;
            }
            s.setId(id);
            s.setSimType(jobj.getString("sim_type"));
            em.persist(s);

            result.add("true");
            result.add("Sikeresen hozzáadta a " + s.getSimType() + " nevű sim típust.");
            return result;
        }
    }

    public List<String> addProcessor(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);

        if (jobj.getString("chipset") == null || jobj.getString("chipset").isEmpty()) {
            result.add("false");
            result.add("A hozzáadandó processzor chipset nevét nem adta meg.");
            return result;
        }

        if (jobj.getString("family") == null || jobj.getString("family").isEmpty()) {
            result.add("false");
            result.add("A hozzáadandó processzor család nevét nem adta meg.");
            return result;
        }

        if (jobj.getString("clock") == null || jobj.getString("clock").isEmpty()) {
            result.add("false");
            result.add("A hozzáadandó processzor órajelét nem adta meg.");
            return result;
        }

        if (jobj.getString("number_of_cores") == null || jobj.getString("number_of_cores").isEmpty()) {
            result.add("false");
            result.add("A hozzáadandó processzor magjainak számát nem adta meg.");
            return result;
        }

        if (!isInteger(jobj.getString("clock"))) {
            result.add("false");
            result.add("A hozzáadandó processzor órajelét nem számmal adta meg.");
            return result;
        }

        if (!isInteger(jobj.getString("number_of_cores"))) {
            result.add("false");
            result.add("A hozzáadandó processzormagok számát nem számmal adta meg.");
            return result;
        }

        final String qstring = "SELECT p FROM Processor p WHERE p.chipset = :name";
        TypedQuery<Processor> query = em.createQuery(qstring, Processor.class);
        query.setParameter("name", jobj.getString("chipset"));
        List<Processor> ps = query.getResultList();

        if (!ps.isEmpty()) {
            result.add("false");
            result.add("Már létezik ilyen chipsettel rendelkező processzor.");
            return result;
        } else {
            Processor p = new Processor();

            Integer id = em.createQuery("SELECT MAX(p.id) FROM Processor p", Integer.class).getSingleResult();
            if (id == null) {
                id = 1;
            } else {
                id = id++;
            }
            p.setId(id);
            p.setChipset(jobj.getString("chipset"));
            p.setFamily(jobj.getString("family"));
            p.setClock(Short.parseShort(jobj.getString("clock")));
            p.setNumberOfCores(Short.parseShort(jobj.getString("number_of_cores")));
            em.persist(p);

            result.add("true");
            result.add("Sikeresen hozzáadta a " + p.getChipset() + " chipsettel ellátott processzort.");
            return result;
        }
    }

    public List<String> managePhoneType(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);
        String todo = jobj.getString("type");
        if (todo.equals("read")) {
            JSONObject obj = getJSONObject("" + jobj.getInt("id"));

            if (obj != null) {
                result.add("trueread");
                result.add(obj.toJSONString());
            } else {
                result.add("false");
                result.add("Nem található a telefon.");
            }
        } else if (todo.equals("add")) {
            return addPhoneType(json);
        } else if (todo.equals("modify")) {
            return modifyPhoneType(json);
        } else if (todo.equals("delete")) {
            return deletePhoneType(json);
        }
        result.add("false");
        result.add("Utasítás nem található. Kérem, frissítse az oldalt.");
        return result;
    }

    public List<String> deletePhoneType(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);
        Long id = null;
        try {
            id = jobj.getLong("id");

        } catch (Exception e) {
            result.add("false");
            result.add("Szükséges adat hiányzik.");
            return result;
        }
        TypedQuery<PhoneType> pquery = em.createQuery("SELECT p FROM PhoneType p WHERE p.id = :pid", PhoneType.class);
        pquery.setParameter("pid", id);
        List<PhoneType> pList = pquery.getResultList();
        if (pList.isEmpty()) {
            result.add("false");
            result.add("Nincs ilyen telefontípus.");
            return result;
        }
        //PhoneType phoneType = pList.get(0);
        PhoneType phoneType = em.find(PhoneType.class, id);
        phoneType.setDeleted(new Short("1"));

        em.persist(phoneType);

        result.add("true");
        result.add("Sikeresen törölre a telefontípust.");
        return result;
    }

    public List<String> addPhoneType(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);
        //STEP 1: parsing
        String typeName = null;
        String manufacturerName = null;
        String osName = null;
        String osVersion = null;
        String processorChipset = null;
        String gpuName = null;
        String simType = null;
        Short microsd = null;
        Short ram = null;
        Integer rom = null;
        Short resx = null;
        Short resy = null;
        Short dpi = null;
        Double screenSize = null;
        Double frontCam = null;
        Double rearCam = null;
        String imageUrl = null;

        try {
            typeName = jobj.getString("type_name");
            manufacturerName = jobj.getString("manufacturer");
            osName = jobj.getString("os_name");
            osVersion = jobj.getString("os_version");
            processorChipset = jobj.getString("processor");
            gpuName = jobj.getString("gpu");
            simType = jobj.getString("sim_type");
            microsd = jobj.getString("microsd_enabled").equals("Van") ? new Short("1") : new Short("0");
            ram = new Short(jobj.getString("ram"));
            rom = jobj.getInt("rom");
            resx = new Short(jobj.getString("res_x"));
            resy = new Short(jobj.getString("res_y"));
            dpi = new Short(jobj.getString("dpi"));
            screenSize = Double.parseDouble(jobj.getString("display_inches"));
            frontCam = Double.parseDouble(jobj.getString("front_camera"));
            rearCam = Double.parseDouble(jobj.getString("rear_camera"));
            imageUrl = jobj.getString("image_url");

        } catch (JSONException | NumberFormatException e) {
            result.add("false");
            result.add(e.getMessage());
            return result;
        }
        //STEP 2: Validating
        TypedQuery<Manufacturer> mquery = em.createQuery("SELECT m FROM Manufacturer m WHERE m.manufacturerName = :mname", Manufacturer.class);
        mquery.setParameter("mname", manufacturerName);
        List<Manufacturer> mList = mquery.getResultList();
        Manufacturer manufacturer;
        if (mList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen gyártó. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            manufacturer = mList.get(0);
        }

        TypedQuery<OsVersion> oquery = em.createQuery("SELECT o FROM OsVersion o WHERE o.fkOs.osName = :oname AND o.versionName = :vname", OsVersion.class);
        oquery.setParameter("oname", osName);
        oquery.setParameter("vname", osVersion);
        List<OsVersion> oList = oquery.getResultList();
        OsVersion osv;
        OperationSystem os;
        if (oList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen operációs rendszer - verzió páros. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            osv = oList.get(0);
            os = oList.get(0).getFkOs();
        }

        TypedQuery<Processor> pquery = em.createQuery("SELECT p FROM Processor p WHERE p.chipset = :pname", Processor.class);
        pquery.setParameter("pname", processorChipset);
        List<Processor> pList = pquery.getResultList();
        Processor processor;
        if (pList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen processzor. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            processor = pList.get(0);
        }
        TypedQuery<Gpu> gquery = em.createQuery("SELECT g FROM Gpu g WHERE g.gpuName = :gname", Gpu.class);
        gquery.setParameter("gname", gpuName);
        List<Gpu> gList = gquery.getResultList();
        Gpu gpu;
        if (gList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen grafikus vezérlő. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            gpu = gList.get(0);
        }
        TypedQuery<Sim> squery = em.createQuery("SELECT s FROM Sim s WHERE s.simType = :sname", Sim.class);
        squery.setParameter("sname", simType);
        List<Sim> sList = squery.getResultList();
        Sim sim;
        if (sList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen SIM típus. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            sim = sList.get(0);
        }

        TypedQuery<Long> ptquery = em.createQuery("SELECT MAX(p.id) FROM PhoneType p", Long.class);
        List<Long> ptList = ptquery.getResultList();
        Long id = new Long(1);
        if (!ptList.isEmpty()) {
            id = ptList.get(0)+1;
        }
        //STEP 3: Saving     
        PhoneType phoneType = new PhoneType();
        
        phoneType.setId(id);
        phoneType.setDeleted(new Short("0"));
        phoneType.setDisplayInches(screenSize);
        phoneType.setDpi(Short.parseShort(dpi.toString()));
        phoneType.setFkGpu(gpu);
        phoneType.setFkManufacturer(manufacturer);
        phoneType.setFkOsVersion(osv);
        phoneType.setFkProcessor(processor);
        phoneType.setFkSim(sim);
        phoneType.setFrontCamera(frontCam);
        phoneType.setImageUrl(imageUrl);
        phoneType.setMicrosdEnabled(microsd);
        phoneType.setPublished(new Date());
        phoneType.setRam(ram);
        phoneType.setRearCamera(rearCam);
        phoneType.setResX(resx);
        phoneType.setResY(resy);
        phoneType.setRom(rom);
        phoneType.setTypeName(typeName);

        em.persist(phoneType);
        result.add("true");
        result.add("Sikeresen hozzáadta a telefontípust");
        return result;
    }

    public List<String> modifyPhoneType(String json) {
        List<String> result = new ArrayList<>();
        org.json.JSONObject jobj = new org.json.JSONObject(json);
        Long id;
        //STEP 1: parsing
        String typeName = null;
        String manufacturerName = null;
        String osName = null;
        String osVersion = null;
        String processorChipset = null;
        String gpuName = null;
        String simType = null;
        Short microsd = null;
        Short ram = null;
        Integer rom = null;
        Short resx = null;
        Short resy = null;
        Short dpi = null;
        Double screenSize = null;
        Double frontCam = null;
        Double rearCam = null;

        try {
            id = jobj.getLong("id");
            typeName = jobj.getString("type_name");
            manufacturerName = jobj.getString("manufacturer");
            osName = jobj.getString("os_name");
            osVersion = jobj.getString("os_version");
            processorChipset = jobj.getString("processor");
            gpuName = jobj.getString("gpu");
            simType = jobj.getString("sim_type");
            microsd = jobj.getString("microsd_enabled").equals("Van") ? new Short("1") : new Short("0");
            ram = new Short(jobj.getString("ram"));
            rom = jobj.getInt("rom");
            resx = new Short(jobj.getString("res_x"));
            resy = new Short(jobj.getString("res_y"));
            dpi = new Short(jobj.getString("dpi"));
            screenSize = Double.parseDouble(jobj.getString("display_inches"));
            frontCam = Double.parseDouble(jobj.getString("front_camera"));
            rearCam = Double.parseDouble(jobj.getString("rear_camera"));

        } catch (JSONException | NumberFormatException e) {
            result.add("false");
            result.add(e.getMessage());
            return result;
        }
        //STEP 2: Validating
        TypedQuery<Manufacturer> mquery = em.createQuery("SELECT m FROM Manufacturer m WHERE m.manufacturerName = :mname", Manufacturer.class);
        mquery.setParameter("mname", manufacturerName);
        List<Manufacturer> mList = mquery.getResultList();
        Manufacturer manufacturer;
        if (mList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen gyártó. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            manufacturer = mList.get(0);
        }

        TypedQuery<OsVersion> oquery = em.createQuery("SELECT o FROM OsVersion o WHERE o.fkOs.osName = :oname AND o.versionName = :vname", OsVersion.class);
        oquery.setParameter("oname", osName);
        oquery.setParameter("vname", osVersion);
        List<OsVersion> oList = oquery.getResultList();
        OsVersion osv;
        OperationSystem os;
        if (oList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen operációs rendszer - verzió páros. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            osv = oList.get(0);
            os = oList.get(0).getFkOs();
        }

        TypedQuery<Processor> pquery = em.createQuery("SELECT p FROM Processor p WHERE p.chipset = :pname", Processor.class);
        pquery.setParameter("pname", processorChipset);
        List<Processor> pList = pquery.getResultList();
        Processor processor;
        if (pList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen processzor. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            processor = pList.get(0);
        }
        TypedQuery<Gpu> gquery = em.createQuery("SELECT g FROM Gpu g WHERE g.gpuName = :gname", Gpu.class);
        gquery.setParameter("gname", gpuName);
        List<Gpu> gList = gquery.getResultList();
        Gpu gpu;
        if (gList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen grafikus vezérlő. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            gpu = gList.get(0);
        }
        TypedQuery<Sim> squery = em.createQuery("SELECT s FROM Sim s WHERE s.simType = :sname", Sim.class);
        squery.setParameter("sname", simType);
        List<Sim> sList = squery.getResultList();
        Sim sim;
        if (sList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen SIM típus. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            sim = sList.get(0);
        }

        TypedQuery<PhoneType> ptquery = em.createQuery("SELECT p FROM PhoneType p WHERE p.id = :pid", PhoneType.class);
        ptquery.setParameter("pid", id);
        List<PhoneType> ptList = ptquery.getResultList();
        PhoneType phoneType;
        if (ptList.isEmpty()) {
            result.add("false");
            result.add("Nem található ilyen telefontípus. Kérem, ellenőrizze a megadott adatokat.");
            return result;
        } else {
            phoneType = ptList.get(0);
        }
        //STEP 3: Saving       
        phoneType.setDeleted(new Short("0"));
        phoneType.setDisplayInches(screenSize);
        phoneType.setDpi(Short.parseShort(dpi.toString()));
        phoneType.setFkGpu(gpu);
        phoneType.setFkManufacturer(manufacturer);
        phoneType.setFkOsVersion(osv);
        phoneType.setFkProcessor(processor);
        phoneType.setFkSim(sim);
        phoneType.setFrontCamera(frontCam);
        //phoneType.setImageUrl();
        phoneType.setMicrosdEnabled(microsd);
        phoneType.setPublished(new Date());
        phoneType.setRam(ram);
        phoneType.setRearCamera(rearCam);
        phoneType.setResX(resx);
        phoneType.setResY(resy);
        phoneType.setRom(rom);
        phoneType.setTypeName(typeName);

        em.persist(phoneType);
        result.add("true");
        result.add("Sikeresen módosította a telefontípust");
        return result;
    }
}
