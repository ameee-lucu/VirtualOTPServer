/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geeky.ejb.query;

import javax.ejb.Local;

/**
 *
 * @author kuuga
 */
@Local
public interface GKQueryHelperLocal {

    public com.geek.otp.UserAccount getUserAccount(java.lang.String username, java.lang.String password);

    public com.geek.otp.UserAccount saveUsers(com.geek.otp.UserAccount userAccount);
    
}
