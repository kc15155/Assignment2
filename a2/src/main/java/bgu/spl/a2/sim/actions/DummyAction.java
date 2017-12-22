package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;

public class DummyAction extends Action<Boolean>{
	
	public DummyAction()
	{
		setActionName("Dummy Action");
	}
	
	public void start()
	{
		complete(true);
	}

}
