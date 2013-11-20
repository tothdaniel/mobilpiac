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
@Table(name = "SIM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Sim.findAll", query = "SELECT s FROM Sim s"),
    @NamedQuery(name = "Sim.findById", query = "SELECT s FROM Sim s WHERE s.id = :id"),
    @NamedQuery(name = "Sim.findBySimType", query = "SELECT s FROM Sim s WHERE s.simType = :simType")})
public class Sim implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Short id;
    @Size(max = 255)
    @Column(name = "SIM_TYPE")
    private String simType;
    @OneToMany(mappedBy = "fkSim")
    private List<PhoneType> phoneTypeList;

    public Sim() {
    }

    public Sim(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getSimType() {
        return simType;
    }

    public void setSimType(String simType) {
        this.simType = simType;
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
        if (!(object instanceof Sim)) {
            return false;
        }
        Sim other = (Sim) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hu.bme.aait.mobilpiac.entities.Sim[ id=" + id + " ]";
    }
    
}
