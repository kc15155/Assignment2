package bgu.spl.a2;

import java.util.List;

public abstract class PrivateState {
	
	private List<String> history;

	public List<String> getLogger(){
		return history;
	}
	

	public void addRecord(String actionName){
		history.add(actionName);
	}
	
	
}
