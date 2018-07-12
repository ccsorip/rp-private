package cl.clcert.rp;

import java.util.ArrayList;

import cl.clcert.de.DataExtractor;

/**
 * Class that creates a typeB random selection, where there is no weight and the desired output is
 * given in a percentual value.
 *  
 * @author constanzacsori
 *
 */
public class TypeB extends SimpleType{

	/**
	 * Main constructor for TypeB. For generation purposes it uses the scenario given as a parameter to create
	 * a DataExtractor and with that obtain the seed used for the random selection in the parent class.
	 * Given the percentual value, it saves it as an absolute value so the parent class only uses that to make
	 * the selection.
	 * 
	 * @param scenario chosen scenario for the DataExtractor
	 * @param lista list to do the random selection on
	 * @param n percentual value of desired outputs
	 * @throws InterruptedException
	 */
	public TypeB(String scenario, ArrayList<String> lista, int n) throws InterruptedException {
		this.baseList = lista;
		this.scenario = scenario;
		if (this.baseList == null) {
			throw new IllegalArgumentException("List is null");
		}
		if (this.baseList.size() < n || n < 0) {
			throw new IllegalArgumentException("n out of range: " + n + " expected range 0 <= " + this.baseList.size());
		}
		this.length = this.baseList.size();
		this.data = new DataExtractor(scenario);
		this.seed = this.data.getSeed();
		this.desiredOutput = this.length*n/100;
	}
	
	/**
	 * Verification constructor for TypeB. Same function as main constructor, but it receives a DataExtractor
	 * that will be used to obtain the seed.
	 * 
	 * @param lista list to do the random selection on
	 * @param n percentual value of desired outputs
	 * @param de already generated DataExtractor for seed purposes
	 * @throws InterruptedException
	 */
	public TypeB(ArrayList<String> lista, int n, DataExtractor de) throws InterruptedException {
		this.baseList = lista;
		this.scenario = de.type();
		if (this.baseList == null) {
			throw new IllegalArgumentException("List is null");
		}
		if (this.baseList.size() < n || n < 0) {
			throw new IllegalArgumentException("n out of range: " + n + " expected range 0 <= " + this.baseList.size());
		}
		this.length = this.baseList.size();
		this.data = de;
		this.seed = this.data.getSeed();
		this.desiredOutput = this.length*n/100;
	}
}