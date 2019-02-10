package cl.clcert.randomProcess;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ing.blockchain.zk.HPAKErangeProof;
import com.ing.blockchain.zk.dto.ClosedRange;
import com.ing.blockchain.zk.dto.Commitment;
import com.ing.blockchain.zk.dto.RangeProof;

import cl.clcert.privateRandomProcess.PrivateTuple;

public class PublicVerification {

	protected ArrayList<PrivateTuple> pTuple;
	
	public PublicVerification(String jsonFile) throws ClassNotFoundException, JSONException, IOException {
		pTuple = new ArrayList<PrivateTuple>();
		JSONObject obj = new JSONObject(jsonFile);
//		String N = obj.getString("N");
//		String G = obj.getString("G");
//		String H = obj.getString("H");
		JSONObject arr = obj.getJSONObject("tuples");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject a = (JSONObject) arr.get(i+"");
		    
		    Commitment comm = (Commitment) fromString(a.get("serializedComm").toString()); //null;
		    RangeProof proof = (RangeProof) fromString(a.get("serializedProof").toString());
		    String u = a.getString("upper");
		    String l = a.getString("lower");		    
		    
		    PrivateTuple newTuple = new PrivateTuple(comm, proof, u, l);
		    pTuple.add(newTuple);
		    
		}
	}
	
	   private static Object fromString( String s ) throws IOException, ClassNotFoundException {
		   byte [] data = Base64.getDecoder().decode( s );
		   ObjectInputStream ois = new ObjectInputStream( 
				   new ByteArrayInputStream(  data ) );
		   Object o  = ois.readObject();
		   ois.close();
		   return o;
	   }
	
	public boolean validateSingleProof(String upper, String lower, Commitment comm, RangeProof rproof) {
		ClosedRange range = ClosedRange.of(lower, upper);
		try {
			HPAKErangeProof.validateRangeProof(rproof, comm, range);
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
		return true;

	}
	
	public boolean validateTupleProof(PrivateTuple pt) {
		return validateSingleProof(pt.getUpper(), pt.getLower(), pt.getComm(), pt.getProof());
	}
	
	public boolean validateTupleListProof(ArrayList<PrivateTuple> list) {
		boolean flag = true;
		for (PrivateTuple el: list) {
			flag = flag && validateTupleProof(el);
		}
		return flag;
	}
	
}
