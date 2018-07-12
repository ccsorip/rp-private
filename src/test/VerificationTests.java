package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import cl.clcert.de.DataExtractor;
import cl.clcert.rp.TypeA;
import cl.clcert.rp.TypeB;
import cl.clcert.rp.TypeC;
import cl.clcert.rp.TypeD;
import cl.clcert.vp.VerifyProcess;

public class VerificationTests {

	private static ArrayList<String> ids;
	private static ArrayList<Integer> priorities;
	private static int [][] testConf;
	private static ArrayList<String> aTest;
	private static ArrayList<String> bTest;
	private static ArrayList<String> cTest;
	private static ArrayList<String> dTest;
	private static DataExtractor extractor;
	private static String val1;
	private static String val2;
	
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
		
		aTest = new ArrayList<String>();
		aTest.add("a105");
		aTest.add("a63");
		aTest.add("a52");
		aTest.add("a6");
		aTest.add("a77");
		
		bTest = new ArrayList<String>();
		bTest.addAll(aTest);
		bTest.add("a78");
		
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
		
		val1 = "a77";
		val2 = "a230";
		
	}
	
	@Before
	public void setConf() {
		testConf = new int [][]{{1, 2, 3}, {5, 0, 5}};
	}
	
	@Test
	public void verifyInAFull() throws InterruptedException {
		TypeA ta = new TypeA(ids, 5, extractor);
		VerifyProcess a = new VerifyProcess(ids, ta.getNewList(), ta.getBundle());
		assertTrue(a.check());
	}
	
	@Test
	public void verifyNotinAFull() throws InterruptedException {
		TypeA ta = new TypeA(ids, 5, extractor);
		ArrayList<String> l = ta.getNewList();
		l.add("extra");
		VerifyProcess a = new VerifyProcess(ids, l, ta.getBundle());
		assertFalse(a.check());
	}
	
	@Test
	public void verifyInASingle() throws InterruptedException {
		TypeA ta = new TypeA(ids, 5, extractor);
		VerifyProcess a = new VerifyProcess(ids, val1, ta.getBundle());
		assertTrue(a.check());
	}
	
	@Test
	public void verifyNotinASingle() throws InterruptedException {
		TypeA ta = new TypeA(ids, 5, extractor);
		VerifyProcess a = new VerifyProcess(ids, val2, ta.getBundle());
		assertFalse(a.check());
	}
	
	
	@Test
	public void verifyInBFull() throws InterruptedException {
		TypeB tb = new TypeB(ids, 5, extractor);
		VerifyProcess b = new VerifyProcess(ids, tb.getNewList(), tb.getBundle());
		assertTrue(b.check());
	}
	
	@Test
	public void verifyNotinBFull() throws InterruptedException {
		TypeB tb = new TypeB(ids, 5, extractor);
		ArrayList<String> l = tb.getNewList();
		l.add("extra");
		VerifyProcess b = new VerifyProcess(ids, l, tb.getBundle());
		assertFalse(b.check());
	}
	
	@Test
	public void verifyInBSimple() throws InterruptedException {
		TypeB tb = new TypeB(ids, 5, extractor);
		VerifyProcess b = new VerifyProcess(ids, val1, tb.getBundle());
		assertTrue(b.check());
	}
	
	@Test
	public void verifyNotinBSimple() throws InterruptedException {
		TypeB tb = new TypeB(ids, 5, extractor);
		VerifyProcess b = new VerifyProcess(ids, val2, tb.getBundle());
		assertFalse(b.check());
	}
	
	@Test
	public void verifyInCFull() throws InterruptedException {
		TypeC tc = new TypeC(ids, priorities, testConf, extractor);
		VerifyProcess c = new VerifyProcess(ids, priorities, tc.getNewList(), tc.getBundle());
		assertTrue(c.check());
	}
	
	@Test
	public void verifyNotinCFull() throws InterruptedException {
		TypeC tc = new TypeC(ids, priorities, testConf, extractor);
		ArrayList<String> l = tc.getNewList();
		l.add("extra");
		VerifyProcess c = new VerifyProcess(ids, priorities, l, tc.getBundle());
		assertFalse(c.check());
	}
	
	@Test
	public void verifyInCSimple() throws InterruptedException {
		TypeC tc = new TypeC(ids, priorities, testConf, extractor);
		VerifyProcess c = new VerifyProcess(ids, priorities, val1, tc.getBundle());
		assertTrue(c.check());
	}
	
	@Test
	public void verifyNotinCSimple() throws InterruptedException {
		TypeC tc = new TypeC(ids, priorities, testConf, extractor);
		VerifyProcess c = new VerifyProcess(ids, priorities, val2, tc.getBundle());
		assertFalse(c.check());
	}
	
	@Test
	public void verifyInDFull() throws InterruptedException {
		TypeD td = new TypeD(ids, priorities, testConf, extractor);
		VerifyProcess d = new VerifyProcess(ids, priorities, td.getNewList(), td.getBundle());
		assertTrue(d.check());
	}
	
	@Test
	public void verifyNotinDFull() throws InterruptedException {
		TypeD td = new TypeD(ids, priorities, testConf, extractor);
		ArrayList<String> l = td.getNewList();
		l.add("extra");
		VerifyProcess d = new VerifyProcess(ids, priorities, l, td.getBundle());
		assertFalse(d.check());
	}
	
	@Test
	public void verifyInDSimple() throws InterruptedException {
		TypeD td = new TypeD(ids, priorities, testConf, extractor);
		VerifyProcess d = new VerifyProcess(ids, priorities, val1, td.getBundle());
		assertTrue(d.check());
	}
	
	@Test
	public void verifyNotinDSimple() throws InterruptedException {
		TypeD td = new TypeD(ids, priorities, testConf, extractor);
		VerifyProcess d = new VerifyProcess(ids, priorities, val2, td.getBundle());
		assertFalse(d.check());
	}

}
