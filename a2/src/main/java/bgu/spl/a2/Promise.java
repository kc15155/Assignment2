package bgu.spl.a2;

import java.util.ArrayList;


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

	
	public void subscribe(callback callback) {
		if (isRes)
			callback.call();
		else
		{
				subscribers.add(callback);
		}
	}
}
