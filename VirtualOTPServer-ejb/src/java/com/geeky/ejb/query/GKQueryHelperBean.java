/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeky.ejb.query;

import com.geek.otp.UserAccount;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author kuuga
 */
@Stateless
public class GKQueryHelperBean implements GKQueryHelperLocal {

    @PersistenceContext(unitName = "VIRTUALOTP")
    protected EntityManager em;

    private Session getSession() {
        return (Session) em.getDelegate();
    }

    public UserAccount saveUsers(UserAccount userAccount){
        try {
            System.out.println(": : : REGISTER ACCOUNT:.");
            em.persist(userAccount);
            return userAccount;
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public UserAccount getUserAccount(String username, String password) {
        try {
            Criteria criteria = getSession().createCriteria(UserAccount.class);
            criteria.add(Restrictions.eq("username", username));
            criteria.add(Restrictions.eq("passwd", password));
            return (UserAccount) criteria.uniqueResult();
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }

    }
}
