package cl.clcert.de;

import com.ing.blockchain.zk.SecretOrderGroupGenerator;
import com.ing.blockchain.zk.dto.SecretOrderGroup;


public class DataPrep {

	public DataPrep() {
		new SecretOrderGroupGenerator(512).generate();
	}
	
}
