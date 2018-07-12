package cl.clcert.de;

/**
 * Parent class that wraps each possible scenario that deals with getting information from the beacon(s). 
 * @author constanzacsori
 *
 */
public class DataExtractor {
	
	DataExtractor a;
	
	public DataExtractor() {
	}
	
	/**
	 * Constructor for the parent class, creates a DataExtractor wrapper that is to be used for the outside
	 * client.
	 * 
	 * @param type specifies the sceneario that is to be used.
	 * @throws InterruptedException
	 */
	public DataExtractor (String type) throws InterruptedException {
		if (type.equals("A")) {
			a = new ScenarioA();
		}else if (type.equals("B")) {
			a= new ScenarioB();
		}else if (type.equals("C")) {
			a = new ScenarioC();
		}else {
			a = new ScenarioD();
		}
	}
	

	/**
	 * Constructor for the parent class, needed for verification or when there is already a DataExtractor
	 * and the seed is needed.
	 * 
	 * @param type specifies the scenario that is to be used.
	 * @param timestamp specifies the timestamp to be used for creation.
	 * @throws InterruptedException
	 */
	public DataExtractor (String type, long timestamp) throws InterruptedException{
		if (type.equals("A")) {
			a = new ScenarioA(timestamp);
		}else if (type.equals("B")) {
			a= new ScenarioB(timestamp);
		}else if (type.equals("C")) {
			a = new ScenarioC(timestamp);
		}else {
			a = new ScenarioD(timestamp);
		}
	}
	
	/**
	 * Public method that returns the seed from this data extractor.
	 * @return 160 bit seed in string format.
	 */
	public String getSeed() {
		return a.getSeed();
	}
	
	/**
	 * Public method that returns the timestamp used for this data extractor.
	 * @return timestamp in unix time.
	 */
	public long getTimestamp() {
		return a.getTimestamp();
	}
	
	/**
	 * Public method that returns the beacon(s) used.
	 * @return beacon used in string format.
	 */
	public String beaconUsed() {
		return a.beaconUsed();
	}
	
	/**
	 * Public method that returns the scenario that was used in this data extractor.
	 * @return scenario letter that was used to create the data extractor.
	 */
	public String type() {
		return a.type();
	}
	
	
}
