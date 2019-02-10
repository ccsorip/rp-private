package cl.clcert.randomProcess;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;

import org.json.JSONObject;

import com.ing.blockchain.zk.HPAKErangeProof;
import com.ing.blockchain.zk.SecretOrderGroupGenerator;
import com.ing.blockchain.zk.TTPGenerator;
import com.ing.blockchain.zk.dto.*;

import cl.clcert.privateRandomProcess.PrivateTuple;

public class PrivateType {
	
	protected ArrayList<String> ids;
	public ArrayList<PrivateTuple> pTuple;
	protected String upper1;
	protected String lower1;
	protected String upper2;
	protected String lower2;
	protected String upper3;
	protected String lower3;
	private ClosedRange r1;
	private ClosedRange r2;
	private ClosedRange r3;
	private String N;
	private String G;
	private String H;
	private String openingFile;
	
	

/**
 * Set for 3 closed intervals.
 * 
 * @param u1
 * @param u2
 * @param u3
 * @param l1
 * @param l2
 * @param l3
 * @param priorities
 * @param d
 */
	public PrivateType(String u1, String u2, String u3, String l1, String l2, String l3, ArrayList<Integer> priorities,
			ArrayList<String> ids) {
		this.upper1 = u1;
		this.upper2 = u2;
		this.upper3 = u3;
		this.lower1 = l1;
		this.lower2 = l2;
		this.lower3 = l3;
		this.ids = ids;
		r1 = ClosedRange.of(lower1, upper1);
		r2 = ClosedRange.of(lower2, upper2);
		r3 = ClosedRange.of(lower3, upper3);
		
		pTuple = new ArrayList<PrivateTuple>();
		
		createProofs(priorities);
		
	}
	
	public void createProofs(ArrayList<Integer> p) {
//		For every priority we create a commitment and a zero knowledge closed range proof of its belonging.
		SecretOrderGroup group = new SecretOrderGroupGenerator(512).generate();
		this.N = group.getN().toString();
		this.G = group.getG().toString();
		this.H = group.getH().toString();

		
		JSONObject pFile = new JSONObject();
		for (Integer i: p) {
			
			TTPMessage ttpMessage = TTPGenerator.generateTTPMessage(BigInteger.valueOf(i.intValue()), group);
			
			RangeProof proof = null;
			PrivateTuple tuple = null;
			if (r1.contains(ttpMessage.getX())) {
				proof = HPAKErangeProof.calculateRangeProof(ttpMessage, r1);
				tuple = new PrivateTuple(ttpMessage.getCommitment(), proof, upper1, lower1);
			}else if (r2.contains(ttpMessage.getX())) {
				proof = HPAKErangeProof.calculateRangeProof(ttpMessage, r2);
				tuple = new PrivateTuple(ttpMessage.getCommitment(), proof, upper2, lower2);
			}else if (r3.contains(ttpMessage.getX())) {
				proof = HPAKErangeProof.calculateRangeProof(ttpMessage, r3);
				tuple = new PrivateTuple(ttpMessage.getCommitment(), proof, upper3, lower3);
			}
			
			
			if (proof != null) {
				// We add the tuple of comm, proof and limits to a list
				pTuple.add(tuple);
				// We add the private data and the private key to keep for the trusted entity, in case they have to open the commitments at one point.
				JSONObject pair = new JSONObject();
				pair.put("R", ttpMessage.getY());
				pair.put("ID", ttpMessage.getX());
				pFile.put(p.indexOf(i)+"", pair);
			}
			
		}
		this.openingFile = pFile.toString();
		
	}
	


	public String bundleJSON() {
		
		JSONObject jsonTuplesRanges = new JSONObject();
	
		
		JSONObject tupleJson = new JSONObject();
		for (PrivateTuple elem: pTuple) {
			JSONObject numberedTuples = new JSONObject();
			String serializedComm = "";
			String serializedProof = "";
			 try {
			     ByteArrayOutputStream bo = new ByteArrayOutputStream();
			     ByteArrayOutputStream bo2 = new ByteArrayOutputStream();
			     ObjectOutputStream so = new ObjectOutputStream(bo);
			     ObjectOutputStream so2 = new ObjectOutputStream(bo2);
			     so.writeObject(elem.getComm());
			     so2.writeObject(elem.getProof());
			     so.flush();
			     so2.flush();
			     serializedComm = Base64.getEncoder().encodeToString(bo.toByteArray()); 
			     serializedProof =  Base64.getEncoder().encodeToString(bo2.toByteArray()); 
			 } catch (Exception e) {
			     System.out.println(e);
			 }
			 
			 numberedTuples.put("serializedComm", serializedComm);
			 numberedTuples.put("serializedProof", serializedProof);
			 numberedTuples.put("upper", elem.getUpper());
			 numberedTuples.put("lower", elem.getLower());
			 
			 tupleJson.put(pTuple.indexOf(elem)+"", numberedTuples);
			 
		}
		
		jsonTuplesRanges.put("tuples", tupleJson);
//		jsonTuplesRanges.put("N", this.N);
//		jsonTuplesRanges.put("G", this.G);
//		jsonTuplesRanges.put("H", this.H);
		
		return jsonTuplesRanges.toString();
	}

	public void toJsonFile(String path, String filename, String json) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(path+filename+".json"));
			writer.write(json.toString());
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getPrivateOpeningFile() {
		return this.openingFile;
	}
	
	public ClosedRange getR1() {
		return r1;
	}

	public void setR1(ClosedRange r1) {
		this.r1 = r1;
	}

	public ClosedRange getR2() {
		return r2;
	}

	public void setR2(ClosedRange r2) {
		this.r2 = r2;
	}

	public ClosedRange getR3() {
		return r3;
	}

	public void setR3(ClosedRange r3) {
		this.r3 = r3;
	}

	public ArrayList<PrivateTuple> getpTuple() {
		return pTuple;
	}

	public void setpTuple(ArrayList<PrivateTuple> pTuple) {
		this.pTuple = pTuple;
	}	
}
