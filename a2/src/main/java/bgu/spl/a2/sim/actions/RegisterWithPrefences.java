package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class RegisterWithPrefences extends Action<Boolean> {
	
	private List<String> coursesList;
	private List<Integer> gradesList;
	
	
	public RegisterWithPrefences(List <String> courses, List <Integer> gradeList)
	{
		setActionName("Register With Prefences");
		this.coursesList = courses;
		this.gradesList = gradeList;
	}
	
	public void start ()
	{
		if (coursesList.size()>0)
		{
			String temp=coursesList.get(0);
			MeetsRequirements meets = new MeetsRequirements(((CoursePrivateState)myPool.getActors().get(temp)).getPrequisites());
			final Promise <Boolean> tempProm = (Promise<Boolean>) sendMessage(meets, actorId, actorState);
			Collection<Action<Boolean>> toCheck = new ArrayList<Action<Boolean>>();
			toCheck.add(meets);
			final String tempFinal=temp;
			callback callback = new callback() {
				
				public void call() {
					if (tempProm.get() && ((CoursePrivateState)myPool.getActors().get(tempFinal)).addStudent(actorId))
					{
						if (gradesList==null)
							((StudentPrivateState)actorState).getGrades().put(tempFinal, null);
						else
							((StudentPrivateState)actorState).getGrades().put(tempFinal, gradesList.get(0));
						complete(true);
					}
					else
					{
						coursesList.remove(0);
						if (gradesList!=null)
						gradesList.remove(0);
						start();
					}
				}
			};
			then(toCheck, callback);
		}
		else
		{
			actorState.addRecord("Register With Prefences");
			complete(false);
		}
	}
	

}
