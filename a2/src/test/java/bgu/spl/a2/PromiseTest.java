package bgu.spl.a2;

import junit.framework.Assert;
import junit.framework.TestCase;

public class PromiseTest extends TestCase {
	
	Promise<Integer> toTest;
	
	protected void setUp() throws Exception {
		toTest = new Promise<Integer>();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGet() {
		try {
			Integer result=toTest.get();
			Assert.fail();
		}
		catch (IllegalStateException e) {}
		
		toTest.resolve(new Integer(15));
		try {
			Integer goodResult=toTest.get();
			assertEquals(new Integer(15), goodResult);
		} catch (IllegalStateException e) {Assert.fail();}
	}

	public void testIsResolved() {
		assertEquals(false, toTest.isResolved());
		toTest.resolve(new Integer(15));
		assertEquals(true, toTest.isResolved());
	}

	public void testResolve() {
		try
		{
			toTest.resolve(5);
			try {
				toTest.resolve(6);
				Assert.fail();
			}
			catch (IllegalStateException ex)
			{
				int x=toTest.get();
				assertEquals(x, 5);
			}
			catch (Exception ex)
			{
				Assert.fail();
			}
		}
		catch (Exception ex) {
			Assert.fail();
		}
	}

	public void testSubscribe() {
		final int [] testArray = {2};
		callback toAdd = new callback() {
		
			public void call() {
				testArray[0]=testArray[0]*2;
			}
		};
		toTest.subscribe(toAdd);
		toTest.subscribe(toAdd);
		try{
			toTest.resolve(10);
		}
		catch (Exception e) {Assert.fail();}
		assertEquals(4, testArray[0]);
		
		toTest.subscribe(toAdd);
		assertEquals(8, testArray[0]);
		
	}

}
