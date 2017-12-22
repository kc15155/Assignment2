package bgu.spl.a2;


import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import org.w3c.dom.css.Counter;

import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;



public class ActorThreadPool {
	
	private HashMap<String, PrivateState> stateMap;
	private ConcurrentHashMap<String, Queue<Action>> actionMap;
	private ConcurrentHashMap<String, AtomicBoolean> blockMap;
	private Thread [] threadArray;
	private VersionMonitor myVer;
	private final boolean[] terminate = {false};
	CountDownLatch terminator;
	protected ActorThreadPool myPool;
	
	
	public ActorThreadPool(int nthreads) {
		stateMap = new HashMap<String, PrivateState>();
		actionMap = new ConcurrentHashMap<String, Queue<Action>>();
		blockMap = new ConcurrentHashMap<String, AtomicBoolean>();
		threadArray = new Thread [nthreads];
		myVer= new VersionMonitor();
		terminator = new CountDownLatch(nthreads);
		myPool=this;
		
		Runnable r = new Runnable() {
			public void run() {
				while (!terminate[0])
				{
				try
				{
					boolean wasBusy=false;
					int before = myVer.getVersion();
					for (String temp : actionMap.keySet())
					{

						if (blockMap.get(temp)==null || stateMap.get(temp)==null)
							throw new ConcurrentModificationException();
						if (blockMap.get(temp).compareAndSet(true,false))
						{
							Action tempAction;
							wasBusy=true;
							if (!actionMap.get(temp).isEmpty())
							{
								synchronized (actionMap.get(temp)) {
										tempAction=actionMap.get(temp).remove();
										myVer.inc();
								}
									tempAction.handle(myPool, temp, myPool.getPrivateStates(temp));
							}
							blockMap.get(temp).set(true);
						}	
					   }
					

						if (!wasBusy)
						{
							try {myVer.await(before);}
							catch (InterruptedException e) {}
						}
					}
					
						catch (ConcurrentModificationException notImportant) {} //try again
					
			}
				terminator.countDown();
			}
			
		};
		
		for (int i=0; i<nthreads ; i++)
		{
			threadArray[i]=new Thread(r);
		}
	}


	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		if (!actionMap.containsKey(actorId))
		{
			actionMap.put(actorId, new ArrayDeque<Action>());
			stateMap.put(actorId, actorState);
			blockMap.put(actorId, new AtomicBoolean(true));
		}
		actionMap.get(actorId).add(action);
		myVer.inc();
	}


	public void shutdown() throws InterruptedException {
		terminate[0]=true;
		myVer.inc();
		terminator.await();
	}


	public void start() {
		for (Thread temp : threadArray)
		{
			temp.start();
		}
	}
	
	
	public PrivateState getPrivateStates(String actorId){
			return stateMap.get(actorId);
	}
	
	public Map<String, PrivateState> getActors(){
		return stateMap;
	}
	

}
