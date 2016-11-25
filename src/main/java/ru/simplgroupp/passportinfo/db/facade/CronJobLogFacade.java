/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.db.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ru.simplgroupp.passportinfo.db.model.CronJobLog;

/**
 *
 * @author stechiev
 */
@Stateless
public class CronJobLogFacade extends AbstractFacade<CronJobLog> {
    @PersistenceContext(unitName = "PassportInfo_pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CronJobLogFacade() {
        super(CronJobLog.class);
    }
    
}
