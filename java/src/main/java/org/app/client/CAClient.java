package org.app.client;

import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Properties;
import java.util.logging.Level;

import org.app.user.UserContext;
import org.app.util.Util;
import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;
import org.hyperledger.fabric_ca.sdk.HFCAClient;
import org.hyperledger.fabric_ca.sdk.RegistrationRequest;
import org.hyperledger.fabric_ca.sdk.exception.EnrollmentException;

public class CAClient {
	
	 String caUrl;
	    Properties caProperties;

	    HFCAClient client;
	    UserContext adminContext;

	    //constructor
	    public CAClient(String caUrl,Properties properties) throws IllegalAccessException, InstantiationException, ClassNotFoundException, CryptoException, InvalidArgumentException, NoSuchMethodException, InvocationTargetException, MalformedURLException
	    {
	        this.caUrl=caUrl;
	        this.caProperties=properties;
	        init();
	    }

	    //Create and set cryptosuite for the client
	    public void init() throws IllegalAccessException, InstantiationException, ClassNotFoundException, CryptoException, InvalidArgumentException, NoSuchMethodException, InvocationTargetException, MalformedURLException
	    {
	        CryptoSuite cryptoSuite=CryptoSuite.Factory.getCryptoSuite();
	        client=HFCAClient.createNewInstance(caUrl,caProperties);
	        client.setCryptoSuite(cryptoSuite);
	    }

	    //get the instance of HFCAClient
	    public HFCAClient getInstance()
	    {
	        return client;
	    }

	    //Get the instance of usercontext to register and enroll user
	    public void setAdminUserContext(UserContext userContext)
	    {
	        this.adminContext=userContext;
	    }

	    //Enroll the admin user
	    public UserContext enrollAdminUser(String userName,String password) throws Exception
	    {
	        Util.cleanUp();
	        UserContext userContext=Util.readUserContext(adminContext.getAffiliation(),userName);
	        if(userContext!=null)
	        {
	            System.out.println("Admin is already enrolled");
	            return userContext;
	        }
	        Enrollment adminEnrollment=client.enroll(userName,password);
	        adminContext.setEnrollment(adminEnrollment);
	        System.out.println("Admin enrolled");
	        Util.writeUserContext(adminContext);
	        return adminContext;

	    }
	    
	    public String registerUser(String username, String organization) throws Exception 
	    {
			UserContext userContext = Util.readUserContext(adminContext.getAffiliation(), username);
			if (userContext != null) {
				Logger.getLogger(CAClient.class.getName()).log(Level.WARNING, "CA -" + caUrl +" User " + username+ " is already registered.");
				return null;
			}
			RegistrationRequest rr = new RegistrationRequest(username, organization);
			String enrollmentSecret = client.register(rr, adminContext);
			Logger.getLogger(CAClient.class.getName()).log(Level.INFO, "CA -" + caUrl + " Registered User - " + username);
			return enrollmentSecret;
	    }


}
