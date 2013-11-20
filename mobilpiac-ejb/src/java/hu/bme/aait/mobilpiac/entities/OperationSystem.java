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
@Table(name = "OPERATION_SYSTEM")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "OperationSystem.findAll", query = "SELECT o FROM OperationSystem o"),
    @NamedQuery(name = "OperationSystem.findById", query = "SELECT o FROM OperationSystem o WHERE o.id = :id"),
    @NamedQuery(name = "OperationSystem.findByOsName", query = "SELECT o FROM OperationSystem o WHERE o.osName = :osName")})
public class OperationSystem implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Short id;
    @Size(max = 255)
    @Column(name = "OS_NAME")
    private String osName;
    @OneToMany(mappedBy = "fkOs")
    private List<OsVersion> osVersionList;

    public OperationSystem() {
    }

    public OperationSystem(Short id) {
        this.id = id;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getOsName() {
        return osName;
    }

    public void setOsName(String osName) {
        this.osName = osName;
    }

    @XmlTransient
    public List<OsVersion> getOsVersionList() {
        return osVersionList;
    }

    public void setOsVersionList(List<OsVersion> osVersionList) {
        this.osVersionList = osVersionList;
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
        if (!(object instanceof OperationSystem)) {
            return false;
        }
        OperationSystem other = (OperationSystem) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hu.bme.aait.mobilpiac.entities.OperationSystem[ id=" + id + " ]";
    }
    
}
