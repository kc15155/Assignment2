package bgu.spl.a2.sim.actions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
				Collection <Action<Boolean>> allUpdates = new ArrayList<>();
				for (String temp : students)
				{
					StudentPrivateState tempStudent =  ((StudentPrivateState) myPool.getPrivateState(temp));
					HashMap<String, Integer> conditionGrades = new HashMap<>();
					for (String tempCourse : conditions)
					{
						conditionGrades.put(tempCourse, tempStudent.getGrade(tempCourse));
					}
					long newSignature=tempProm.get().checkAndSign(conditions,conditionGrades);
					UpdateSignature update = new UpdateSignature(newSignature);
					sendMessage(update, temp, tempStudent);
					allUpdates.add(update);
				}
				Warehouse.getInstance().release(tempProm.get().getType());
				then(allUpdates, new callback() {
					
					public void call() {
						complete(true);
					}
				});
			}
		};
		tempProm.subscribe(newCall);
		
	}

}

