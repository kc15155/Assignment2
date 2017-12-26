package bgu.spl.a2;

public class VersionMonitor {
	
	private volatile int _version;
	
	public VersionMonitor()
	{
		this._version=0;
	}
	
    public int getVersion() {
       return _version;
    }

    synchronized public void inc() {
        _version++;
        notifyAll();
    }

    synchronized public void await(int version) throws InterruptedException {
        while (_version==version)
        { 
        	wait();
       	}
        throw new InterruptedException();
    }
}
