package bgu.spl.a2;


import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;



public class ActorThreadPool {
	
	ConcurrentHashMap<String, String> stateMap;
	ConcurrentHashMap<String, Queue<Action>> actionMap;
	ConcurrentHashMap<String, AtomicBoolean> blockMap;
	Thread [] threadArray;
	VersionMonitor myVer;
	
	public ActorThreadPool(int nthreads) {
		stateMap = new ConcurrentHashMap<String, String>();
		actionMap = new ConcurrentHashMap<String, Queue<Action>>();
		blockMap = new ConcurrentHashMap<String, AtomicBoolean>();
		threadArray = new Thread [nthreads];
		myVer= new VersionMonitor();
		Runnable r = new Runnable() {
			public void run() {
				for (String temp : actionMap.keySet())
				{
					if (!actionMap.get(temp).isEmpty())
					{
						if (blockMap.get(temp).compareAndSet(true,false))
						{
							actionMap.get(temp).poll().handle(getPool(), temp, checkPrivateState(temp));
							myVer.inc();
						}
							blockMap.get(temp).set(true);	
					}
				}
				try {myVer.await(myVer.getVersion());}
				catch (InterruptedException e) {
					run();
				}
				
			}
			
		};
		for (int i=0; i<nthreads ; i++)
		{
			threadArray[i]=new Thread(r);
		}
	}


	public void submit(Action<?> action, String actorId, PrivateState actorState) {
		if (actionMap.containsKey(actorId))
			actionMap.get(actorId).add(action);
		else
		{
			actionMap.put(actorId, new LinkedList<Action>());
			actionMap.get(actorId).add(action);
		}
		blockMap.putIfAbsent(actorId, new AtomicBoolean(true));
		if (actorState instanceof CoursePrivateState)
			stateMap.putIfAbsent("course", actorId);
		if (actorState instanceof StudentPrivateState)
			stateMap.putIfAbsent("student", actorId);
		if (actorState instanceof DepartmentPrivateState)
			stateMap.putIfAbsent("department", actorId);
		myVer.inc();
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
	
	private PrivateState checkPrivateState (String actorId)
	{
		if (stateMap.get("student").contains(actorId))
			return new StudentPrivateState();
		if (stateMap.get("course").contains(actorId))
			return new CoursePrivateState();
		if (stateMap.get("department").contains(actorId))
			return new DepartmentPrivateState();
		else
			return null;
	}
	
	private ActorThreadPool getPool ()
	{
		return this;
	}
	

}
