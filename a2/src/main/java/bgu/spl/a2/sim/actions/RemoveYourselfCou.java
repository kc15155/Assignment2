package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class RemoveYourselfCou extends Action<Boolean> {
	
	private String studentName;
	
	public RemoveYourselfCou(String name)
	{
		setActionName("Remove Yourself (Course)");
		this.studentName=name;
	}
	
	public void start()
	{
		((CoursePrivateState)actorState).removeStudent(studentName);
		complete(true);
	}

}
