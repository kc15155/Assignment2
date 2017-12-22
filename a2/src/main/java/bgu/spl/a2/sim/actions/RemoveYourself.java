package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RemoveYourself extends Action<Boolean> {
	
	private String courseName;
	
	public RemoveYourself(String name)
	{
		setActionName("Remove Yourself");
		this.courseName=name;
	}
	
	public void start()
	{
		if (((StudentPrivateState)actorState).getGrades().containsKey(courseName))
		((StudentPrivateState)actorState).getGrades().remove(courseName);
		complete(true);
	}

}
