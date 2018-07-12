package cl.clcert.rp;

import java.util.ArrayList;
import cl.clcert.de.DataExtractor;

/**
 * Extends from CompositeType. Class that creates a typeC random selection, where there is weight and the desired output is given by a 
 * configuration of priorities and percentual values associated to each priorityto be extrated.
 * 
 *  
 * @author constanzacsori
 *
 */
public class TypeD extends CompositeType{
	
	/**
	 * Main constructor of TypeD, sets the correct parameters to be used by the parent class to return a randomly
	 * created ArrayList of Strings. Throws an exception in case that the dimensions of the two arraylists don't match 
	 * or in the case of illegal limit to the desired output. 
	 * It converts the percentual values to absolutes so the parent class only uses absolutes.
	 * <p>
	 * @param scenario chosen scenario for the DataExtractor
	 * @param d an ArrayList of type String that contains the list we want to extract content from. It is composed of two 
	 * @param p an ArrayList of Integer type that contains the priority values maped one-to-one to the @d parameter.
	 * @param o a two dimensional Java array of type int. First dimension says the priority number, second dimension specifies the percentage of that priority to extract.
	 * @throws InterruptedException 
	 * 
	 */
	public TypeD(String scenario, ArrayList<String> d, ArrayList<Integer> p, int[][] o) throws InterruptedException {
		this.data = d;
		this.priority = p;
		this.scenario = scenario;
		this.length = this.data.size();
		
		if (this.data == null || this.priority == null) {
			throw new IllegalArgumentException("A given list is null");
		}
		if (this.data.size() != this.priority.size()) {
			throw new IllegalArgumentException("The dimensions of the lists don't match");
		}
		int total = 0;
		for (int i=0 ; i<o[1].length ; i++) {
			total += o[1][i];
		}
		if (total < 0 || total > 100) {
			throw new IllegalArgumentException("Values out of range for output requested.");
		}
		
		this.output = this.newNumberArray(o, d.size());
		
		this.dataE = new DataExtractor(scenario);
		this.seed = this.dataE.getSeed();
		
	}
	
	/**
	 * Verification constructor of TypeD, sets the correct parameters to be used by the parent class to return a randomly
	 * created ArrayList of Strings. Throws an exception in case that the dimensions of the two arraylists don't match 
	 * or in the case of illegal limit to the desired output. Uses a given DataExtractor for the seed.
	 * It converts the percentual values to absolutes so the parent class only uses absolutes.
	 * <p>
	 * @param d an ArrayList of type String that contains the list we want to extract content from. It is composed of two 
	 * @param p an ArrayList of Integer type that contains the priority values maped one-to-one to the @d parameter
	 * @param o a two dimensional Java array of type int. First dimension says the priority number, second dimension specifies the percentage of that priority to extract
	 * @param de given DataExtractor to be used for the seed
	 * @throws InterruptedException 
	 * 
	 */
	public TypeD(ArrayList<String> d, ArrayList<Integer> p, int[][] o, DataExtractor de) throws InterruptedException {
		this.data = d;
		this.priority = p;
		this.scenario = de.type();
		this.length = this.data.size();
		
		if (this.data == null || this.priority == null) {
			throw new IllegalArgumentException("A given list is null");
		}
		if (this.data.size() != this.priority.size()) {
			throw new IllegalArgumentException("The dimensions of the lists don't match");
		}
		int total = 0;
		for (int i=0 ; i<o[1].length ; i++) {
			total += o[1][i];
		}
		if (total < 0 || total > 100) {
			throw new IllegalArgumentException("Values out of range for output requested.");
		}
		
		this.output = this.newNumberArray(o, d.size());
		
		this.dataE = de;
		this.seed = this.dataE.getSeed();
	}
	
	/**
	 * Class function that receives the array given by the user containing the percentages of each priority to be used, and transforms it to
	 * an array of the same dimensions that has the absolute numbers (integers) of data to be extracted. This is done to maintain the same
	 * implementation of the composite class and use the extended methods from said class.
	 * 
	 * @param init initial double dimension array that holds the percentage of each priority to be extracted.
	 * @param n integer number that represents the total number of elements in the main list of data to extract from.
	 * @return A double dimension array with absolute numbers for each priority.
	 */
	private int[][] newNumberArray(int[][] init, int n){
		int [][] out = init;
		for (int i = 0; i<init[1].length; i++) {
			out[1][i] = (init[1][i]*n)/100; 
		}
		return out;
	}
	
}
