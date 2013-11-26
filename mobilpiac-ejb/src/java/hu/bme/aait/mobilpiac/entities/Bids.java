/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hu.bme.aait.mobilpiac.entities;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Daniel
 */
@Entity
@Table(name = "BIDS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Bids.findAll", query = "SELECT b FROM Bids b"),
    @NamedQuery(name = "Bids.findById", query = "SELECT b FROM Bids b WHERE b.id = :id"),
    @NamedQuery(name = "Bids.findByDateOfBid", query = "SELECT b FROM Bids b WHERE b.dateOfBid = :dateOfBid"),
    @NamedQuery(name = "Bids.findByPrice", query = "SELECT b FROM Bids b WHERE b.price = :price")})
public class Bids implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Column(name = "DATE_OF_BID")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOfBid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "PRICE")
    private int price;
    @JoinColumn(name = "BIDDER_USER_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Users bidderUserId;
    @JoinColumn(name = "ADVERTISEMENT_ID", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Advertisement advertisementId;

    public Bids() {
    }

    public Bids(Long id) {
        this.id = id;
    }

    public Bids(Long id, int price) {
        this.id = id;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfBid() {
        return dateOfBid;
    }

    public void setDateOfBid(Date dateOfBid) {
        this.dateOfBid = dateOfBid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public Users getBidderUserId() {
        return bidderUserId;
    }

    public void setBidderUserId(Users bidderUserId) {
        this.bidderUserId = bidderUserId;
    }

    public Advertisement getAdvertisementId() {
        return advertisementId;
    }

    public void setAdvertisementId(Advertisement advertisementId) {
        this.advertisementId = advertisementId;
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
        if (!(object instanceof Bids)) {
            return false;
        }
        Bids other = (Bids) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hu.bme.aait.mobilpiac.entities.Bids[ id=" + id + " ]";
    }
    
}
