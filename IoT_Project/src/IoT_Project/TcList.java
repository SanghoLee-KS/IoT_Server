package IoT_Project;

import java.net.Socket;
import java.util.ArrayList;

public class TcList {
	private static TcList tcList = null;
	public static ArrayList<ThreadController> list = new ArrayList<ThreadController>();
	
	private TcList() {}
	
	// 싱글톤 패턴
	public static TcList getInstance() {
		if(tcList == null) {
			System.out.println("tcList 객체생성");
			tcList = new TcList();
		}
		return tcList;
	}
	
	
	public void setTcList(int deviceId, TCO tco, Thread rxThread, Thread txThread, Socket socket) {
		list.add(new ThreadController(deviceId, tco, txThread, rxThread, socket));
	}
	
	public ArrayList<ThreadController> getTcList() {
		return list;
	}
	
	public void sendSignal(int deviceId, int index) {
		System.out.println("id: " + deviceId);
		System.out.println("index: " + index);
		for(ThreadController tc : list) {
			System.out.println("test 1: " + tc.getDeviceId());
		}
	}
	
}
