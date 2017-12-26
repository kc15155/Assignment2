package bgu.spl.a2.sim.actions;

import java.util.Iterator;
import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.callback;

public class RegisterWithPreferences extends Action<Boolean> {
	
	private List<String> coursesList;
	private List<Integer> gradesList;
	private boolean wasRegistered;
	Iterator<String> myIterator;
	Iterator<Integer> gradeIterator;
	
	public RegisterWithPreferences(List <String> courses, List <Integer> gradeList)
	{
		setActionName("Register With Prefences");
		this.coursesList = courses;
		this.gradesList = gradeList;
		wasRegistered=false;
		myIterator = coursesList.iterator();
		gradeIterator = gradeList.iterator();
	}
	
	public void start ()
	{
		if (myIterator.hasNext() && gradeIterator.hasNext())
		{
			String temp=myIterator.next();
			ParticipateInCourse tryRegister = new ParticipateInCourse(actorId, gradeIterator.next());
			sendMessage(tryRegister, temp, myPool.getPrivateState(temp)).subscribe(new callback() {
				
				@Override
				public void call() {
					if (tryRegister.getResult().get())
					{
						while (myIterator.hasNext())
							{myIterator.next();}
				       	wasRegistered=true;
				       	start();
					}
					else
					{
						start();
					}
				}
			});
		}
		else
		{
			if (wasRegistered)
			complete(true);
			else
				complete(false);
		}
	}
	

}
