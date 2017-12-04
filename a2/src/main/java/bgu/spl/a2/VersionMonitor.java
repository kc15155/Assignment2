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

    public void inc() {
        _version++;
    }

    public void await(int version) throws InterruptedException {
        while (_version==version);
        throw new InterruptedException("Versions don't match");
    }
}
