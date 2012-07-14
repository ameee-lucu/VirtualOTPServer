/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.geeky.web.util;
import java.io.File;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;

/**
 *
 * @author kuuga
 */
public class OTPProperties {

    private static final String OTP_SYSTEM_PROPERTIES = "/apps/otp/properties/otpConfig.properties";
    private static final String OTP_SQL_QUERIES_PROPERTIES = "/apps/otp/properties/escrowSQL.properties";
    private static final String OTP_GUI_PROPERTIES = "/apps/otp/properties/escrowGUI.properties";
    public static final String OTP_ACTIVITY_LOG_PROPERTIES = "/apps/otp/properties/escrowActivityLog.properties";
    private static transient PropertiesConfiguration otpConfig = null;
    private static transient PropertiesConfiguration otpSQLConfig = null;
    private static transient PropertiesConfiguration otpGUIConfig = null;
    private static transient PropertiesConfiguration otpActivityLogConfig = null;
    static final Object LOCK4 = new Object();
    static final Object LOCK5 = new Object();
    static final Object LOCK6 = new Object();
    static final Object LOCK7 = new Object();

    public static PropertiesConfiguration createConfigFile(String file, boolean disableDelimiter) {
        try {
            PropertiesConfiguration config = new PropertiesConfiguration();
            config.setDelimiterParsingDisabled(disableDelimiter);
            File f = new File(file);
            config.load(file);
            config.setReloadingStrategy(new FileChangedReloadingStrategy());
            return config;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }

    }

    public static PropertiesConfiguration getOTPConfig() {
        synchronized (LOCK4) {
            if (otpConfig == null) {
                otpConfig = createConfigFile(OTP_SYSTEM_PROPERTIES, false);
            }
        }
        return otpConfig;
    }

    

}
