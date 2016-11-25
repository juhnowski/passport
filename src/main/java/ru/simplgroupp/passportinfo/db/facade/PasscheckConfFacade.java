/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.db.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import ru.simplgroupp.passportinfo.db.model.PasscheckConf;

/**
 *
 * @author stechiev
 */
@Stateless
public class PasscheckConfFacade extends AbstractFacade<PasscheckConf> {

    @PersistenceContext(unitName = "PassportInfo_pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PasscheckConfFacade() {
        super(PasscheckConf.class);
    }

    public String getConfValueByName(String name) {
        try {
            PasscheckConf conf = em.createNamedQuery("PasscheckConf.findByPropName", PasscheckConf.class)
                    .setParameter("propName", name)
                    .getSingleResult();
            return conf.getPropValue();
        } catch (NoResultException nre) {
            return null;
        }
    }

}
