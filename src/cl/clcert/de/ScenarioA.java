package cl.clcert.de;

import java.io.FileNotFoundException;
import java.io.IOException;
import com.google.gson.JsonObject;

/**
 * Class that extends from the main Data Extractor, deals with the scenario in which we ask the seed value from the
 * UdeChile beacon.
 * If the value is not available, we wait 10 minutes until the beacon is back or we return an error.
 * The API from UdeChile returns a json file.
 *
 * @author constanzacsori
 *
 */
public class ScenarioA extends DataExtractor{
	private String seed;
	private long timestamp;
	private String beacon_used;
	
	/**
	 * Main constructor for this scenario. Uses the current timestamp to extract data from the beacon.
	 * @throws InterruptedException
	 */
	public ScenarioA() throws InterruptedException  {
		this.timestamp = System.currentTimeMillis() / 1000L;
		try {
			this.getBeaconData(this.timestamp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Verification constructor, when it is needed to create a beacon request with a set timestamp.
	 * @param timestamp unix time timestamp for beacon request
	 * @throws InterruptedException
	 */
	public ScenarioA(long timestamp) throws InterruptedException  {
		this.timestamp = timestamp;
		try {
			this.getBeaconData(timestamp);;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Method that makes a connection to the URL from the UdeChile beacon using the timestamp given from the use of itself,
	 * it manipulates the json file that it receives and it saves 160 bits (10 words) as a seed value.
	 * 
	 * @throws IOException
	 * @throws InterruptedException 
	 */
	private void getBeaconData(long timestamp) throws IOException, InterruptedException {
		int flag = 5000;
		while (flag > 0 && flag < 360000) {			
		    try {
			    JsonObject rootobj = new HttpsClient().getContent(timestamp+"");
			    if (rootobj == null) throw new FileNotFoundException(); 
			    String complete_seed = rootobj.get("outputValue").getAsString();
			    
//				We are using 10 words, which are 160 bits, given that we're using Fortuna as a PRNG. (0-39).
				this.seed = complete_seed.substring(0, 39);
				this.beacon_used = "udechile";
			    flag = -1;
		    }
		    catch  (FileNotFoundException e) {
		    	Thread.sleep(flag);
		    	flag+=5000;
		    }
		}
		
		if (flag >=36000) {
			throw new NullPointerException("Timeout, over 10 minutes wait for udechile beacon");
		}
		    
	}
	
	/**
	 * Method that returns the value of the seed retrieved from the beacon.
	 * @return seed value in a String.
	 */
	public String getSeed(){
		return this.seed;
	}
	
	/**
	 * Method that makes public the value of the timestamp used to retrieve the data.
	 * @return A long value that represents the value that we asked from the beacon in the next setting.
	 */
	public long getTimestamp() {
		return this.timestamp;
	}
	
	/**
	 * Returns the string with the beacons that were actually used for the seed.
	 * @return String with the beacons used.
	 */
	public String beaconUsed() {
		return this.beacon_used;
	}
	
	/**
	 * Method that exposes the scenario that is under use.
	 * @return "A" code for the first scenario.
	 */
	public String type() {
		return "A";
	}
}
