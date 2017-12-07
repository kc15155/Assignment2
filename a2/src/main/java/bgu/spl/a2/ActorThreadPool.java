package bgu.spl.a2;

import java.util.concurrent.ConcurrentHashMap;



public class ActorThreadPool {
	
	ConcurrentHashMap<PrivateState, String> stateMap;
	ConcurrentHashMap<String, Action> actionMap;
	Thread [] threadArray;

	
	public ActorThreadPool(int nthreads) {
		stateMap = new ConcurrentHashMap<PrivateState, String>();
		actionMap = new ConcurrentHashMap<String, Action>();
		threadArray = new Thread [nthreads];
		Runnable r = new Runnable() {
			public void run() {
			
			}
			
		};
		for (int i=0; i<nthreads ; i++)
		{
			threadArray[i]=new Thread(r);
		}
	}


	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		stateMap.putIfAbsent(actorState, actorId);
		actionMap.putIfAbsent(actorId, action);
	}


	public void shutdown() throws InterruptedException {
		for (Thread temp: threadArray)
		{
			temp.interrupt();
			temp.join();
		}
		throw new InterruptedException("No existing threads");
	}


	public void start() {
		for (Thread temp : threadArray)
		{
			temp.start();
		}
	}

}
