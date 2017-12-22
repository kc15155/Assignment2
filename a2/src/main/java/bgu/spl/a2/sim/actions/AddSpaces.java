package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class AddSpaces extends Action <Boolean>{
	
	private int newSpots;
	
	public AddSpaces(int toAdd)
	{
		setActionName("Add Spaces");
		newSpots=toAdd;
	}
	
	public void start()
	{
		
			int temp=((CoursePrivateState)actorState).getAvailableSpots().intValue();
			if (temp!=-1)
			{
				((CoursePrivateState)actorState).setAvailable(temp+newSpots);
				complete(true);
			}
			else
				complete(false);
	}
}
