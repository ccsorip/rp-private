package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import cl.clcert.de.DataExtractor;
import cl.clcert.rp.TypeA;
import cl.clcert.rp.TypeB;

public class SimpleTypeTests {

	private static ArrayList<String> ids;
	private static ArrayList<String> aTest;
	private static ArrayList<String> bTest;
	private static DataExtractor extractor;
	
	@BeforeClass
	public static void prepare() throws InterruptedException {
		ids = new ArrayList<String>();
		while (ids.size() < 120) {
			ids.add("a" + ids.size());
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
		
	}	
	
	@Test
	public void checkTypeATest() throws InterruptedException {
		TypeA a = new TypeA(ids, 5, extractor);
		assertEquals("TypeA", a.getClass().getSimpleName());
	}


	@Test
	public void checkTypeBTest() throws InterruptedException {
		TypeB b = new TypeB(ids, 5, extractor);
		assertEquals("TypeB", b.getClass().getSimpleName());	
	}
	
	@Test
	public void lengthATest() throws InterruptedException {
		TypeA a = new TypeA(ids, 5, extractor);
		assertEquals(5, a.getNewList().size());
	}
	
	@Test
	public void lengthBTest() throws InterruptedException {
		TypeB b = new TypeB(ids, 5, extractor);
		assertEquals(6, b.getNewList().size());
	}

	@Test
	public void equalListA() throws InterruptedException {
		TypeA a = new TypeA(ids, 5, extractor);
		assertTrue(aTest.containsAll(a.getNewList()));
	}
	
	@Test
	public void equalListB() throws InterruptedException {
		TypeB b = new TypeB(ids, 5, extractor);
		assertTrue(bTest.containsAll(b.getNewList()));
	}
	
	@Test
	public void checkTimestampA() throws InterruptedException {
		TypeA a = new TypeA(ids, 5, extractor);
		assertEquals("1526513000", a.getTimestamp());
	}
	
	@Test
	public void checkTimestampB() throws InterruptedException {
		TypeB b = new TypeB(ids, 5, extractor);
		assertEquals("1526513000", b.getTimestamp());
	}
	
	
}
