/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.ui;

import java.io.IOException;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.passportinfo.csv.CsvCopyUtil;
import ru.simplgroupp.passportinfo.csv.CsvUtil;
import ru.simplgroupp.passportinfo.db.model.CronJobLogType;

/**
 *
 * @author stechiev
 */
@Named(value = "csvUploadView")
 @RequestScoped
//@ManagedBean
public class CsvUploadView{

    @EJB
    private CsvUtil csvUtil;

    private UploadedFile file;
    
    private Logger log = LoggerFactory.getLogger(CsvUploadView.class);

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void handleFileUpload(FileUploadEvent event) throws Exception {
        file = event.getFile();
        log.info("File uploaded:" + file);
        if (file != null) {

            String str = file.getFileName();
            String ext = str.substring(str.lastIndexOf('.'), str.length());
            log.info("Uploaded file name: {}; extension: {}", str, ext);
            if (ext.equalsIgnoreCase(".csv")) {
                //copyUtil.copyInDb(file.getInputstream());
                csvUtil.copyInDb(file.getInputstream(), CronJobLogType.FILEUPLOAD);
                
            }
            if (ext.equalsIgnoreCase(".bz2")) {
                //copyUtil.copyInDb(new BZip2CompressorInputStream(file.getInputstream()));
                csvUtil.copyInDb(new BZip2CompressorInputStream(file.getInputstream()), CronJobLogType.FILEUPLOAD);
            }
            FacesMessage message = new FacesMessage("File ", file.getFileName() + "was successfully uploaded. Database refresh started.");
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
    }
}
