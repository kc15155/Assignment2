package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.Collection;

import bgu.spl.a2.Action;
import bgu.spl.a2.callback;
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
		{
			Collection<Action<Boolean>> toAdd = new ArrayList<Action<Boolean>>();
			RemoveYourself remove = new RemoveYourself(actorId);
			toAdd.add(remove);
			sendMessage(remove, studentName, tempStudent);
			callback call = new callback() {
				
				public void call() {
					complete(true);
				}
			};
			then(toAdd, call);
		}
	}

}
