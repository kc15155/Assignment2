package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.Collection;

import bgu.spl.a2.Action;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;

public class Unregister extends Action<Boolean>{
	
	private String studentName;
	
	public Unregister(String studentName)
	{
		this.setActionName("Unregister");
		this.studentName=studentName;
	}
	
	public void start()
	{
		DummyAction checkStudent = new DummyAction();
		Collection<Action<Boolean>> ifExists= new ArrayList<>();
		ifExists.add(checkStudent);
		sendMessage(checkStudent, studentName, myPool.getPrivateState(studentName));
		then(ifExists, new callback() {
			
			@Override
			public void call() {
				((CoursePrivateState)actorState).removeStudent(studentName);
				RemoveYourselfStu removeGrades = new RemoveYourselfStu(actorId);
				Collection<Action<Boolean>> remove= new ArrayList<>();
				remove.add(removeGrades);
				sendMessage(removeGrades, studentName, myPool.getPrivateState(studentName));
				then(remove, new callback() {
					
					@Override
					public void call() {
						complete(true);
					}
				});
			}
		});
		
	}
}
