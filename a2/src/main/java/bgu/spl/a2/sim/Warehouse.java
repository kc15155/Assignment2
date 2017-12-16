package bgu.spl.a2.sim;

import java.util.ArrayList;

import bgu.spl.a2.Promise;


public class Warehouse {
	
	private ArrayList<Computer> compList;
	
	public Warehouse()
	{
		compList = new ArrayList<Computer>();
	}
	
	public void addComputer (String type, long suc, long fail)
	{
		Computer toAdd = new Computer(type);
		toAdd.setFail(fail);
		toAdd.setSuccess(suc);
		compList.add(toAdd);
	}
	
	public Promise<Computer> acquire (String compType)
	{
		for (Computer temp : compList)
		{
			if (temp.getType()==compType)
				return temp.getMutex().down(temp.getType());
		}
		return null;

	}
	
	public static Warehouse getInstance() {
        return WarehouseHolder.instance;
    }
	
	private static class WarehouseHolder {
        private static Warehouse instance = new Warehouse();
    }
	
}
