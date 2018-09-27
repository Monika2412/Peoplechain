package org.app.user;

import java.lang.reflect.InvocationTargetException;

import java.net.MalformedURLException;

import org.app.client.CAClient;
import org.app.config.Config;
import org.app.util.Util;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;


public class RegisterEnrollUser {
	
	public static void main(String[] args) throws Exception
    {
		try
		{
        Util.cleanUp();
        String caUrl=Config.CA_ORG1_URL;
        CAClient client=new CAClient(caUrl,null);

        //Enroll Admin to org1
        UserContext adminUserContext=new UserContext();
        adminUserContext.setName(Config.ADMIN);
        adminUserContext.setAffiliation(Config.ORG1);
        adminUserContext.setMspId(Config.ORG1_MSP);
        client.setAdminUserContext(adminUserContext);
        adminUserContext=client.enrollAdminUser(Config.ADMIN,Config.ADMIN_PASSWORD);
        
     // Register and Enroll user to Org1MSP
     			UserContext userContext = new UserContext();
     			String name = "user"+System.currentTimeMillis();
     			userContext.setName(name);
     			userContext.setAffiliation(Config.ORG1);
     			userContext.setMspId(Config.ORG1_MSP);

     			String eSecret = client.registerUser(name, Config.ORG1);

     			userContext = client.enrollUser(userContext, eSecret);

        
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

    }


}
