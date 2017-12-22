package bgu.spl.a2.sim.actions;

import java.util.List;

import bgu.spl.a2.Action;
import bgu.spl.a2.Promise;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.Computer;
import bgu.spl.a2.sim.Warehouse;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class AdministrativeCheck extends Action<Boolean> {
	
	private List <String> students;
	private String type;
	private List<String> conditions;
	
	public AdministrativeCheck(List<String> studentsList, String compType, List<String> conditions)
	{
		setActionName("Administrative Check");
		students=studentsList;
		type=compType;
		this.conditions=conditions;
	}
	
	public void start ()
	{
		final Promise<Computer> tempProm=Warehouse.getInstance().acquire(type);
		callback newCall = new callback() {
			
			public void call() {
				for (String temp : students)
				{
					StudentPrivateState tempStudent =  ((StudentPrivateState) myPool.getActors().get(temp));
					long newSignature=tempProm.get().checkAndSign(conditions,tempStudent.getGrades());
					tempStudent.setSignature(newSignature);
				}
				Warehouse.getInstance().release(tempProm.get());
				complete(true);
			}
		};
		tempProm.subscribe(newCall);
		
	}

}
