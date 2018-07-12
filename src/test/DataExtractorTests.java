package test;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import cl.clcert.de.DataExtractor;

public class DataExtractorTests {
	
	private static DataExtractor a;
	private static DataExtractor b;
	private static DataExtractor c;
	private static DataExtractor d;
	
	private static DataExtractor aT;
	private static DataExtractor bT;
	private static DataExtractor cT;
	private static DataExtractor dT;
	
	@BeforeClass
	public static void prepare() throws InterruptedException {
		a = new DataExtractor("A");
		b = new DataExtractor("B");
		c = new DataExtractor("C");
		d = new DataExtractor("D");
		
		aT = new DataExtractor("A", 1526513000);
		bT = new DataExtractor("B", 1526513000);
		cT = new DataExtractor("C", 1526513000);
		dT = new DataExtractor("D", 1526513000);
	}
	
	@Test
	public void testAType() throws InterruptedException {
		assertEquals("A", a.type());
	}
	
	@Test
	public void testBType() throws InterruptedException {
		assertEquals("B", b.type());
	}
	
	@Test
	public void testCType() throws InterruptedException {
		assertEquals("C", c.type());
	}
	
	@Test
	public void testDType() throws InterruptedException {
		assertEquals("D", d.type());
	}
	
	@Test
	public void testSameSeedA() {
		assertEquals("d6bb448da0dd6f9e1ea039c2aeeb8cac69df435", aT.getSeed());
	}

	@Test
	public void testSameSeedB() {
		assertEquals("d6bb448da0dd6f9e1ea039c2aeeb8cac69df435", bT.getSeed());
	}
	
	@Test
	public void testSameSeedC() {
		assertEquals("FE24CD38CFD06E78BDEAFA438E24E7B73C29459", cT.getSeed());
	}
	
	@Test
	public void testSameSeedD() {
		assertEquals("289F89B56F0D01E6A34AC38120CF6B1B55F606C", dT.getSeed());
	}
	
	@Test
	public void testBeaconUsedA() {
		assertEquals("udechile", a.beaconUsed());
	}
	
	@Test
	public void testBeaconUsedB() {
		assertEquals("udechile", b.beaconUsed());
	}

	@Test
	public void testBeaconUsedC() {
		assertEquals("udechile-nist", c.beaconUsed());
	}
	
	@Test
	public void testBeaconUsedD() {
		assertEquals("nist", d.beaconUsed());
	}
	
	
}
