/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "PHONE_TYPE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "PhoneType.findAll", query = "SELECT p FROM PhoneType p"),
    @NamedQuery(name = "PhoneType.findById", query = "SELECT p FROM PhoneType p WHERE p.id = :id"),
    @NamedQuery(name = "PhoneType.findByDisplayInches", query = "SELECT p FROM PhoneType p WHERE p.displayInches = :displayInches"),
    @NamedQuery(name = "PhoneType.findByDpi", query = "SELECT p FROM PhoneType p WHERE p.dpi = :dpi"),
    @NamedQuery(name = "PhoneType.findByFrontCamera", query = "SELECT p FROM PhoneType p WHERE p.frontCamera = :frontCamera"),
    @NamedQuery(name = "PhoneType.findByImageUrl", query = "SELECT p FROM PhoneType p WHERE p.imageUrl = :imageUrl"),
    @NamedQuery(name = "PhoneType.findByMicrosdEnabled", query = "SELECT p FROM PhoneType p WHERE p.microsdEnabled = :microsdEnabled"),
    @NamedQuery(name = "PhoneType.findByPublished", query = "SELECT p FROM PhoneType p WHERE p.published = :published"),
    @NamedQuery(name = "PhoneType.findByRam", query = "SELECT p FROM PhoneType p WHERE p.ram = :ram"),
    @NamedQuery(name = "PhoneType.findByRearCamera", query = "SELECT p FROM PhoneType p WHERE p.rearCamera = :rearCamera"),
    @NamedQuery(name = "PhoneType.findByResX", query = "SELECT p FROM PhoneType p WHERE p.resX = :resX"),
    @NamedQuery(name = "PhoneType.findByResY", query = "SELECT p FROM PhoneType p WHERE p.resY = :resY"),
    @NamedQuery(name = "PhoneType.findByRom", query = "SELECT p FROM PhoneType p WHERE p.rom = :rom"),
    @NamedQuery(name = "PhoneType.findByTypeName", query = "SELECT p FROM PhoneType p WHERE p.typeName = :typeName"),
    @NamedQuery(name = "PhoneType.findByDeleted", query = "SELECT p FROM PhoneType p WHERE p.deleted = :deleted")})
public class PhoneType implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "DISPLAY_INCHES")
    private Double displayInches;
    @Column(name = "DPI")
    private Short dpi;
    @Column(name = "FRONT_CAMERA")
    private Double frontCamera;
    @Size(max = 255)
    @Column(name = "IMAGE_URL")
    private String imageUrl;
    @Column(name = "MICROSD_ENABLED")
    private Short microsdEnabled;
    @Column(name = "PUBLISHED")
    @Temporal(TemporalType.DATE)
    private Date published;
    @Column(name = "RAM")
    private Short ram;
    @Column(name = "REAR_CAMERA")
    private Double rearCamera;
    @Column(name = "RES_X")
    private Short resX;
    @Column(name = "RES_Y")
    private Short resY;
    @Column(name = "ROM")
    private Integer rom;
    @Size(max = 255)
    @Column(name = "TYPE_NAME")
    private String typeName;
    @Column(name = "DELETED")
    private Short deleted;
    @JoinColumn(name = "FK_SIM", referencedColumnName = "ID")
    @ManyToOne
    private Sim fkSim;
    @JoinColumn(name = "FK_PROCESSOR", referencedColumnName = "ID")
    @ManyToOne
    private Processor fkProcessor;
    @JoinColumn(name = "FK_OS_VERSION", referencedColumnName = "ID")
    @ManyToOne
    private OsVersion fkOsVersion;
    @JoinColumn(name = "FK_MANUFACTURER", referencedColumnName = "ID")
    @ManyToOne
    private Manufacturer fkManufacturer;
    @JoinColumn(name = "FK_GPU", referencedColumnName = "ID")
    @ManyToOne
    private Gpu fkGpu;
    @OneToMany(mappedBy = "fkPhoneType")
    private List<Advertisement> advertisementList;

    public PhoneType() {
    }

    public PhoneType(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getDisplayInches() {
        return displayInches;
    }

    public void setDisplayInches(Double displayInches) {
        this.displayInches = displayInches;
    }

    public Short getDpi() {
        return dpi;
    }

    public void setDpi(Short dpi) {
        this.dpi = dpi;
    }

    public Double getFrontCamera() {
        return frontCamera;
    }

    public void setFrontCamera(Double frontCamera) {
        this.frontCamera = frontCamera;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Short getMicrosdEnabled() {
        return microsdEnabled;
    }

    public void setMicrosdEnabled(Short microsdEnabled) {
        this.microsdEnabled = microsdEnabled;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Short getRam() {
        return ram;
    }

    public void setRam(Short ram) {
        this.ram = ram;
    }

    public Double getRearCamera() {
        return rearCamera;
    }

    public void setRearCamera(Double rearCamera) {
        this.rearCamera = rearCamera;
    }

    public Short getResX() {
        return resX;
    }

    public void setResX(Short resX) {
        this.resX = resX;
    }

    public Short getResY() {
        return resY;
    }

    public void setResY(Short resY) {
        this.resY = resY;
    }

    public Integer getRom() {
        return rom;
    }

    public void setRom(Integer rom) {
        this.rom = rom;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Short getDeleted() {
        return deleted;
    }

    public void setDeleted(Short deleted) {
        this.deleted = deleted;
    }

    public Sim getFkSim() {
        return fkSim;
    }

    public void setFkSim(Sim fkSim) {
        this.fkSim = fkSim;
    }

    public Processor getFkProcessor() {
        return fkProcessor;
    }

    public void setFkProcessor(Processor fkProcessor) {
        this.fkProcessor = fkProcessor;
    }

    public OsVersion getFkOsVersion() {
        return fkOsVersion;
    }

    public void setFkOsVersion(OsVersion fkOsVersion) {
        this.fkOsVersion = fkOsVersion;
    }

    public Manufacturer getFkManufacturer() {
        return fkManufacturer;
    }

    public void setFkManufacturer(Manufacturer fkManufacturer) {
        this.fkManufacturer = fkManufacturer;
    }

    public Gpu getFkGpu() {
        return fkGpu;
    }

    public void setFkGpu(Gpu fkGpu) {
        this.fkGpu = fkGpu;
    }

    @XmlTransient
    public List<Advertisement> getAdvertisementList() {
        return advertisementList;
    }

    public void setAdvertisementList(List<Advertisement> advertisementList) {
        this.advertisementList = advertisementList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PhoneType)) {
            return false;
        }
        PhoneType other = (PhoneType) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hu.bme.aait.mobilpiac.entities.PhoneType[ id=" + id + " ]";
    }
    
}
