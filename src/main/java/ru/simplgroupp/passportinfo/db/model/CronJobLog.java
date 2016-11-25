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
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
@Table(name = "cron_job_log")
@NamedQueries({
    @NamedQuery(name = "CronJobLog.findAll", query = "SELECT c FROM CronJobLog c"),
    @NamedQuery(name = "CronJobLog.findById", query = "SELECT c FROM CronJobLog c WHERE c.id = :id"),
    @NamedQuery(name = "CronJobLog.findByStartDate", query = "SELECT c FROM CronJobLog c WHERE c.startDate = :startDate"),
    @NamedQuery(name = "CronJobLog.findByEndDate", query = "SELECT c FROM CronJobLog c WHERE c.endDate = :endDate"),
    @NamedQuery(name = "CronJobLog.findByStatus", query = "SELECT c FROM CronJobLog c WHERE c.status = :status"),
    @NamedQuery(name = "CronJobLog.findByErrorDesc", query = "SELECT c FROM CronJobLog c WHERE c.errorDesc = :errorDesc"),
    @NamedQuery(name = "CronJobLog.findByUploadType", query = "SELECT c FROM CronJobLog c WHERE c.uploadType = :uploadType")})
public class CronJobLog implements Serializable {
    
    /*public static final String TYPE_URL ="URL";
    public static final String TYPE_FILEUPLOAD ="FILEUPLOAD";*/
    
    public static final String STATUS_SUCCESS = "SUCCESS";
    public static final String STATUS_ERROR ="ERROR";
    
    
    
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "start_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    @Column(name = "end_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "status")
    private String status;
    @Size(max = 1024)
    @Column(name = "error_desc")
    private String errorDesc;
    @Size(max = 20)
    @Column(name = "upload_type")
    private String uploadType;

    public CronJobLog() {
    }

    public CronJobLog(Integer id) {
        this.id = id;
    }

    public CronJobLog(Integer id, Date startDate, String status) {
        this.id = id;
        this.startDate = startDate;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc;
    }

    public String getUploadType() {
        return uploadType;
    }

    public void setUploadType(String uploadType) {
        this.uploadType = uploadType;
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
        if (!(object instanceof CronJobLog)) {
            return false;
        }
        CronJobLog other = (CronJobLog) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ru.simplgroupp.passportinfo.db.model.CronJobLog[ id=" + id + " ]";
    }
    
}
