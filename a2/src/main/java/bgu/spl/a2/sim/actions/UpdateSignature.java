package bgu.spl.a2.sim.actions;

import bgu.spl.a2.Action;
import bgu.spl.a2.sim.privateStates.StudentPrivateState;

public class UpdateSignature extends Action<Boolean> {

	
	private long signature;
	
	public UpdateSignature(long newSig)
	{
		setActionName("Update Signature");
		signature=newSig;
	}
	
	public void start ()
	{
		((StudentPrivateState)actorState).setSignature(signature);
		complete(true);
	}
}
