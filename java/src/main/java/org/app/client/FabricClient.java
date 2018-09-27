package org.app.client;

import java.lang.reflect.InvocationTargetException;

import org.app.user.UserContext;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;

public class FabricClient {
	
	private HFClient client;
	
	//get instance of client
	public HFClient getInstance()
	{
		return client;
	}
	
	public FabricClient(User context) throws IllegalAccessException, InstantiationException, ClassNotFoundException, CryptoException, InvalidArgumentException, NoSuchMethodException, InvocationTargetException
	{
		CryptoSuite cryptoSuite=CryptoSuite.Factory.getCryptoSuite();
		//setup client
		client=HFClient.createNewInstance();
		client.setCryptoSuite(cryptoSuite);
		client.setUserContext(context);
	}

}
