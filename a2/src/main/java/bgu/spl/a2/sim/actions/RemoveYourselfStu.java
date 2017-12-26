package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RemoveYourselfStu extends Action<Boolean> {
	
	private String courseName;
	
	public RemoveYourselfStu(String name)
	{
		setActionName("Remove Yourself (Student)");
		this.courseName=name;
	}
	
	public void start()
	{
		((StudentPrivateState)actorState).removeGrade(courseName);
		complete(true);
	}

}
