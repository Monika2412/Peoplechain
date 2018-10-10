package org.app.client;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.hyperledger.fabric.sdk.TransactionRequest.Type;
import org.hyperledger.fabric.sdk.exception.ChaincodeEndorsementPolicyParseException;
import org.hyperledger.fabric.sdk.exception.InvalidArgumentException;
import org.hyperledger.fabric.sdk.exception.ProposalException;
import org.hyperledger.fabric.sdk.BlockEvent.TransactionEvent;
import org.hyperledger.fabric.sdk.ChaincodeEndorsementPolicy;
import org.hyperledger.fabric.sdk.ChaincodeID;
import org.hyperledger.fabric.sdk.Channel;
import org.hyperledger.fabric.sdk.InstantiateProposalRequest;
import org.hyperledger.fabric.sdk.ProposalResponse;
import org.hyperledger.fabric.sdk.QueryByChaincodeRequest;
import org.hyperledger.fabric.sdk.TransactionProposalRequest;

public class ChannelClient {
	
	String name;
	Channel channel;
	FabricClient fabClient;
	
	public String getName()
	{
		return name;
	}
	
	public Channel getChannel()
	{
		return channel;
	}
	
	public FabricClient getFabricClient()
	{
		return fabClient;
	}
	
	//Constructor
	public ChannelClient(String name,Channel channel,FabricClient fabClient)
	{
		this.name=name;
		this.channel=channel;
		this.fabClient=fabClient;
	}
	
	//Instantiate chaincode on channel
	public Collection<ProposalResponse> instantiateChaincode(String chaincodeName,String chaincodeVersion,String chaincodePath,String language,String functionName, String[] functionArgs,String policyPath) throws InvalidArgumentException, ChaincodeEndorsementPolicyParseException, IOException, ProposalException, IOException
	{
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO, "Instantiate proposal request " + chaincodeName + " on channel " + channel.getName() + " with Fabric client " + fabClient.getInstance().getUserContext().getMspId() + " " + fabClient.getInstance().getUserContext().getName());
		
		InstantiateProposalRequest instantiateProposalRequest=fabClient.getInstance().newInstantiationProposalRequest();
		instantiateProposalRequest.setProposalWaitTime(180000);
		ChaincodeID.Builder chaincodeIDBuilder=ChaincodeID.newBuilder().setName(chaincodeName).setVersion(chaincodeVersion).setPath(chaincodePath);
		ChaincodeID ccid=chaincodeIDBuilder.build();
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO, "Instantiating Chaincode ID " + chaincodeName + " on channel " + channel.getName());
		instantiateProposalRequest.setChaincodeID(ccid);
		if(language.equals(Type.GO_LANG.toString()))
			instantiateProposalRequest.setChaincodeLanguage(Type.GO_LANG);
		else
			instantiateProposalRequest.setChaincodeLanguage(Type.JAVA);
		
		instantiateProposalRequest.setFcn(functionName);
		instantiateProposalRequest.setArgs(functionArgs);
		
		Map<String, byte[]> tm=new HashMap<>();
		tm.put("HyperledgerFabric", "InstantiateProposalRequest".getBytes(UTF_8));
		tm.put("method", "InstantiateProposalRequest".getBytes(UTF_8));
		instantiateProposalRequest.setTransientMap(tm);
		
		if(policyPath!=null)
		{
			ChaincodeEndorsementPolicy cep=new ChaincodeEndorsementPolicy();
			cep.fromYamlFile(new File(policyPath));
			instantiateProposalRequest.setChaincodeEndorsementPolicy(cep);
		}
		
		Collection<ProposalResponse> responses = channel.sendInstantiationProposal(instantiateProposalRequest);
		CompletableFuture<TransactionEvent> cf = channel.sendTransaction(responses);
		
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,	"Chaincode " + chaincodeName + " on channel " + channel.getName() + " instantiation " + cf);
        
		return responses;
		
	}
	
	//sending transaction proposal
	public Collection<ProposalResponse> sendTransactionProposal(TransactionProposalRequest request)
			throws ProposalException, InvalidArgumentException 
	{
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,
				"Sending transaction proposal on channel " + channel.getName());

		Collection<ProposalResponse> response = channel.sendTransactionProposal(request, channel.getPeers());
		for (ProposalResponse pres : response) {
			String stringResponse = new String(pres.getChaincodeActionResponsePayload());
			Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,
					"Transaction proposal on channel " + channel.getName() + " " + pres.getMessage() + " "
							+ pres.getStatus() + " with transaction id:" + pres.getTransactionID());
			Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,stringResponse);
		}

		CompletableFuture<TransactionEvent> cf = channel.sendTransaction(response);
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,cf.toString());

		return response;
    }
	
	//query by chaincode
	public Collection<ProposalResponse> queryByChainCode(String chaincodeName, String functionName, String[] args)
			throws InvalidArgumentException, ProposalException {
		Logger.getLogger(ChannelClient.class.getName()).log(Level.INFO,
				"Querying " + functionName + " on channel " + channel.getName());
		QueryByChaincodeRequest request = fabClient.getInstance().newQueryProposalRequest();
		ChaincodeID ccid = ChaincodeID.newBuilder().setName(chaincodeName).build();
		request.setChaincodeID(ccid);
		request.setFcn(functionName);
		if (args != null)
			request.setArgs(args);

		Collection<ProposalResponse> response = channel.queryByChaincode(request);

		return response;
}
	
	
	

}
