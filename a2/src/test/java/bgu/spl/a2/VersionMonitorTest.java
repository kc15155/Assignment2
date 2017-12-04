package bgu.spl.a2;

import junit.framework.Assert;
import junit.framework.TestCase;

public class VersionMonitorTest extends TestCase {
	
	VersionMonitor toTest;

	protected void setUp() throws Exception {
		toTest=new VersionMonitor();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetVersion() {
		assertEquals(0, toTest.getVersion());
	}

	public void testInc() {
		int get=toTest.getVersion();
		toTest.inc();
		assertEquals(get+1, toTest.getVersion());
		
	}

	public void testAwait() {
		final boolean [] actualTest = {false};
		Runnable r = new Runnable() {
			public void run() {
				int old = toTest.getVersion();
				try {
					toTest.await(toTest.getVersion());
				} catch (InterruptedException e) {
					assertEquals(old+1, toTest.getVersion());
					actualTest[0]=true;
				}
			}
		};
		Thread t= new Thread(r);
		t.start();
		try{Thread.sleep(2000);}
		catch (Exception e) {}
		toTest.inc();
		try {Thread.sleep(2000);}
		catch (Exception e) {}
		if (actualTest[0]==false)
		Assert.fail();
	}

}
