package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddNewGrade extends Action<Boolean> {
	
	private String courseName;
	private Integer grade;
	
	public AddNewGrade(String course, Integer grade)
	{
		setActionName("Add New Grade");
		courseName=course;
		this.grade=grade;
	}
	
	public void start()
	{
		((StudentPrivateState)actorState).getGrades().put(courseName,grade);
		complete(true);
	}

}
