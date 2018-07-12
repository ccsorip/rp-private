package cl.clcert.vp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import cl.clcert.de.DataExtractor;
import cl.clcert.rp.*;

/**
 * Class that verifies for all types and scenarios the outputs.
 * @author constanzacsori
 *
 */
public class VerifyProcess {
	
	private DataExtractor de;
	private boolean checked;
	
	/**
	 * Constructor that will receive in the case of verification of simple types A and B. Receives the input data
	 * and within the configuration bundle there is the data needed to recreate the random selection
	 * process. After this, the comparison is ade between the given output list @outData and the newly
	 * calculated one. It saves the comparison result in the boolean value checked that can be exposed. 
	 * 
	 * @param inData original input data list
	 * @param outData original output data list to be compared with the calculated one
	 * @param conf configuration bundle in json format containing all needed information for the recreation of the random selection
	 * @throws InterruptedException 
	 */
	public VerifyProcess (ArrayList<String> inData, ArrayList<String> outData, String conf) throws InterruptedException {
		JSONObject obj = new JSONObject(conf);
		String scenario = obj.getString("scenario");
		String selType = obj.getString("selectionType");
		long timestamp = obj.getLong("timestamp");
		int desiredOutput = obj.getInt("desiredOutput");
		
		this.de = new DataExtractor(scenario, timestamp);
		SimpleType s = null;

		if (selType.equalsIgnoreCase("TypeA")) {
			s = new TypeA(inData, desiredOutput, de);
			
		}else if (selType.equalsIgnoreCase("TypeB")) {
			s = new TypeB(inData, desiredOutput, de);
		}
		ArrayList<String> outputTest = s.getNewList();
		this.checked = compareLists(outData, outputTest);
	}
	
	/**
	 * Constructor that will receive in the case of verification of simple types A and B. Receives the input data
	 * and within the configuration bundle there is the data needed to recreate the random selection
	 * process. After this, it saves wether the value to check is in the new list or not in checked. 
	 * 
	 * @param inData original input data list
	 * @param comp id to check if exists in the calculated one
	 * @param conf configuration bundle in json format containing all needed information for the recreation of the random selection
	 * @throws InterruptedException 
	 */
	public VerifyProcess (ArrayList<String> inData, String comp, String conf) throws InterruptedException {
		
		JSONObject obj = new JSONObject(conf);
		String scenario = obj.getString("scenario");
		String selType = obj.getString("selectionType");
		long timestamp = obj.getLong("timestamp");
		int desiredOutput = obj.getInt("desiredOutput");
		
		
		this.de = new DataExtractor(scenario, timestamp);
		SimpleType s = null;

		if (selType.equalsIgnoreCase("TypeA")) {
			s = new TypeA(inData, desiredOutput, de);
			
		}else if (selType.equalsIgnoreCase("TypeB")) {
			s = new TypeB(inData, desiredOutput, de);
		}
		ArrayList<String> outputTest = s.getNewList();
		this.checked = outputTest.contains(comp);
	}
	
	
	/**
	 * Constructor that will receive in the case of verification of composite types C and D. Receives 
	 * the input data list alongside the priority input list and with the configuration bundle there 
	 * is the data needed to recreate the random selection process. After this, the comparison is made 
	 * between the given output list @outData and the newly calculated one. It saves the comparison 
	 * result in the boolean value checked that can be exposed.
	 * 
	 * 
	 * @param inData original input data list
	 * @param priorities original priority input list 
	 * @param outData original output data list to be compared with the calculated one
	 * @param conf configuration bundle in json format containing all needed information for the recreation of the random selection
	 * @throws InterruptedException 
	 */
	public VerifyProcess (ArrayList<String> inData, ArrayList<Integer> priorities, ArrayList<String> outData, String conf) throws InterruptedException {
		JSONObject obj = new JSONObject(conf);
		String scenario = obj.getString("scenario");
		Long timestamp = obj.getLong("timestamp");
		String outputConf = obj.getString("output-configuration");
		
		this.de = new DataExtractor(scenario, timestamp);
		CompositeType c = new TypeC(inData, priorities, this.getIntConf(outputConf), de);
		ArrayList<String> outputTest = c.getNewList();
		this.checked = compareLists(outData, outputTest);
	}
	
	/**
	 * Constructor that will receive in the case of verification of composite types C and D. Receives 
	 * the input data list alongside the priority input list and with the configuration bundle there 
	 * is the data needed to recreate the random selection process. After this, the comparison is made 
	 * between the given output list @outData and the newly calculated one. It saves the comparison 
	 * result in the boolean value checked that can be exposed.
	 * 
	 * 
	 * @param inData original input data list
	 * @param priorities original priority input list 
	 * @param comp id to check if exists in the calculated one
	 * @param conf configuration bundle in json format containing all needed information for the recreation of the random selection
	 * @throws InterruptedException 
	 */
	public VerifyProcess (ArrayList<String> inData, ArrayList<Integer> priorities, String comp, String conf) throws InterruptedException {
		System.out.println("IN VERIFY PROCESS FROM VER. TYPE C AND D SIMPLE");
		JSONObject obj = new JSONObject(conf);
		String scenario = obj.getString("scenario");
		Long timestamp = obj.getLong("timestamp");
		String outputConf = obj.getString("output-configuration");
		System.out.println("GOT OBJECTS FROM JSON CONF");
		
		this.de = new DataExtractor(scenario, timestamp);
		System.out.println("CREATED DATAEXTRACTOR");
		CompositeType c = new TypeC(inData, priorities, this.getIntConf(outputConf), de);
		System.out.println("CREATED TYPE C");
		ArrayList<String> outputTest = c.getNewList();
		System.out.println("GOT NEW LIST");
		this.checked = outputTest.contains(comp);
		System.out.println("FINISHED AND COMPARED");
	}
	
	
	/**
	 * Exposes the calculated comparison between the output list given and the calculated one given the 
	 * parameters in configuration.
	 * @return result of the comparison
	 */
	public boolean check() {
		return this.checked;
	}
	
	/**
	 * Function that compares two String array lists to see if the have the same content.
	 * @param d1 first list
	 * @param d2 seconf list
	 * @return comparison result for all values
	 */
	private boolean compareLists(ArrayList<String> d1, ArrayList<String> d2) {
		ArrayList<Boolean> comp= new ArrayList<Boolean>();
		for (String temp2 : d1) {
        		comp.add(d2.contains(temp2) ? true : false);
		}
		boolean rowResult = true;
		for (Boolean el: comp) {
		    rowResult = el && rowResult;
		}
		return rowResult;
	}
	
	/**
	 * Function that takes a double dimension int array in string format and turns it into a valid
	 * double dimension int array.
	 * @param conf double dimension int array in string
	 * @return same content from @conf but in the format to be used in verification
	 */
	private int [][] getIntConf(String conf){
		Pattern p = Pattern.compile("\\[[^\\[]*\\]");
		String [] split = conf.split(",");
		Matcher m = p.matcher(conf);
		int [][] out = new int [2][split.length/2];
		int i = 0;
		while(m.find()) {
			String s = m.group();
			Pattern p2 = Pattern.compile("\\d+");
			Matcher m2 = p2.matcher(s);
			int j = 0;
			while(m2.find()) {
				out[i][j] = Integer.parseInt(m2.group());
				j++;
			}
            i++;
        }
		
		return out;
	}
	
}
