/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.passportinfo.db.facade.PasscheckConfFacade;

/**
 *
 * @author stechiev
 */
@Singleton
@Startup
public class PropsHolder {

    @EJB
    PasscheckConfFacade confFacade;
    private String month;
    private String dayOfWeek;
    private String hour;
    private String minute;

    private String jdbcDbUrl;
    private String jdbcUser;
    private String jdbcPass;

    private String csvURL;

    private Logger log = LoggerFactory.getLogger(PropsHolder.class);

    @PostConstruct
    private void init() {

        /*this.month = confFacade.getConfValueByName("month");
        this.dayOfWeek = confFacade.getConfValueByName("dayOfWeek");
        this.hour = confFacade.getConfValueByName("hour");
        this.minute = confFacade.getConfValueByName("minute");
        this.csvURL = confFacade.getConfValueByName("csvURL");*/

        log.info("Singleton initialized; properties are: {}", propertiesToString());
    }

    public String getMonth() {
        return confFacade.getConfValueByName("month");
    }

    public String getDayOfWeek() {
        return confFacade.getConfValueByName("dayOfWeek");
    }

    public String getHour() {
        return confFacade.getConfValueByName("hour");
    }

    public String getMinute() {
        return confFacade.getConfValueByName("minute");
    }

    public String getCsvURL() {
        return confFacade.getConfValueByName("csvURL");
    }

    public String getJdbcDbUrl() {
        return confFacade.getConfValueByName("jdbcDbUrl");
    }

    public String getJdbcUser() {
        return confFacade.getConfValueByName("jdbcUser");
    }

    public String getJdbcPass() {
        return confFacade.getConfValueByName("jdbcPass");
    }

    public String getCsvRowRegex() {
        return confFacade.getConfValueByName("csvRowRegex");
    }

    public String propertiesToString() {
        return "{" + "confFacade=" + confFacade + ", month=" + month + ", dayOfWeek=" + dayOfWeek + ", hour=" + hour + ", minute=" + minute + ", jdbcDbUrl=" + jdbcDbUrl + ", jdbcUser=" + jdbcUser + ", jdbcPass=" + jdbcPass + ", csvURL=" + csvURL + ", log=" + log + '}';
    }
    

}
