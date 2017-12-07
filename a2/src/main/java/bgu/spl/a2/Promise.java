package bgu.spl.a2;

import java.util.ArrayList;
/**
 * this class represents a deferred result i.e., an object that eventually will
 * be resolved to hold a result of some operation, the class allows for getting
 * the result once it is available and registering a callback that will be
 * called once the result is available.
 *
 * Note for implementors: you may add methods and synchronize any of the
 * existing methods in this class *BUT* you must be able to explain why the
 * synchronization is needed. In addition, the methods you add can only be
 * private, protected or package protected - in other words, no new public
 * methods
 *
 * @param <T>
 *            the result type, <boolean> resolved - initialized ;
 */
public class Promise<T>{
	
	private T value;
	private boolean isRes;
	private ArrayList<callback> subscribers;
	
	public Promise ()
	{
		subscribers=new ArrayList<callback>();
		isRes=false;
	}
	
	public T get() {
		if (isRes)
			return value;
		else
			throw new IllegalStateException("Not resolved yet");
	}


	public boolean isResolved() {
		return isRes;
	}



	public void resolve(T value){
		if (isRes)
			throw new IllegalStateException("Already resolved");
		this.value=value;
		isRes=true;
		for (callback tempC : subscribers)
		{
			tempC.call();
		}
		subscribers.clear();
	}

	/**
	 * add a callback to be called when this object is resolved. If while
	 * calling this method the object is already resolved - the callback should
	 * be called immediately
	 *
	 * Note that in any case, the given callback should never get called more
	 * than once, in addition, in order to avoid memory leaks - once the
	 * callback got called, this object should not hold its reference any
	 * longer.
	 *
	 * @param callback
	 *            the callback to be called when the promise object is resolved
	 */
	public void subscribe(callback callback) {
		if (isRes)
			callback.call();
		else
		{
				subscribers.add(callback);
		}
	}
}
