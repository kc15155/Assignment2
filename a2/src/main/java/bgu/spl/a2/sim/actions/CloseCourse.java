package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;


public class CloseCourse extends Action<Boolean>{
	
	private String courseName;
	
	public CloseCourse(String courseName)
	{
		this.setActionName("Close Course");
		this.courseName=courseName;
	}
	
	public void start()
	{
		CloseCourseImmediately firstStep = new CloseCourseImmediately();
		Collection<Action<List<String>>> myCollection= new ArrayList<>();
		myCollection.add(firstStep);
		sendMessage(firstStep, courseName, myPool.getPrivateState(courseName));
		then(myCollection, new callback() {
			
			@Override
			public void call() {
				for (String temp : firstStep.getResult().get())
				{
					Unregister tempUn= new Unregister(temp);
					sendMessage(tempUn, courseName, myPool.getPrivateState(courseName));
				}
				((DepartmentPrivateState)actorState).removeCourse(courseName);
				complete(true);
			}
		});
	}

}
