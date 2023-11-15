package com.softlond.base.aws;


public class SESFrom {

	public static void main(String[] args) throws Exception {
     
		
		AmazonEmail email=new AmazonEmail("juan.gallego@neimpetu.com", "Hola mundo", String.join(
	    	    System.getProperty("line.separator"),
	    	    "<h1>Amazon SES SMTP Email Test</h1>",
	    	    "<p>This email was sent with Amazon SES using the ", 
	    	    "<a href='https://github.com/javaee/javamail'>Javamail Package</a>",
	    	    " for <a href='https://www.java.com'>Java</a>."
	    	));
		
		email.sendEmail();

    }
}
