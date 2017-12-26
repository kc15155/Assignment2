package bgu.spl.a2.sim.actions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class CloseCourseImmediately extends Action<List<String>>{
	
	public CloseCourseImmediately()
	{
		setActionName("Close Course Immediately");
	}
	
	public void start()
	{
		((CoursePrivateState)actorState).setAvailable(-1);
		complete(((CoursePrivateState)actorState).getRegStudents());
	}

}
