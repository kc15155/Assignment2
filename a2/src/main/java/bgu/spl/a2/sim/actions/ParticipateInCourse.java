package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.Collection;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class ParticipateInCourse extends Action <Boolean>{
	
	private String studentName;
	private Integer grade;

	
	public ParticipateInCourse (String toAdd, int grade)
	{
		this.setActionName("Participate In Course");
		this.studentName=toAdd;
		this.grade=new Integer(grade);
	}
	
	public void start()
	{

			MeetsRequirements checkReqs = new MeetsRequirements(((CoursePrivateState)actorState).getPrequisites());
			sendMessage(checkReqs, studentName, myPool.getPrivateState(studentName));
			Collection<Action<Boolean>> toCheck = new ArrayList<Action<Boolean>>();
			toCheck.add(checkReqs);
			callback callback = new callback() {
				
				public void call() {
					if (checkReqs.getResult().get() && ((CoursePrivateState)actorState).getAvailableSpots()>0)
					{
						((CoursePrivateState)actorState).addStudent(studentName);
						AddNewGrade addNewGrade = new AddNewGrade(actorId, grade);
						sendMessage(addNewGrade, studentName, myPool.getPrivateState(studentName));
						Collection<Action<Boolean>> toCheckSecond = new ArrayList<Action<Boolean>>();
						toCheckSecond.add(addNewGrade);
						then(toCheckSecond, new callback() {
							
							@Override
							public void call() {
								complete(true);
							}
						});
					}
					else
						{
						complete(false);
					}
				}
			};
			then(toCheck, callback);
		
		
	}
	
}
