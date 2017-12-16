package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.Collection;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class ParticipateInCourse extends Action {
	
	private String toAdd;
	private Integer grade;
	private StudentPrivateState newStudent;
	
	public ParticipateInCourse (StudentPrivateState newStudent, String toAdd)
	{
		this.setActionName("Participate in Course");
		this.toAdd=toAdd;
		grade=null;
		this.newStudent=newStudent;
	}
	
	public ParticipateInCourse (StudentPrivateState newStudent, String toAdd, int grade)
	{
		this.setActionName("Participate in Course");
		this.toAdd=toAdd;
		this.grade=new Integer(grade);
		this.newStudent=newStudent;
	}
	
	public void start()
	{
		if (actorState instanceof CoursePrivateState)
		{
			
			MeetsRequirements checkReqs = new MeetsRequirements(((CoursePrivateState)actorState).getPrequisites());
			final Promise<Boolean> myPromise = sendMessage(checkReqs, toAdd, newStudent);
			Collection<Action> toCheck = new ArrayList<Action>();
			toCheck.add(checkReqs);
			callback callback = new callback() {
				
				public void call() {
					if (myPromise.get())
						{
						if (((CoursePrivateState)actorState).addStudent(toAdd))
							newStudent.getGrades().put(actorId,grade);
						}
				}
			};
			actorState.addRecord(getActionName());
			then(toCheck, callback);
		}	
		
	}
	
}
