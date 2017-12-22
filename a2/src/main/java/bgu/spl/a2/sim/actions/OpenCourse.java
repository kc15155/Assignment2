package bgu.spl.a2.sim.actions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;

public class OpenCourse extends Action<Boolean>{
	
	
	private String courseName;
	private List<String> reqs;
	private int available;
	
	public  OpenCourse (int available, List<String> reqs, String courseName)
	{
		setActionName("Open Course");
		this.available=available;
		this.reqs=reqs;
		this.courseName=courseName;
	}
	
	public void start()
	{

		Collection<Action<Boolean>> toAdd = new ArrayList<Action<Boolean>>();
		DummyAction dummy= new DummyAction();
		CoursePrivateState newCourse = new CoursePrivateState();
		newCourse.setAvailable(available);
		newCourse.setPrequisites(reqs);
		toAdd.add(dummy);
		sendMessage(dummy, courseName, newCourse);
		callback callback = new callback() {
			
			public void call() {
				((DepartmentPrivateState)actorState).addCourse(courseName);
				complete(true);
			}
		};
		then(toAdd, callback);	
		
	}

}