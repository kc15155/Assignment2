package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.Collection;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class ParticipateInCourse extends Action <Boolean>{
	
	private String toAdd;
	private Integer grade;

	
	public ParticipateInCourse (String toAdd, int grade)
	{
		this.setActionName("Participate in Course");
		this.toAdd=toAdd;
		this.grade=new Integer(grade);
	}
	
	public void start()
	{

			MeetsRequirements checkReqs = new MeetsRequirements(((CoursePrivateState)actorState).getPrequisites());
			final Promise<Boolean> myPromise = (Promise<Boolean>) sendMessage(checkReqs, toAdd, myPool.getActors().get(toAdd));
			Collection<Action<Boolean>> toCheck = new ArrayList<Action<Boolean>>();
			toCheck.add(checkReqs);
			callback callback = new callback() {
				
				public void call() {
					if (myPromise.get())
						{
						if (((CoursePrivateState)actorState).addStudent(toAdd))
							((StudentPrivateState)myPool.getActors().get(toAdd)).getGrades().put(actorId,grade);
							actorState.addRecord(getActionName());
							complete(true);
						}
				}
			};
			then(toCheck, callback);
		
		
	}
	
}
