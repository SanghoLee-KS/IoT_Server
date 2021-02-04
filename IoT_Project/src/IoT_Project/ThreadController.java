package IoT_Project;

public class ThreadController {
	private int deviceId;
	private TCO tco; //Thread Control Object
	private Thread txThread;
	private Thread rxThread;
	
	public ThreadController(int deviceId, TCO tco, Thread txThread, Thread rxThread) {
		this.deviceId = deviceId;
		this.tco = tco;
		this.txThread = txThread;
		this.rxThread = rxThread;
	}
	
	public int getDeviceId() {
		return deviceId;
	}
}
