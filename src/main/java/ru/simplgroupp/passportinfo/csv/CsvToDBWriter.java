/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.csv;

import java.util.List;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.passportinfo.db.facade.PassportFacade;
import ru.simplgroupp.passportinfo.db.model.Passport;
import ru.simplgroupp.passportinfo.db.model.PassportPK;

/**
 *
 * @author stechiev
 */
@Stateless
public class CsvToDBWriter {

    @EJB
    private PassportFacade passportFacade;

    private Logger log = LoggerFactory.getLogger(CsvToDBWriter.class);
    
    /**
     *
     * @param line
     */
        
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    @Asynchronous
    public void persistData(List<String> lines) {        
        for (String line : lines){
        String[] splitted = line.split(",");
        String series = splitted[0];
        String number = splitted[1];
        if (series.length() > 4 || number.length() > 6) {
            return;
        }
        PassportPK passPK = new PassportPK(series, number);
        if (passportFacade.find(passPK) != null) {
            return;
        }
        //log.info("Inserting new Passport {} ....", passPK.toString());
        passportFacade.create(new Passport(passPK));
        //log.info("inserted");
        }
         log.info("Persisted 1000 rows...");
    }

}
