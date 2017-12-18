package bgu.spl.a2.sim;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import bgu.spl.a2.Promise;


public class SuspendingMutex {
	
	private AtomicBoolean isFree;
	private Queue<Promise<Computer>> promiseQueue;
	private Computer myComputer;
	
	public SuspendingMutex(Computer myComp)
	{
		isFree = new AtomicBoolean(true);
		promiseQueue = new LinkedList<Promise<Computer>>();
		myComputer=myComp;
	}
	
	
	public Promise<Computer> down(){
		Promise<Computer> newPromise = new Promise<Computer>();
		if (!isFree.get())
		{
			promiseQueue.add(newPromise);	
		}
		else
		{
			isFree.compareAndSet(true, false);
			newPromise.resolve(myComputer);
		}
		return newPromise;
	}

	
	public void up(){
		if (!promiseQueue.isEmpty())
		promiseQueue.poll().resolve(myComputer);
		else
			isFree.compareAndSet(false, true);
	}
}
