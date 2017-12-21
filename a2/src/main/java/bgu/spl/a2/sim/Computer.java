package bgu.spl.a2.sim;

import java.util.List;
import java.util.Map;

public class Computer {

	String computerType;
	long failSig;
	long successSig;
	SuspendingMutex myMutex;
	
	public Computer(String computerType) {
		this.computerType = computerType;
		myMutex= new SuspendingMutex(this);
	}
	
	public SuspendingMutex getMutex ()
	{
		return myMutex;
	}
	
	public void setFail (long fail)
	{
		this.failSig=fail;
	}
	
	public void setSuccess (long success)
	{
		this.successSig=success;
	}
	
	public String getType ()
	{
		return computerType;
	}

	
	public long checkAndSign(List<String> courses, Map<String, Integer> coursesGrades){
		int counter=0;
		for (String temp : courses)
		{
			if (coursesGrades.get(temp)!=null && coursesGrades.get(temp).intValue()>=56)
				counter++;
		}
		if (counter==courses.size())
			return successSig;
		else
			return failSig;
	}
}
