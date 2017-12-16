package bgu.spl.a2.sim.actions;


import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddStudent extends Action {
	
	private StudentPrivateState newStudent;
	private String studentName;
	
	public  AddStudent (String name)
	{
		setActionName("Add Student");
		newStudent = new StudentPrivateState();
		this.studentName=name;
	}
	
	public void start()
	{
		if (actorState instanceof DepartmentPrivateState)
		{
			((DepartmentPrivateState)actorState).addStudent(studentName);
			actorState.addRecord(getActionName());
			myPool.addActor(studentName, newStudent);
		}
	}

}
