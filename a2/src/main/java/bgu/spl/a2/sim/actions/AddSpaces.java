package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class AddSpaces extends Action {
	
	private int newSpots;
	
	public AddSpaces(int toAdd)
	{
		newSpots=toAdd;
	}
	
	public void start()
	{
		if (actorState instanceof CoursePrivateState)
		{
			int temp=((CoursePrivateState)actorState).getAvailableSpots().intValue();
			if (temp!=-1)
				((CoursePrivateState)actorState).setAvailable(temp+newSpots);
			actorState.addRecord(getActionName());
		}
	}
}
