package cl.clcert.rp;

import java.util.ArrayList;
import java.util.List;
import org.jitsi.bccontrib.prng.FortunaGenerator;
import org.json.JSONObject;

import cl.clcert.de.DataExtractor;

/**
 * Parent class that deals with the generation of random lists with equal probability in all being chosen.
 * @author constanzacsori
 *
 */
public class SimpleType {

	protected List<String> baseList;
	protected int length;
	protected int desiredOutput;
	protected String seed;
	protected DataExtractor data;
	protected String scenario;
	
	/**
	 * Returns the random selection list with the given ids taken the random generation 
	 * provided by getSequence.
	 * @return random selection list from the list that was created with the type
	 */
	public ArrayList<String> getNewList() {
		ArrayList<String> newList = new ArrayList<String>();
		for (Integer i: this.getSequence()) {
			newList.add(this.baseList.get(i));
		}
		return newList;
	}
	
	/**
	 * Function that given the seed, uses the Fortuna PRNG to generate a stream of random numbers,
	 * checks if the numbers are apt to be used as indexes for the base list and creates a list with
	 * the random numbers.
	 * 
	 * @return integer array containing all different indexes. As long as the desired output is.
	 */
	protected ArrayList<Integer> getSequence() {
		FortunaGenerator f = new FortunaGenerator(seed.getBytes());
		byte[] out = new byte[1];
		ArrayList<Integer> randomList = new ArrayList<Integer>();
		while (randomList.size() < this.desiredOutput) {
			f.nextBytes(out);
			int number = out[0];
			if (randomList.indexOf(number) == -1 && number >= 0 && number<this.length) {
				randomList.add(number);
			}
		}
		return randomList;
	}
	
	/**
	 * Generates a string that contains a json with the needed data for verification.
	 * 
	 * @return bundle in json format
	 */
	public String getBundle() {
		String message;
		
		JSONObject json = new JSONObject();
		
		json.put("seed", this.data.getSeed());
		json.put("timestamp", this.data.getTimestamp());
		json.put("selectionType", this.getClass().getSimpleName());
//		We're always returning absolute quantities, in typeB we return the 
//		calculated quantities from the percentages.
		json.put("desiredOutput", this.desiredOutput);
		json.put("scenario", this.scenario);
		message = json.toString();
		
		return message;
	}
	
	/**
	 * Gives public access to the timestamp used to generate the random list.
	 * @return a String that has the timestamp used for the generation.
	 */
	public String getTimestamp() {
		return this.data.getTimestamp()+"";
	}
}
