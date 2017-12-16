package bgu.spl.a2.sim.actions;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class OpenCourse extends Action{
	
	private CoursePrivateState newCourse;
	private String courseName;
	
	public  OpenCourse (int available, List<String> reqs, String courseName)
	{
		setActionName("Open Course");
		newCourse = new CoursePrivateState();
		newCourse.setAvailable(available);
		newCourse.setPrequisites(reqs);
		this.courseName=courseName;
	}
	
	public void start()
	{
		if (actorState instanceof DepartmentPrivateState)
		{
			((DepartmentPrivateState)actorState).addCourse(courseName);
			actorState.addRecord(getActionName());
			myPool.addActor(courseName, newCourse);
		}
		
	}

}