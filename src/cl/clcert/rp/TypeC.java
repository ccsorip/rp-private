package cl.clcert.rp;

import java.util.ArrayList;

import cl.clcert.de.DataExtractor;

/**
 * Extends from CompositeType. Class that creates a typeC random selection, where there is weight and the desired output is given by a 
 * configuration of priorities and absolute values associated to each priorityto be extrated.
 * 
 *  
 * @author constanzacsori
 *
 */
public class TypeC extends CompositeType {
	
	/**
	 * Main constructor of TypeC, sets the correct parameters to be used by the parent class to return a randomly
	 * created ArrayList of Strings. Throws an exception in case that the dimensions of the two arraylists don't match 
	 * or in the case of illegal limit to the desired output. 
	 * <p>
	 * @param scenario chosen scenario for the DataExtractor
	 * @param d an ArrayList of type String that contains the list we want to extract content from. It is composed of two 
	 * @param p an ArrayList of Integer type that contains the priority values maped one-to-one to the @d parameter.
	 * @param o a two dimensional Java array of type int. First dimension says the priority number, second dimension specifies the absolute value of that priority to extract.
	 * @throws InterruptedException 
	 * 
	 */
	public TypeC(String scenario, ArrayList<String> d, ArrayList<Integer> p, int[][] o) throws InterruptedException {
		this.data = d;
		this.priority = p;
		this.output = o;
		this.scenario = scenario;
		this.length = this.data.size();
		
		if (this.data == null || this.priority == null) {
			throw new IllegalArgumentException("A given list is null");
		}
		if (this.data.size() != this.priority.size()) {
			throw new IllegalArgumentException("The dimensions of the lists don't match");
		}
		int total = 0;
		for (int i=0 ; i<this.output[1].length ; i++) {
			total += this.output[1][i];
		}
		if (total < 0 || total > this.data.size()) {
			throw new IllegalArgumentException("Values out of range for output requested.");
		}
		
		this.dataE = new DataExtractor(scenario);
		this.seed = this.dataE.getSeed();
	}
	
	/**
	 * Verification constructor of TypeC, sets the correct parameters to be used by the parent class to return a randomly
	 * created ArrayList of Strings. Throws an exception in case that the dimensions of the two arraylists don't match 
	 * or in the case of illegal limit to the desired output. 
	 * Uses a given DataExtractor for the seed.
	 * <p>
	 * 
	 * @param d an ArrayList of type String that contains the list we want to extract content from. It is composed of two 
	 * @param p an ArrayList of Integer type that contains the priority values maped one-to-one to the @d parameter.
	 * @param o a two dimensional Java array of type int. First dimension says the priority number, second dimension specifies the absolute value of that priority to extract.
	 * @param de given DataExtractor to be used for the seed
	 * @throws InterruptedException 
	 * 
	 */
	public TypeC(ArrayList<String> d, ArrayList<Integer> p, int[][] o, DataExtractor de) throws InterruptedException {
		this.data = d;
		this.priority = p;
		this.output = o;
		this.scenario = de.type();
		this.length = this.data.size();
		
		if (this.data == null || this.priority == null) {
			throw new IllegalArgumentException("A given list is null");
		}
		if (this.data.size() != this.priority.size()) {
			throw new IllegalArgumentException("The dimensions of the lists don't match");
		}
		int total = 0;
		for (int i=0 ; i<this.output[1].length ; i++) {
			total += this.output[1][i];
		}
		if (total < 0 || total > this.data.size()) {
			throw new IllegalArgumentException("Values out of range for output requested.");
		}
		this.dataE = de;
		this.seed = this.dataE.getSeed();
	}
	
}
