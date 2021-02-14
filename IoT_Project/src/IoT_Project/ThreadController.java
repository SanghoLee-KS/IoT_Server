package IoT_Project;

public class ThreadController{
	private int deviceId;
	private TCO tco; //Thread Control Object
	private Thread rx_Thread;
	private Thread tx_Thread;
	
	public ThreadController(int deviceId, TCO tco, Thread rx_Thread, Thread tx_Thread) {
		this.deviceId = deviceId;
		this.tco = tco;
		this.tx_Thread = rx_Thread;
		this.rx_Thread = tx_Thread;
	}
	
	public int getDeviceId() {
		return deviceId;
	}
	
	public TCO getTco() {
		return tco;
	}
}
