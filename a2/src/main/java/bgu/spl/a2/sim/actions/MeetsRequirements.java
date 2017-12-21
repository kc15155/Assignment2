package bgu.spl.a2.sim.actions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class MeetsRequirements extends Action<Boolean>{
	
	private List<String> courses;
	
	public MeetsRequirements(List<String> coursesList)
	{
		setActionName("Meets Requirements");
		courses=coursesList;
	}
	
	public void start ()
	{
		boolean meetsReq=true;
		StudentPrivateState me = ((StudentPrivateState)actorState);
		for (String temp : courses)
		{
			if (!me.getGrades().containsKey(temp))
			{
				meetsReq=false;
			}
		}
		actorState.addRecord("Meets Requirements");
		complete(meetsReq);
	}

}
