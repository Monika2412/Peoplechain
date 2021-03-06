
package org.app.user;

import org.app.client.CAClient;
import org.app.config.Config;
import org.app.util.Util;


public class RegisterEnrollUser {

	public static void main(String args[]) {
		try {
			Util.cleanUp();
			String caUrl = Config.CA_ORG1_URL;
			CAClient caClient = new CAClient(caUrl, null);
			// Enroll Admin to Org1MSP
			UserContext adminUserContext = new UserContext();
			adminUserContext.setName(Config.ADMIN);
			adminUserContext.setAffiliation(Config.ORG1);
			adminUserContext.setMspId(Config.ORG1_MSP);
			caClient.setAdminUserContext(adminUserContext);
			adminUserContext = caClient.enrollAdminUser(Config.ADMIN, Config.ADMIN_PASSWORD);

			//Register and enroll user to org1
			UserContext userContext=new UserContext();
			String name="user"+System.currentTimeMillis();
			userContext.setName(name);
			userContext.setAffiliation(Config.ORG1);
			userContext.setMspId(Config.ORG1_MSP);
			
			String eSecret=caClient.registerUser(name,Config.ORG1); 
			
			userContext=caClient.enrollUser(userContext, eSecret);
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}