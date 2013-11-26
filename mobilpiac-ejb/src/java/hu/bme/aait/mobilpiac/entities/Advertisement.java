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
import javax.persistence.CascadeType;
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
@Table(name = "ADVERTISEMENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Advertisement.findAll", query = "SELECT a FROM Advertisement a"),
    @NamedQuery(name = "Advertisement.findById", query = "SELECT a FROM Advertisement a WHERE a.id = :id"),
    @NamedQuery(name = "Advertisement.findByDescription", query = "SELECT a FROM Advertisement a WHERE a.description = :description"),
    @NamedQuery(name = "Advertisement.findByFinished", query = "SELECT a FROM Advertisement a WHERE a.finished = :finished"),
    @NamedQuery(name = "Advertisement.findByMinPrice", query = "SELECT a FROM Advertisement a WHERE a.minPrice = :minPrice"),
    @NamedQuery(name = "Advertisement.findByPublished", query = "SELECT a FROM Advertisement a WHERE a.published = :published")})
public class Advertisement implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;
    @Size(max = 255)
    @Column(name = "FINISHED")
    private String finished;
    @Column(name = "MIN_PRICE")
    private Integer minPrice;
    @Column(name = "PUBLISHED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date published;
    @JoinColumn(name = "FK_USER", referencedColumnName = "ID")
    @ManyToOne
    private Users fkUser;
    @JoinColumn(name = "FK_PHONE_TYPE", referencedColumnName = "ID")
    @ManyToOne
    private PhoneType fkPhoneType;
    @JoinColumn(name = "FK_NETWORK_LOCK", referencedColumnName = "ID")
    @ManyToOne
    private MobileNetwork fkNetworkLock;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "advertisementId")
    private List<Bids> bidsList;

    public Advertisement() {
    }

    public Advertisement(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFinished() {
        return finished;
    }

    public void setFinished(String finished) {
        this.finished = finished;
    }

    public Integer getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(Integer minPrice) {
        this.minPrice = minPrice;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Users getFkUser() {
        return fkUser;
    }

    public void setFkUser(Users fkUser) {
        this.fkUser = fkUser;
    }

    public PhoneType getFkPhoneType() {
        return fkPhoneType;
    }

    public void setFkPhoneType(PhoneType fkPhoneType) {
        this.fkPhoneType = fkPhoneType;
    }

    public MobileNetwork getFkNetworkLock() {
        return fkNetworkLock;
    }

    public void setFkNetworkLock(MobileNetwork fkNetworkLock) {
        this.fkNetworkLock = fkNetworkLock;
    }

    @XmlTransient
    public List<Bids> getBidsList() {
        return bidsList;
    }

    public void setBidsList(List<Bids> bidsList) {
        this.bidsList = bidsList;
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
        if (!(object instanceof Advertisement)) {
            return false;
        }
        Advertisement other = (Advertisement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hu.bme.aait.mobilpiac.entities.Advertisement[ id=" + id + " ]";
    }
    
}
