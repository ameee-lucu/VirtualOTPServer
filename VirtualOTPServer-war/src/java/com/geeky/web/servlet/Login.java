/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.geeky.web.servlet;

import com.geek.otp.UserAccount;
import com.geeky.ejb.query.GKQueryHelperLocal;
import com.geeky.web.util.GoogleAuthenticator;
import com.geeky.web.util.OTPProperties;
import com.geeky.web.util.SHAHash;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.configuration.PropertiesConfiguration;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.naming.InitialContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author kuuga
 */
public class Login extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();


        try {
            System.out.println(": :: IM HERE:.");

            GKQueryHelperLocal queryHelper = (GKQueryHelperLocal) InitialContext.doLookup("VirtualOTPServer/GKQueryHelperBean/local");
            PropertiesConfiguration config = OTPProperties.getOTPConfig();
            String templatePath = "/apps/otp/template";
            Configuration cfg = new Configuration();
            cfg.setDirectoryForTemplateLoading(new File(templatePath));
            cfg.setObjectWrapper(new DefaultObjectWrapper());
            Template temp = cfg.getTemplate("login.html");


            HttpSession session = request.getSession();

            Map root = new HashMap();
            String buttonSubmit = request.getParameter("login");

            if (buttonSubmit == null) {
                buttonSubmit = "";
            }

            if (buttonSubmit.equalsIgnoreCase("dologin")) {
                String username = request.getParameter("username");
                String password = request.getParameter("passwd");
                
                System.out.println("=========================================");
                System.out.println(": : : INCOMING LOGIN REQUEST:.");
                System.out.println("=========================================");
                System.out.println(": : : USERNAME             [" + username + "]");
                System.out.println(": : : PASSWORD             [" + SHAHash.SHA1(password) + "]");
                System.out.println("=========================================");
                System.out.println(": : : NOW CHECKING IF THE LOGIN TRUE:.");

                if (username == null || username.trim().equalsIgnoreCase("")) {
                    System.out.println("ERROR ON LOGIN, USERNAME NULL:.");
                    temp = cfg.getTemplate("error.html");
                    temp.process(root, out);
                    session.invalidate();
                } else if (password == null || password.trim().equalsIgnoreCase("")) {
                    System.out.println("ERROR ON LOGIN, PASSWORD NULL:.");
                    temp = cfg.getTemplate("error.html");
                    temp.process(root, out);
                    session.invalidate();
                } else {
                    password = SHAHash.SHA1(password);
                    UserAccount uac = queryHelper.getUserAccount(username, password);
                    if (uac == null) {
                        System.out.println(": : : INVALID LOGIN ID OR PASSWORD:.");
                        temp = cfg.getTemplate("errorLogin.html");
                        temp.process(root, out);
                        session.invalidate();
                    } else {
                        System.out.println(": : : USERNAME FOUND, NOW SAVE TO SESSION AND GIVE TO OTP PAGE:.");
                        temp = cfg.getTemplate("otp.html");
                        temp.process(root, out);
                        session.setAttribute("users", uac);
                    }
                }


            } else if (buttonSubmit.equalsIgnoreCase("dootp")) {
                System.out.println(": : : INCOMING REQUEST TO LOGIN OTP:.");
                UserAccount uac = (UserAccount) session.getAttribute("users");
                if (uac == null) {
                    temp = cfg.getTemplate("error.html");
                    temp.process(root, out);
                    session.invalidate();
                } else {
                    String otpCode = request.getParameter("otpCode");
                    System.out.println(": : : OTP CODE          [" + otpCode + "]:.");
                    long code = Long.parseLong(otpCode);
                    long t = System.currentTimeMillis();
                    GoogleAuthenticator ga = new GoogleAuthenticator();
                    ga.setWindowSize(5);  //should give 5 * 30 seconds of grace...
                    boolean r = ga.check_code(uac.getAccountNo(), code, t);
                    if (r == true) {
                        System.out.println(": : : SUCCESS LOGIN:.");
                        temp = cfg.getTemplate("successLogin.html");
                        temp.process(root, out);
                        session.invalidate();
                    } else {
                        System.out.println(": : : LOGIN FAILED, BACK LO LOGIN PAGE:.");
                        temp = cfg.getTemplate("errorOTP.html");
                        temp.process(root, out);
                        session.invalidate();
                    }
                }
            } else {
                temp.process(temp, out);
            }



        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
