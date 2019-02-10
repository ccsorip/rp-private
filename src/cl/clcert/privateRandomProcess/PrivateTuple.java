package cl.clcert.privateRandomProcess;


import java.io.Serializable;

import com.ing.blockchain.zk.dto.Commitment;
import com.ing.blockchain.zk.dto.RangeProof;

 
public class PrivateTuple implements Serializable{

	private Commitment comm;
	private RangeProof proof;
	private String upper;
	private String lower;
	
	public PrivateTuple(Commitment comm, RangeProof proof, String u, String l) {
		this.comm = comm;
		this.proof = proof;
		this.upper = u;
		this.lower = l;
	}
	
	public Commitment getComm() {
		return comm;
	}
	
	public RangeProof getProof() {
		return proof;
	}

	public void setProof(RangeProof proof) {
		this.proof = proof;
	}

	public void setComm(Commitment comm) {
		this.comm = comm;
	}

	public String getUpper() {
		return upper;
	}

	public void setUpper(String upper) {
		this.upper = upper;
	}

	public String getLower() {
		return lower;
	}

	public void setLower(String lower) {
		this.lower = lower;
	}

		
}
