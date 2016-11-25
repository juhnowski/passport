/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.services.rest;

import java.util.List;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.validation.constraints.Size;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.simplgroupp.passportinfo.csv.CsvUtil;
import ru.simplgroupp.passportinfo.db.facade.PassportFacade;
import ru.simplgroupp.passportinfo.db.model.Passport;
import ru.simplgroupp.passportinfo.db.model.PassportPK;
import ru.simplgroupp.passportinfo.exception.PassportInfoException;

/**
 * REST Web Service
 *
 * @author stechiev
 */
@Path("passcheck")
public class PassportCheck {

    @Context
    private UriInfo context;

    @EJB
    private PassportFacade passportFacade;

    @EJB
    private CsvUtil csvUtil;

    private Logger log = LoggerFactory.getLogger(PassportCheck.class);

    /**
     * Creates a new instance of PassportCheck
     */
    public PassportCheck() {
    }

    /*@GET
    @Produces("application/json; charset=utf-8")
    @RolesAllowed({"passpinfo_client"})
    public PIResponse getPassInfo(@QueryParam(value = "series")
                            String series,
                            @QueryParam(value = "number")
                            String number) {
        return check(series, number);
    }*/
    
    @GET
    @Produces("application/json; charset=utf-8")
    @RolesAllowed({"passpinfo_client"})
    public PIResponse getPassInfo(@QueryParam(value = "passpnum")
                            String passpNum){
        if(passpNum!=null && passpNum.length()==10){
            String series = passpNum.substring(0,4);
            String number = passpNum.substring(4,passpNum.length());
            return check(series, number);
        }
        
        return new PIResponse(2,"Длина серии и номера паспорта должна составлять 10 цифр!");
    }
    
    private PIResponse check(String series, String number){
        log.info("Series: {}; Number: {}", series, number);
        try {            
            validate(series, number);
            if (passportFacade.find(new PassportPK(series, number)) != null) {
                return new PIResponse(1, "Паспорт найден среди недействительных");
            }
        } catch (Exception e) {
            log.error("Opps! Exception happened", e.getMessage());
            return new PIResponse(2, e.getMessage());
        }
        return new PIResponse(0, "Паспорт не найден среди недействительных");
    }

    private void validate(String... args) throws PassportInfoException {
        for (String arg : args) {
            if (arg == null || arg.isEmpty()) {
                throw new PassportInfoException("Пустое или не присвоенное значение одного из параметров - series, number");
            }
            if(!arg.matches("[0-9]+")){
                throw new PassportInfoException("Значения параметров паспорта могут содержать только цифры");
            }
        }
    }

    @GET
    @Path("loadcsv")
    @Produces("text/plain")
    @RolesAllowed({"passpinfo_client"})
    public Response runCsvProcessing(@QueryParam(value = "url") String url) {

        List<Passport> passpList = passportFacade.findAll();
        csvUtil.processURL(url);
        log.info("Size is: " + passpList.size());

        return Response.ok("csv loading started").build();
    }
}
