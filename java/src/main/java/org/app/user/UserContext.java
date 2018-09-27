package org.app.user;

import java.io.Serializable;
import java.util.Set;

import org.hyperledger.fabric.sdk.Enrollment;
import org.hyperledger.fabric.sdk.User;

public class UserContext implements User,Serializable{
	
	private static final long serialVersionId=1L;
    protected String name;
    protected String affiliation;
    protected String account;
    protected String mspId;
    protected Enrollment enrollment;
    protected Set<String> roles;

    public void setName(String name)
    {
        this.name=name;
    }

    public String getName()
    {
        return name;
    }

    public void setAffiliation(String affiliation)
    {
        this.affiliation=affiliation;
    }

    public String getAffiliation()
    {
        return affiliation;
    }

    public void setAccount(String account)
    {
        this.account=account;
    }

    public String getAccount()
    {
        return account;
    }

    public void setMspId(String mspId)
    {
        this.mspId=mspId;
    }

    public String getMspId()
    {
        return mspId;
    }

    public void setEnrollment(Enrollment enrollment)
    {
        this.enrollment=enrollment;
    }

    public Enrollment getEnrollment()
    {
        return enrollment;
    }

    public void setRoles(Set<String> roles)
    {
        this.roles=roles;
    }

    public Set<String> getRoles()
    {
        return roles;
    }


}
