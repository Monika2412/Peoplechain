package org.app.user;

import java.io.Serializable;
import java.security.PrivateKey;

import org.hyperledger.fabric.sdk.Enrollment;

public class CAEnrollment implements Enrollment,Serializable{
	public static final long serialVersionId=550416591376968096L;
	PrivateKey key;
	String cert;
	
	public CAEnrollment(PrivateKey pk,String pem)
	{
		this.key=pk;
		this.cert=pem;
	}

	@Override
	public PrivateKey getKey() {
		// TODO Auto-generated method stub
		return key;
	}

	@Override
	public String getCert() {
		// TODO Auto-generated method stub
		return cert;
	}

}
