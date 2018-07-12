package cl.clcert.de;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Class that deals with the scenario in which we ask for the data ONLY from the Nist Beacon.
 * @author constanzacsori
 *
 */
public class ScenarioD extends DataExtractor{

		private String seed;
		private long timestamp;
		private String beacon_used;
		private String beaconData;
		
		/**
		 * Main constructor for this scenario. Uses the current timestamp to extract data from the beacon.
		 * @throws InterruptedException
		 */
		public ScenarioD() throws InterruptedException  {
			this.timestamp = System.currentTimeMillis() / 1000L;
			try {
				this.getBeaconData();
				this.getSeedValue();
			} catch (IOException | SAXException | ParserConfigurationException e) {
				e.printStackTrace();
			}

		}
		
		/**
		 * Verification constructor, when it is needed to create a beacon request with a set timestamp.
		 * @param timestamp unix time timestamp for beacon request
		 * @throws InterruptedException
		 */
		public ScenarioD(long timestamp) throws InterruptedException {
			this.timestamp = timestamp;
			try {
				this.getBeaconData();
				this.getSeedValue();
			} catch (IOException | SAXException | ParserConfigurationException e) {
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
		private void getBeaconData() throws IOException, InterruptedException {
			String sURL = "	https://beacon.nist.gov/rest/record/" + this.timestamp;
			int flag = 5000;
			
			while (flag > 0 && flag < 360000) {
			    // Connect to the URL using java's native library
			    URL url = new URL(sURL);
			    HttpURLConnection request = (HttpURLConnection) url.openConnection();
			    request.connect();
			    try {
			    		BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) request.getContent()));
			    		this.beaconData = br.readLine();
			    		flag = -1;
			    }
			    catch  (FileNotFoundException e) {
			    		Thread.sleep(flag);
			    		flag+=5000;
			    }
			}
			
			if (flag >=36000) {
				throw new NullPointerException("Timeout, over 10 minutes wait for NIST beacon");
			}
		}
		
		/**
		 * Function that searches for a value in an xml element given an xml tag name.
		 * @param tagName Name of the tag that is searched.
		 * @param element Element that contains the xml DOM.
		 * @return Returns the value associated to tagName or null in the case it does not exist.
		 */
		private String getString(String tagName, Element element) {
	        NodeList list = element.getElementsByTagName(tagName);
	        if (list != null && list.getLength() > 0) {
	            NodeList subList = list.item(0).getChildNodes();

	            if (subList != null && subList.getLength() > 0) {
	                return subList.item(0).getNodeValue();
	            }
	        }

	        return null;
	    }
		
		/**
		 * Creates a representation of the xml DOM and returns the seed value in the xml file taken from the Nist beacon, the seed is 160
		 * bits (10 words) long according to the use of Yarrow as a PRNG.
		 * 
		 * @throws SAXException
		 * @throws IOException
		 * @throws ParserConfigurationException
		 */
		private void getSeedValue() throws SAXException, IOException, ParserConfigurationException {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new InputSource(new StringReader(this.beaconData)));
			Element rootElement = document.getDocumentElement();
			
			String complete_seed = getString("seedValue", rootElement);
//			We are using 10 words, which are 160 bits, given that we're using Fortuna as a PRNG. (0-39).
			this.seed = complete_seed.substring(0, 39);
			this.beacon_used = "nist";
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
		 * @return "D" code for the first scenario.
		 */
		public String type() {
			return "D";
		}
}
