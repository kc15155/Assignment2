/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bgu.spl.a2.sim;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;


import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import bgu.spl.a2.Action;
import bgu.spl.a2.ActorThreadPool;
import bgu.spl.a2.PrivateState;
import bgu.spl.a2.callback;
import bgu.spl.a2.sim.actions.AddSpaces;
import bgu.spl.a2.sim.actions.AddStudent;
import bgu.spl.a2.sim.actions.AdministrativeCheck;
import bgu.spl.a2.sim.actions.CloseCourse;
import bgu.spl.a2.sim.actions.OpenCourse;
import bgu.spl.a2.sim.actions.ParticipateInCourse;
import bgu.spl.a2.sim.actions.RegisterWithPreferences;
import bgu.spl.a2.sim.actions.Unregister;
import bgu.spl.a2.sim.json.Input;
import bgu.spl.a2.sim.json.JsonComputer;
import bgu.spl.a2.sim.json.Phase1AndActions;
import bgu.spl.a2.sim.privateStates.CoursePrivateState;
import bgu.spl.a2.sim.privateStates.DepartmentPrivateState;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;


public class Simulator {
	
	private static CountDownLatch tracker;	
	public static ActorThreadPool actorThreadPool;
	
	
    public static void start(){
		actorThreadPool.start();
    }
	
    
	public static void attachActorThreadPool(ActorThreadPool myActorThreadPool){
		actorThreadPool=myActorThreadPool;
	}
	

	public static HashMap<String,PrivateState> end(){
		try {
			actorThreadPool.shutdown();
		} catch (InterruptedException e) { }
		return  (HashMap<String, PrivateState>) actorThreadPool.getActors();
	}
	
	
	public static void main(String [] args){
		

		try
		{
		Gson gson = new Gson();
		JsonReader reader = new JsonReader(new FileReader(args[0]));
		Input myInput = gson.fromJson(reader, Input.class);
		
		attachActorThreadPool(new ActorThreadPool(myInput.getThreads().intValue()));
		for (JsonComputer tempComp : myInput.getComputers())
		{
			Warehouse.getInstance().addComputer(tempComp.getType(), Long.parseLong(tempComp.getSigSuccess()), Long.parseLong(tempComp.getSigFail()));
			
		}
		

		start();
		
		tracker = new CountDownLatch(myInput.getPhase1().size());
		if (tracker.getCount()!=0)
		{
			for (Phase1AndActions tempAction : myInput.getPhase1())
				{
					submitAction(tempAction);
				}
			try {
				tracker.await();
				System.out.println("End Phase 1");
				tracker = new CountDownLatch(myInput.getPhase2().size());
				if (tracker.getCount()!=0)
				{
					for (Phase1AndActions tempAction : myInput.getPhase2())
					{
						submitAction(tempAction);
					}
					try {
						tracker.await();
						System.out.println("End Phase 2");
						tracker=new CountDownLatch(myInput.getPhase3().size());
						if (tracker.getCount()!=0)
						{
							for (Phase1AndActions tempAction : myInput.getPhase3())
							{
								submitAction(tempAction);
							}
							try {
								tracker.await();
								System.out.println("End Phase 3");
								HashMap<String, PrivateState> result = end();
								FileOutputStream fout = new FileOutputStream("result.ser");
								try {
									ObjectOutputStream oos = new ObjectOutputStream(fout);
									oos.writeObject(result);
								} catch (IOException exc1) {
									exc1.printStackTrace();
								}
								
							} catch (InterruptedException exc2) {
								exc2.printStackTrace();
							}
						}
						else {
							end();
						}
					} catch (InterruptedException exc3) {
						exc3.printStackTrace();
					}
				}
				else {
					end();
				}
			} catch (InterruptedException exc4) {
				exc4.printStackTrace();
			}
			 
		}
		else {
			end();
		}
		
		}
		catch (FileNotFoundException exc5)
		{
			exc5.printStackTrace();
		}

	}
	
	private static void submitAction (Phase1AndActions toAdd)
	{
		boolean added=false;
		Action newAction = null;
		String actorId = null;
		PrivateState actorstate = null;
		if (toAdd.getAction().equals("Open Course"))
		{
			newAction = new OpenCourse(Integer.parseInt(toAdd.getSpace()), toAdd.getPrerequisites(), toAdd.getCourse());
			actorId=toAdd.getDepartment();
			actorstate= actorThreadPool.getPrivateState(actorId);
			if (actorstate==null)
				actorstate=new DepartmentPrivateState();
			added=true;
			
		}
		
		if (toAdd.getAction().equals("Add Student"))
		{
			newAction = new AddStudent(toAdd.getStudent());
			actorId=toAdd.getDepartment();
			actorstate=actorThreadPool.getPrivateState(actorId);
			if (actorstate==null)
				actorstate = new DepartmentPrivateState();
			added=true;
		}
		
		if (toAdd.getAction().equals("Add Spaces"))
		{
			newAction = new AddSpaces(Integer.parseInt(toAdd.getNumber()));
			actorId=toAdd.getCourse();
			actorstate=actorThreadPool.getPrivateState(actorId);
			if (actorstate==null)
				actorstate=new CoursePrivateState();
			added=true;
		
		}
		
		if (toAdd.getAction().equals("Administrative Check"))
		{
			newAction=new AdministrativeCheck(toAdd.getStudents(), toAdd.getComputer(), toAdd.getConditions());
			actorId=toAdd.getDepartment();
			actorstate=actorThreadPool.getPrivateState(actorId);
			if (actorstate==null)
				actorstate=new DepartmentPrivateState();
			added=true;
		}
		
		if (toAdd.getAction().equals("Close Course"))
		{
			newAction = new CloseCourse(toAdd.getCourse());
			actorId=toAdd.getDepartment();
			actorstate=actorThreadPool.getPrivateState(actorId);
			if (actorstate==null)
				actorstate=new DepartmentPrivateState();
			added=true;
		}
		
		if (toAdd.getAction().equals("Participate In Course"))
		{
			if (toAdd.getGrade().get(0).equals("-"))
			newAction = new ParticipateInCourse(toAdd.getStudent(), -1);
			else 
			newAction = new ParticipateInCourse(toAdd.getStudent(), Integer.parseInt(toAdd.getGrade().get(0)));
			actorId=toAdd.getCourse();
			actorstate=actorThreadPool.getPrivateState(actorId);
			if (actorstate==null)
				actorstate=new CoursePrivateState();
			added=true;
		}
			
		if (toAdd.getAction().equals("Unregister"))
		{
			newAction=new Unregister(toAdd.getStudent());
			actorId=toAdd.getCourse();
			actorstate=actorThreadPool.getPrivateState(actorId);
			if (actorstate==null)
				actorstate=new CoursePrivateState();
			added=true;
			
		}
		
		if (toAdd.getAction().equals("Register With Preferences"))
		{
			List<Integer> studentGrades = new ArrayList<Integer>();
			for (String temp : toAdd.getGrade())
			{
				if (temp.equals("-"))
					studentGrades.add(-1);
					else {
						studentGrades.add(Integer.parseInt(temp));
					}
			}
			newAction=new RegisterWithPreferences(toAdd.getPreferences(), studentGrades);
			actorId=toAdd.getStudent();
			actorstate=actorThreadPool.getPrivateState(actorId);
			if (actorstate==null)
				actorstate=new StudentPrivateState();
			added=true;
		}
		
		if (!added)
		{
			System.out.println(toAdd.getAction()+"??????????????????");
			throw new IllegalArgumentException("Illegal Action");
		}
		newAction.getResult().subscribe(new callback() {
			
			public void call() {
				tracker.countDown();
			}
		});
		actorThreadPool.submit(newAction, actorId, actorstate);
		
	}
}
