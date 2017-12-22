package bgu.spl.a2.sim.actions;


import java.util.ArrayList;
import java.util.Collection;
import bgu.spl.a2.Action;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AddStudent extends Action<Boolean> {
	
	private String studentName;
	
	public  AddStudent (String name)
	{
		setActionName("Add Student");
		this.studentName=name;
	}
	
	public void start()
	{
			
			Collection<Action<Boolean>> toAdd = new ArrayList<Action<Boolean>>();
			DummyAction dummy = new DummyAction();
			toAdd.add(dummy);
			sendMessage(dummy, studentName, new StudentPrivateState());
			callback callback = new callback() {
				
				public void call() {
					((DepartmentPrivateState)actorState).addStudent(studentName);
					complete(true);
				}
			};
			then(toAdd, callback);
			
	}

}
