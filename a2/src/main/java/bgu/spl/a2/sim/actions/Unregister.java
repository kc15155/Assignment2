package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Unregister extends Action<Boolean>{
	
	private StudentPrivateState myStudent;
	private String studentName;
	
	public Unregister(StudentPrivateState student, String studentName)
	{
		this.setActionName("Unregister");
		myStudent=student;
		this.studentName=studentName;
	}
	
	public void start()
	{
		
			if(((CoursePrivateState)actorState).removeStudent(studentName))
				myStudent.getGrades().remove(actorId);	
			actorState.addRecord(getActionName());
			complete(true);
		
	}

}
