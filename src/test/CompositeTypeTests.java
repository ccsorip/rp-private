package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cl.clcert.de.DataExtractor;
import cl.clcert.rp.TypeC;
import cl.clcert.rp.TypeD;

public class CompositeTypeTests {

	private static ArrayList<String> ids;
	private static ArrayList<Integer> priorities;
	private static int [][] testConf;
	private static ArrayList<String> cTest;
	private static ArrayList<String> dTest;
	private static DataExtractor extractor;
	
	@BeforeClass
	public static void prepare() throws InterruptedException {
		ids = new ArrayList<String>();
		priorities = new ArrayList<Integer>();
		int i=1;
		while (ids.size() < 120) {
			if (i==4) i=1;
			ids.add("a" + ids.size());
			priorities.add(i);
			i++;
		}
		
		
		extractor = new DataExtractor("A", 1526513000);
		
		cTest = new ArrayList<String>();
		cTest.add("a18");
		cTest.add("a3");
		cTest.add("a66");
		cTest.add("a75");
		cTest.add("a111");
		cTest.add("a20");
		cTest.add("a5");
		cTest.add("a68");
		cTest.add("a77");
		cTest.add("a113");
		
		dTest = new ArrayList<String>();
		dTest.addAll(cTest);
		dTest.add("a0");
		dTest.add("a2");
		
	}
	
	@Before
	public void setConf() {
		testConf = new int [][]{{1, 2, 3}, {5, 0, 5}};
	}
	
	private int [][] getIntConf(String c){
		Pattern p = Pattern.compile("\\[[^\\[]*\\]");
		String [] split = c.split(",");
		Matcher m = p.matcher(c);
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
	
	@Test
	public void checkTypeCTest() throws InterruptedException {
		TypeC c = new TypeC(ids, priorities, testConf, extractor);
		assertEquals("TypeC", c.getClass().getSimpleName());
	}


	@Test
	public void checkTypeDTest() throws InterruptedException {
		TypeD d = new TypeD(ids, priorities, testConf, extractor);
		assertEquals("TypeD", d.getClass().getSimpleName());	
	}
	
	@Test
	public void lengthCTest() throws InterruptedException {
		TypeC c = new TypeC(ids, priorities, testConf, extractor);
		assertEquals(10, c.getNewList().size());
	}
	
	@Test
	public void lengthDTest() throws InterruptedException {
		TypeD d = new TypeD(ids, priorities, testConf, extractor);
		assertEquals(12, d.getNewList().size());
	}
	
	@Test
	public void equalListC() throws InterruptedException {
		TypeC c = new TypeC(ids, priorities, testConf, extractor);
		assertTrue(cTest.containsAll(c.getNewList()));
	}
	
	@Test
	public void equalListD() throws InterruptedException {
		TypeD d = new TypeD(ids, priorities, testConf, extractor);
		assertTrue(dTest.containsAll(d.getNewList()));
	}
	
	@Test
	public void outputConfCTest() throws InterruptedException {
		TypeC c = new TypeC(ids, priorities, testConf, extractor);
		String bundle = c.getBundle();
		JSONObject obj = new JSONObject(bundle);
		String outputConf = obj.getString("output-configuration");
		int[][] outConf = getIntConf(outputConf);
		
		assertArrayEquals(testConf, outConf);
	}
	
	@Test
	public void outputConfDTest() throws InterruptedException {
		TypeD d = new TypeD(ids, priorities, testConf, extractor);
		String bundle = d.getBundle();
		JSONObject obj = new JSONObject(bundle);
		String outputConf = obj.getString("output-configuration");
		int[][] outConf = getIntConf(outputConf);
		
		assertArrayEquals(testConf, outConf);
	}
	

}
