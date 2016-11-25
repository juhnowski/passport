/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.db.model;

import java.io.Serializable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author stechiev
 */
@Entity
@Table(name = "passport")
@NamedQueries({
    @NamedQuery(name = "Passport.findAll", query = "SELECT p FROM Passport p"),
    @NamedQuery(name = "Passport.findByPasspSeries", query = "SELECT p FROM Passport p WHERE p.passportPK.passpSeries = :passpSeries"),
    @NamedQuery(name = "Passport.findByPasspNumber", query = "SELECT p FROM Passport p WHERE p.passportPK.passpNumber = :passpNumber")})
public class Passport implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected PassportPK passportPK;

    public Passport() {
    }

    public Passport(PassportPK passportPK) {
        this.passportPK = passportPK;
    }

    public Passport(String passpSeries, String passpNumber) {
        this.passportPK = new PassportPK(passpSeries, passpNumber);
    }

    public PassportPK getPassportPK() {
        return passportPK;
    }

    public void setPassportPK(PassportPK passportPK) {
        this.passportPK = passportPK;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passportPK != null ? passportPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Passport)) {
            return false;
        }
        Passport other = (Passport) object;
        if ((this.passportPK == null && other.passportPK != null) || (this.passportPK != null && !this.passportPK.equals(other.passportPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.simplgroupp.passportinfo.db.model.Passport[ passportPK=" + passportPK + " ]";
    }
    
}
