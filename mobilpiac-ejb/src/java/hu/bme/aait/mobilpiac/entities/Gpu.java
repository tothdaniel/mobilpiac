/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "GPU")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Gpu.findAll", query = "SELECT g FROM Gpu g"),
    @NamedQuery(name = "Gpu.findById", query = "SELECT g FROM Gpu g WHERE g.id = :id"),
    @NamedQuery(name = "Gpu.findByGpuName", query = "SELECT g FROM Gpu g WHERE g.gpuName = :gpuName")})
public class Gpu implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Size(max = 255)
    @Column(name = "GPU_NAME")
    private String gpuName;
    @OneToMany(mappedBy = "fkGpu")
    private List<PhoneType> phoneTypeList;

    public Gpu() {
    }

    public Gpu(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGpuName() {
        return gpuName;
    }

    public void setGpuName(String gpuName) {
        this.gpuName = gpuName;
    }

    @XmlTransient
    public List<PhoneType> getPhoneTypeList() {
        return phoneTypeList;
    }

    public void setPhoneTypeList(List<PhoneType> phoneTypeList) {
        this.phoneTypeList = phoneTypeList;
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
        if (!(object instanceof Gpu)) {
            return false;
        }
        Gpu other = (Gpu) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hu.bme.aait.mobilpiac.entities.Gpu[ id=" + id + " ]";
    }
    
}
