package IoT_Project;

import java.net.Socket;

public class ThreadController{
	private int deviceId;
	private TCO tco; //Thread Control Object
	private Thread rx_Thread;
	private Thread tx_Thread;
	private Socket socket;
	
	public ThreadController(int deviceId, TCO tco, Thread rx_Thread, Thread tx_Thread, Socket socket) {
		this.deviceId = deviceId;
		this.tco = tco;
		this.tx_Thread = rx_Thread;
		this.rx_Thread = tx_Thread;
		this.socket = socket;
	}
	
	public int getDeviceId() {
		return deviceId;
	}
	
	public TCO getTco() {
		return tco;
	}
	
	public Socket getSocket() {
		return socket;
	}
}
