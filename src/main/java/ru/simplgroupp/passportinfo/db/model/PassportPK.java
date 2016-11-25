/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.db.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author stechiev
 */
@Embeddable
public class PassportPK implements Serializable {
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 4)
    @Column(name = "passp_series")
    private String passpSeries;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 6)
    @Column(name = "passp_number")
    private String passpNumber;

    public PassportPK() {
    }

    public PassportPK(String passpSeries, String passpNumber) {
        this.passpSeries = passpSeries;
        this.passpNumber = passpNumber;
    }

    public String getPasspSeries() {
        return passpSeries;
    }

    public void setPasspSeries(String passpSeries) {
        this.passpSeries = passpSeries;
    }

    public String getPasspNumber() {
        return passpNumber;
    }

    public void setPasspNumber(String passpNumber) {
        this.passpNumber = passpNumber;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (passpSeries != null ? passpSeries.hashCode() : 0);
        hash += (passpNumber != null ? passpNumber.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof PassportPK)) {
            return false;
        }
        PassportPK other = (PassportPK) object;
        if ((this.passpSeries == null && other.passpSeries != null) || (this.passpSeries != null && !this.passpSeries.equals(other.passpSeries))) {
            return false;
        }
        if ((this.passpNumber == null && other.passpNumber != null) || (this.passpNumber != null && !this.passpNumber.equals(other.passpNumber))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.simplgroupp.passportinfo.db.model.PassportPK[ passpSeries=" + passpSeries + ", passpNumber=" + passpNumber + " ]";
    }
    
}
