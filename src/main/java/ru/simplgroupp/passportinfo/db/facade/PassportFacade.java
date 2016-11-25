/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.simplgroupp.passportinfo.db.facade;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import ru.simplgroupp.passportinfo.db.model.Passport;

/**
 *
 * @author stechiev
 */
@Stateless
public class PassportFacade extends AbstractFacade<Passport> {
    @PersistenceContext(unitName = "PassportInfo_pu")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PassportFacade() {
        super(Passport.class);
    }

    @Override
    public void edit(Passport entity) {
        super.edit(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
    }

    @Override
    public void create(Passport entity) {
        super.create(entity); //To change body of generated methods, choose Tools | Templates.
        em.flush();
    }
    
    
    
}
