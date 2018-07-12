package cl.clcert.de;

/**
 * Class that deals with the scenario in which we combine the NIST and UdeChile beacon with an xor, checks availability on each beacon
 * and proceeds to xor or just leave the seed of the one available.
 * @author constanzacsori
 *
 */
public class ScenarioC extends DataExtractor{
	
	private String seed;
	private String seedA;
	private String seedD;
	private long timestamp;
	private String beacon_used="";
	
	/**
	 * Main constructor for this scenario. Uses the current timestamp to extract data from the beacon.
	 * @throws InterruptedException
	 */
	public ScenarioC() throws InterruptedException {
		this.timestamp = System.currentTimeMillis() / 1000L;
		this.create();
	}
	
	/**
	 * Verification constructor, when it is needed to create a beacon request with a set timestamp.
	 * @param timestamp unix time timestamp for beacon request
	 * @throws InterruptedException
	 */
	public ScenarioC(long timestamp) throws InterruptedException{
		this.timestamp = timestamp;
		this.create();
	}
	
	/**
	 * Method that internally tries to create each beacon individually and combines each requested
	 * seed via xor in hex. If one of the beacons is unreachable it only uses the one available or thwrows
	 * an exception indicating that they both failed.
	 * 
	 */
	private void create() {
		seedA = "";
		seedD = "";
		try {
			ScenarioA a = new ScenarioA(this.timestamp);
			seedA = a.getSeed();
		}
		catch (Exception e) {
			seedA = "FLAG";
			System.out.println("Failed beacon udechile");
		}
		
		
		try {
			ScenarioD d = new ScenarioD(this.timestamp);
			seedD = d.getSeed();
		}catch (Exception e) {
			seedD = "FLAG";
			System.out.println("Failed beacon NIST");
		}
		
		
		if (!seedA.equals("FLAG") && !seedD.equals("FLAG")) {
			this.seed = xorHex(seedA, seedD);
			this.beacon_used = "udechile-nist";
		}else if (seedA.equals("FLAG") && !seedD.equals("FLAG")) {
			this.seed = seedD;
			this.beacon_used = "nist";
		}else if (!seedA.equals("FLAG") && seedD.equals("FLAG")) {
			this.seed = seedA;
			this.beacon_used = "udechile";
		}else {
			throw new NullPointerException("Both beacons failed");
		}
	}
	
	/**
	 * Function that combines in xor two hexadecimal values.
	 * @param a first hexadecimal value
	 * @param b seconf hexadecimal value
	 * @return xor of a and b
	 */
	private String xorHex(String a, String b) {
		if (a.length() != b.length()) return null;
	    char[] chars = new char[a.length()];
	    for (int i = 0; i < chars.length; i++) {
	        chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
	    }
	    return new String(chars);
	}

	/**
	 * Function that returns an integer value from a char representing a hexadecimal.
	 * @param c hexadecimal char
	 * @return int from the hexadecimal c
	 */
	private static int fromHex(char c) {
	    if (c >= '0' && c <= '9') {
	        return c - '0';
	    }
	    if (c >= 'A' && c <= 'F') {
	        return c - 'A' + 10;
	    }
	    if (c >= 'a' && c <= 'f') {
	        return c - 'a' + 10;
	    }
	    throw new IllegalArgumentException();
	}

	/**
	 * Function that returns a char representing the integer value as a hexadecimal
	 * @param nybble hexadecimal int
	 * @return char from the hexadecimal nybble
	 */
	private char toHex(int nybble) {
	    if (nybble < 0 || nybble > 15) {
	        throw new IllegalArgumentException();
	    }
	    return "0123456789ABCDEF".charAt(nybble);
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
	 * @return "C" code for the first scenario.
	 */
	public String type() {
		return "C";
	}
}
