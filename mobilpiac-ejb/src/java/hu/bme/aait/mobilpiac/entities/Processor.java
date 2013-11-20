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
@Table(name = "PROCESSOR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Processor.findAll", query = "SELECT p FROM Processor p"),
    @NamedQuery(name = "Processor.findById", query = "SELECT p FROM Processor p WHERE p.id = :id"),
    @NamedQuery(name = "Processor.findByChipset", query = "SELECT p FROM Processor p WHERE p.chipset = :chipset"),
    @NamedQuery(name = "Processor.findByClock", query = "SELECT p FROM Processor p WHERE p.clock = :clock"),
    @NamedQuery(name = "Processor.findByFamily", query = "SELECT p FROM Processor p WHERE p.family = :family"),
    @NamedQuery(name = "Processor.findByNumberOfCores", query = "SELECT p FROM Processor p WHERE p.numberOfCores = :numberOfCores")})
public class Processor implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Size(max = 255)
    @Column(name = "CHIPSET")
    private String chipset;
    @Column(name = "CLOCK")
    private Short clock;
    @Size(max = 255)
    @Column(name = "FAMILY")
    private String family;
    @Column(name = "NUMBER_OF_CORES")
    private Short numberOfCores;
    @OneToMany(mappedBy = "fkProcessor")
    private List<PhoneType> phoneTypeList;

    public Processor() {
    }

    public Processor(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getChipset() {
        return chipset;
    }

    public void setChipset(String chipset) {
        this.chipset = chipset;
    }

    public Short getClock() {
        return clock;
    }

    public void setClock(Short clock) {
        this.clock = clock;
    }

    public String getFamily() {
        return family;
    }

    public void setFamily(String family) {
        this.family = family;
    }

    public Short getNumberOfCores() {
        return numberOfCores;
    }

    public void setNumberOfCores(Short numberOfCores) {
        this.numberOfCores = numberOfCores;
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
        if (!(object instanceof Processor)) {
            return false;
        }
        Processor other = (Processor) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hu.bme.aait.mobilpiac.entities.Processor[ id=" + id + " ]";
    }
    
}
