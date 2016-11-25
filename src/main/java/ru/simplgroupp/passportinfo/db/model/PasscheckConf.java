/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.db.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author stechiev
 */
@Entity
@Table(name = "passcheck_conf")
@NamedQueries({
    @NamedQuery(name = "PasscheckConf.findAll", query = "SELECT p FROM PasscheckConf p"),
    @NamedQuery(name = "PasscheckConf.findById", query = "SELECT p FROM PasscheckConf p WHERE p.id = :id"),
    @NamedQuery(name = "PasscheckConf.findByPropName", query = "SELECT p FROM PasscheckConf p WHERE p.propName = :propName"),
    @NamedQuery(name = "PasscheckConf.findByPropValue", query = "SELECT p FROM PasscheckConf p WHERE p.propValue = :propValue"),
    @NamedQuery(name = "PasscheckConf.findByUpdated", query = "SELECT p FROM PasscheckConf p WHERE p.updated = :updated")})
public class PasscheckConf implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    private Short id;
    @Size(max = 32)
    @Column(name = "prop_name")
    private String propName;
    @Size(max = 256)
    @Column(name = "prop_value")
    private String propValue;
    @Basic(optional = false)
    @NotNull
    @Column(name = "updated")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

    public PasscheckConf() {
    }

    public PasscheckConf(Short id) {
        this.id = id;
    }

    public PasscheckConf(Short id, Date updated) {
        this.id = id;
        this.updated = updated;
    }

    public Short getId() {
        return id;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public String getPropName() {
        return propName;
    }

    public void setPropName(String propName) {
        this.propName = propName;
    }

    public String getPropValue() {
        return propValue;
    }

    public void setPropValue(String propValue) {
        this.propValue = propValue;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
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
        if (!(object instanceof PasscheckConf)) {
            return false;
        }
        PasscheckConf other = (PasscheckConf) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.simplgroupp.passportinfo.db.model.PasscheckConf[ id=" + id + " ]";
    }
    
}
