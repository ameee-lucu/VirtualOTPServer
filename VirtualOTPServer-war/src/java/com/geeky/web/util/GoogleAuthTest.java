package com.geeky.web.util;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author kuuga
 */
public class GoogleAuthTest  {


	public static void genSecretTest() {
		String secret = GoogleAuthenticator.generateSecretKey();

		//String url = GoogleAuthenticator.getQRBarcodeURL("testuser", "testhost", secret);
		//System.out.println("Please register " + url);

		System.out.println("Secret key is " + secret);
	}


	// Change this to the saved secret from the running the above test.
	static String savedSecret = "YY2LHNDP7ZLZIYPI";


	public static void authTest()  {
		// enter the code shown on device. Edit this and run it fast before the code expires!
		long code =  383534 ;
		long t = System.currentTimeMillis();
		GoogleAuthenticator ga = new GoogleAuthenticator();
		ga.setWindowSize(5);  //should give 5 * 30 seconds of grace...

		boolean r = ga.check_code(savedSecret, code, t);

		System.out.println("Check code = " + r);
	}

    public static void generateCode()  {
		// enter the code shown on device. Edit this and run it fast before the code expires!
		long code =  185640 ;
		long t = System.currentTimeMillis();
		GoogleAuthenticator ga = new GoogleAuthenticator();
		ga.setWindowSize(5);  //should give 5 * 30 seconds of grace...

		long r = ga.generate_code(savedSecret, t);

		System.out.println("Generate code = " + r);
	}

    public static void main(String[]args){
        //generateCode();
        authTest();
        //genSecretTest();

    }


}
