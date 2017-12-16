package bgu.spl.a2;


import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;



public class ActorThreadPool {
	
	private ConcurrentHashMap<String, String> stateMap;
	private ConcurrentHashMap<String, Queue<Action>> actionMap;
	private ConcurrentHashMap<String, AtomicBoolean> blockMap;
	private Thread [] threadArray;
	private VersionMonitor myVer,terminateVM;
	private final boolean[] terminate = {false};
	private final int[] activeThreads = {0};
	
	
	public ActorThreadPool(int nthreads) {
		stateMap = new ConcurrentHashMap<String, String>();
		actionMap = new ConcurrentHashMap<String, Queue<Action>>();
		blockMap = new ConcurrentHashMap<String, AtomicBoolean>();
		threadArray = new Thread [nthreads];
		myVer= new VersionMonitor();
		terminateVM=new VersionMonitor();
		Runnable r = new Runnable() {
			public void run() {
				int before = myVer.getVersion();
				synchronized (activeThreads) {
					activeThreads[0]++;
					}
				
				for (String temp : actionMap.keySet())
				{
					if (!actionMap.get(temp).isEmpty())
					{
						if (blockMap.get(temp).compareAndSet(true,false))
						{
							actionMap.get(temp).poll().handle(getPool(), temp, getPrivateStates(temp));
							myVer.inc();
						}
							blockMap.get(temp).set(true);	
					}
				}
				if (before==myVer.getVersion())
				{
				try {myVer.await(myVer.getVersion());}
				catch (InterruptedException e) {
					if (terminate[0])
						{terminateVM.inc();
						synchronized (activeThreads) {
							activeThreads[0]--;	
						}
						}
					else
					run();
					}	
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
		terminate[0]=true;
		myVer.inc();
		while (activeThreads[0]!=0)
		{
		try {
			terminateVM.await(terminateVM.getVersion());
		}
		catch (InterruptedException e) { }
		}
	}


	public void start() {
		for (Thread temp : threadArray)
		{
			temp.start();
		}
	}
	
	
	public PrivateState getPrivateStates(String actorId){
		if (stateMap.get("student").contains(actorId))
			return new StudentPrivateState();
		if (stateMap.get("course").contains(actorId))
			return new CoursePrivateState();
		if (stateMap.get("department").contains(actorId))
			return new DepartmentPrivateState();
		else
			return null;
	}
	
	public Map<String, PrivateState> getActors(){
		HashMap<String, PrivateState> output = new HashMap<String, PrivateState>();
		for (String temp : actionMap.keySet())
		{
			PrivateState myState = getPrivateStates(temp);
			output.put(temp, myState);
		}
		return output;
	}
	
	private ActorThreadPool getPool ()
	{
		return this;
	}
	
	public void addActor (String actorId, PrivateState actorstate)
	{
		actionMap.putIfAbsent(actorId, new LinkedList<Action>());
		if (actorstate instanceof DepartmentPrivateState)
			stateMap.put("department", actorId);
		if (actorstate instanceof CoursePrivateState)
			stateMap.put("course", actorId);
		if (actorstate instanceof StudentPrivateState)
			stateMap.put("student", actorId);
		blockMap.putIfAbsent(actorId, new AtomicBoolean(true));
	}
	

}
