package bgu.spl.a2;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;



public abstract class Action<R> {
	
	protected String name;
	private Promise<R> result; 
	protected ActorThreadPool myPool;
	private callback myCallback;
	protected String actorId;
	protected PrivateState actorState;
	
    protected abstract void start();
    

   /*package*/ final void handle(ActorThreadPool pool, String actorId, PrivateState actorState) {
	   
	   this.actorState=actorState;
	   this.actorId=actorId;
	   this.myPool=pool;
	   if (myCallback==null)
		   start();
	   else
	   {
		   myCallback.call();
	   }
   }
    
    
   	protected final void then(Collection<? extends Action<?>> actions, callback callback) {
   		
   		final callback toAdd=callback;
    	final int actionSize=actions.size();
   		final int [] counter = new int [1];
   		counter[0]=0;
   		callback newCall = new callback() {
			
			public void call() {
				counter[0]++;
				if (counter[0]==actionSize)
				{
					myCallback=toAdd;
					sendMessage(getAction(), actorId, actorState);
				}
			}
		};
    	for (Action<?> temp : actions)
    	{
    		temp.getResult().subscribe(newCall);
    	}	
    	
    }

 
    protected final void complete(R result) {
       	this.result.resolve(result);
   
    }
    

    public final Promise<R> getResult() {
    	return result;
    }
    

	public Promise<?> sendMessage(Action<?> action, String actorId, PrivateState actorState)
	{
        myPool.submit(action, actorId, actorState);
        return action.getResult();
	}
	

	public void setActionName(String actionName){
        this.name=actionName;
	}

	
	public String getActionName(){
        return name;
	}
	
	private Action<R> getAction()
	{
		return this;
	}
}