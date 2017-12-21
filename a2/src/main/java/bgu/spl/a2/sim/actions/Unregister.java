package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class Unregister extends Action<Boolean>{
	
	private String studentName;
	
	public Unregister(String studentName)
	{
		this.setActionName("Unregister");
		this.studentName=studentName;
	}
	
	public void start()
	{
		StudentPrivateState tempStudent = (StudentPrivateState) myPool.getPrivateStates(studentName);
		
			if(((CoursePrivateState)actorState).removeStudent(studentName))
				tempStudent.getGrades().remove(actorId);	
			actorState.addRecord(getActionName());
			complete(true);
		
	}

}
