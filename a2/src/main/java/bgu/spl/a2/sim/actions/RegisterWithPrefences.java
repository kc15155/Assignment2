package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RegisterWithPrefences extends Action {
	
	private CoursePrivateState [] coursesList;
	private int [] gradesList;
	String courseName;
	
	public RegisterWithPrefences(CoursePrivateState [] coursesList, String course)
	{
		setActionName("Register With Prefences");
		this.coursesList=coursesList;
		gradesList=null;
		courseName=course;
	}
	
	public RegisterWithPrefences(CoursePrivateState [] coursesList, int [] gradeList, String course)
	{
		setActionName("Register With Prefences");
		this.coursesList=coursesList;
		this.gradesList=gradeList;
		courseName=course;
	}
	
	public void start ()
	{
		boolean isAdded=false;
		int index=0;
		for (CoursePrivateState temp : coursesList)
		{
			if (isAdded)
				break;
			if (temp.addStudent(actorId))
			{
				if (gradesList==null)
				((StudentPrivateState)actorState).getGrades().put(courseName, null);
				else 
				((StudentPrivateState)actorState).getGrades().put(courseName, gradesList[index]);	
				isAdded=true;
			}
			else
				index++;
		}
		actorState.addRecord("Register With Prefences");
	}
	

}
