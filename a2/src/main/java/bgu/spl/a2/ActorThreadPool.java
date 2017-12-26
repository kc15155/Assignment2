package bgu.spl.a2;


import java.util.ArrayDeque;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;



public class ActorThreadPool {
	
	private HashMap<String, PrivateState> stateMap;
	private ConcurrentHashMap<String, Queue<Action>> actionMap;
	private ConcurrentHashMap<String, AtomicBoolean> blockMap;
	private Thread [] threadArray;
	private VersionMonitor myVer;
	private final boolean[] terminate = {false};
	CountDownLatch terminator;
	protected ActorThreadPool myPool;
	
	private synchronized HashMap<String, Action> getAction()
	{
		for (String temp : actionMap.keySet())
		{
			try
			{
			if (blockMap.get(temp)==null || stateMap.get(temp)==null)
				throw new ConcurrentModificationException();
			if (blockMap.get(temp).compareAndSet(true,false))
			{ 
				if (!actionMap.get(temp).isEmpty())
				{
					HashMap<String, Action> output = new HashMap<>();
					Action tempAction = actionMap.get(temp).remove();
					output.put(temp, tempAction);
					return output;
				}
				else
				{
					synchronized (blockMap) {
						blockMap.get(temp).set(true);
					}
				}
			}	
			}
			catch (ConcurrentModificationException notImportant) {}	
	   }
		return null;
	}
	
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
					int before = myVer.getVersion();
					HashMap<String, Action> tempMap = getAction();
					if (tempMap!=null)
					{
						
						String actorId=(String) (tempMap.keySet().toArray())[0];
						tempMap.get(actorId).handle(myPool, actorId, getPrivateState(actorId));
						synchronized (blockMap) {
							blockMap.get(actorId).set(true);
						}
						myVer.inc();
					}
					else
					{
						try {myVer.await(before);}
						catch (InterruptedException toIgnore) { }
					}						
				}
				terminator.countDown();
			}
			
		};
		
		for (int i=0; i<nthreads ; i++)
		{
			threadArray[i]=new Thread(r);
		}
	}


	public synchronized void submit(Action<?> action, String actorId, PrivateState actorState) {
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
	
	
	public PrivateState getPrivateState(String actorId){
			return stateMap.get(actorId);
	}
	
	public Map<String, PrivateState> getActors(){
		return stateMap;
	}
	

}
