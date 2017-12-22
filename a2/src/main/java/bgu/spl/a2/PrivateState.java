package bgu.spl.a2;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public abstract class PrivateState implements Serializable{
	
	private List<String> history;
	
	public PrivateState(){
		history = new LinkedList<String>();
	}

	public List<String> getLogger(){
		return history;
	}
	

	public void addRecord(String actionName){
		history.add(actionName);
	}
	
	
}
