package cl.clcert.rp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.jitsi.bccontrib.prng.FortunaGenerator;
import org.json.JSONObject;

import cl.clcert.de.DataExtractor;

/**
 * Class that deals with the generation of random lists with weighed probability depending on an array with them.
 * It receives the priority intervals already normalised, ie: 1, 2, 3 would be the possible values for the array 
 * if there is 3 intervals.
 * @author constanzacsori
 *
 */
public class CompositeType {

	protected ArrayList<String> data;
	protected List<Integer> priority;
	protected int[][] output;
	protected String seed;
	protected int length;
	protected ArrayList<String> out;
	protected DataExtractor dataE;
	protected String scenario;
	
	/**
	 * Returns the random selection list with the given ids taken the random generation 
	 * provided by the methods in the class.
	 * @return random selection list from the list that was created with the type
	 */
	public ArrayList<String> getNewList(){
		this.out = new ArrayList<String>();
		for (int i=0; i<this.output[0].length; i++) {
			ArrayList<String> aux = getPriorityList(this.data, this.output[0][i]);
			ArrayList<Integer> aux_seq = getSequence(aux.size(), this.output[1][i]);
			out.addAll(getNewListP(aux, aux_seq));
		}
		
		return out;
	}
	
	/**
	 * Function that matches the randomly selected indexes to the items on the original list.
	 * @param old_list original list to make selection on
	 * @param sequence randomly created index list
	 * @return matched list from the original (old_list)
	 */
	protected ArrayList<String> getNewListP(ArrayList<String> old_list, ArrayList<Integer> sequence){
		ArrayList<String> newList = new ArrayList<String>();
		for (Integer i: sequence) {
			newList.add(old_list.get(i));
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
	public ArrayList<Integer> getSequence(int max_length, int desiredOutput) {
		if (max_length == 0) {
			return new ArrayList<Integer>();
		}
		FortunaGenerator f = new FortunaGenerator(this.seed.getBytes());
		byte[] out = new byte[1];
		ArrayList<Integer> randomList = new ArrayList<Integer>();
		while (randomList.size() < desiredOutput) {
			f.nextBytes(out);
			int number = out[0];
			if (randomList.indexOf(number) == -1 && number >= 0 && number<max_length) {
				randomList.add(number);
			}	
		}
		return randomList;
	}
	
	/**
	 * Takes the full input list to be chosen from and a desired priority from the input configuration
	 * and returns a new list containing all the values of the given priority.
	 * @param d list containing all the priorities
	 * @param p priority to be extracted
	 * @return new list only with the extracted priority
	 */
	public ArrayList<String> getPriorityList(ArrayList<String> d, int p) {
		ArrayList<String> priorityList = new ArrayList<String>();
		for (int i=0; i<this.data.size(); i++) {
			if (this.priority.get(i) == p) {
				priorityList.add(this.data.get(i));
			}
		}
		return priorityList;
	}
	
	/**
	 * Generates a string that contains a json with the needed data for verification.
	 * 
	 * @return bundle in json format
	 */
	public String getBundle() {
		String message;
		JSONObject json = new JSONObject();

		json.put("seed", this.dataE.getSeed());
		json.put("timestamp", this.dataE.getTimestamp());
		json.put("selectionType", this.getClass().getSimpleName());
//		We're always returning absolute quantities, in typeD we return the 
//		calculated quantities from the percentages.
		json.put("output-configuration", Arrays.deepToString(this.output));
		json.put("scenario", this.scenario);
		message = json.toString();
		
		return message;
	}
	
	/**
	 * Gives public access to the timestamp used to generate the random list.
	 * @return a String that has the timestamp used for the generation.
	 */
	public String getTimestamp() {
		return this.dataE.getTimestamp()+"";
	}
	
	
}
