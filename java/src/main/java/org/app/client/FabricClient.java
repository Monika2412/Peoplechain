package org.app.client;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.HFClient;
import org.hyperledger.fabric.sdk.InstallProposalRequest;
import org.hyperledger.fabric.sdk.Peer;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.User;
import org.hyperledger.fabric.sdk.exception.CryptoException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.security.CryptoSuite;


public class FabricClient {

	private HFClient instance;

	//getting instance of HFClient
	public HFClient getInstance() {
		return instance;
	}

	//constructor
	public FabricClient(User context) throws CryptoException, InvalidArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException {
		CryptoSuite cryptoSuite = CryptoSuite.Factory.getCryptoSuite();
		// setup the client
		instance = HFClient.createNewInstance();
		instance.setCryptoSuite(cryptoSuite);
		instance.setUserContext(context);
	}
	
	//create channel client
	public ChannelClient createChannelClient(String channelName) throws InvalidArgumentException
	{
		Channel channel=instance.newChannel(channelName);
		ChannelClient channelClient=new ChannelClient(channelName,channel,this);
		return channelClient;
	}
	
	//Deploy chaincode
	public Collection<ProposalResponse> deployChaincode(String chaincodeName,String chaincodePath,String codePath,String lang,String version,Collection<Peer> peers) throws InvalidArgumentException,IOException,ProposalException
	{
		InstallProposalRequest request=instance.newInstallProposalRequest();
		ChaincodeID.Builder chaincodeIDBuilder=ChaincodeID.newBuilder().setName(chaincodeName).setVersion(version).setPath(chaincodePath);
		ChaincodeID chaincodeID=chaincodeIDBuilder.build();
		Logger.getLogger(FabricClient.class.getName()).log(Level.INFO,"Deploying chaincode " + chaincodeName + " using Fabric client " + instance.getUserContext().getMspId()+ " " + instance.getUserContext().getName());
		request.setChaincodeID(chaincodeID);
		request.setUserContext(instance.getUserContext());
		request.setChaincodeSourceLocation(new File(codePath));
		request.setChaincodeVersion(version);
		Collection<ProposalResponse> responses = instance.sendInstallProposal(request, peers);
        return responses;
	}

}