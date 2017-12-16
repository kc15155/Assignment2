package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.Collection;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class CloseCourse extends Action{
	
	private CoursePrivateState toRemove;
	private String courseName;
	
	public CloseCourse(CoursePrivateState toRemove, String courseName)
	{
		this.setActionName("Close Course");
		this.toRemove=toRemove;
		this.courseName=courseName;
	}
	
	public void start()
	{
		Collection<Unregister> myCollection= new ArrayList<Unregister>();
		for (String temp : toRemove.getRegStudents())
		{
			StudentPrivateState tempStudent=((StudentPrivateState)myPool.getActors().get(temp));
			Unregister tempUn= new Unregister(tempStudent, temp);
			myCollection.add(tempUn);
			Promise tempPromise = sendMessage(tempUn, courseName, toRemove);
		}
		callback tempCallback = new callback() {
			
			public void call() {
				((DepartmentPrivateState)actorState).getCourseList().remove(courseName);
				toRemove.setAvailable(-1);
				complete(true);
			}
		};
		then(myCollection, tempCallback);
	}

}
