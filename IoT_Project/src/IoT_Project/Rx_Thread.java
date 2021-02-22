package IoT_Project;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.locks.ReentrantLock;

public class Rx_Thread extends Thread {
	private Socket socket;
	private Device device;
	private TCO tco;
	private BufferedInputStream bis = null;
	private int devId = 0;
	ReentrantLock lock = new ReentrantLock();
	private DBConnection db = new DBConnection();
	private long heartTimePrev, heartTimeCurrent;
	
	public Rx_Thread(TCO tco, Socket socket, Device device) {
		this.socket = socket;
		this.device = device;
		this.tco = tco;
		devId = device.getId();
		heartTimePrev = heartTimeCurrent = System.currentTimeMillis();
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		System.out.println("Rx_Thread start[devId]");

		try {
			bis = new BufferedInputStream(socket.getInputStream());
			byte[] buff = new byte[1024];
			int read = 0;

			while (true) {
				if (this.socket == null) {
					System.out.println("Socket null");
					break;
				}
				
				if(socket.isClosed()) {
					System.out.println("[Rx" + devId + "] Socket closed");
					break;
				}
				
				//HeartBeat Check
				heartTimeCurrent = System.currentTimeMillis();
				if( (heartTimeCurrent - heartTimePrev)/1000 > 31) {
					System.out.println("[Rx" + devId + "] HeartBeat arrested");
					lock.lock();
					try {
	            	  sleep(1);
	                  tco.setTco(-1);
					} catch(InterruptedException e) {
					} finally {
	                  lock.unlock();
					}
					socket.close();
					break;
				}
				
				try {
				read = bis.read(buff, 0, 1024);
				}
				catch(Exception e ){
					continue;
				}
				if (read < 0) {
					break;
				}

				byte messageOP;
				// System.arraycopy(buff, 0, tempArr, 0, 1);
				messageOP = buff[0];

				// op3 -> sensing value
				if (messageOP == 3) {
					ByteBuffer tempBuffer;
					byte[] tempMac = new byte[6];
					System.arraycopy(buff, 1, tempMac, 0, 6);
					// Mac일치하는지 확인해야하는가?

					byte[] tempId = new byte[4];
					System.arraycopy(buff, 8, tempId, 0, 4);
					tempBuffer = ByteBuffer.wrap(tempId);
					int id = tempBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
					// ID일치하는지 확인해야하는가?

					byte tempAction = buff[12];
					System.out.println("[" + devId + "] Action : " + tempAction);

					byte[] tempData = new byte[4];
					tempBuffer.clear();
					System.arraycopy(buff, 16, tempData, 0, 4);
					tempBuffer = ByteBuffer.wrap(tempData);
					int data = tempBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
					System.out.println("[RX" + devId + "]Data : " + data);

					// TODO : Insert into StatusTable
					System.out.println("[RX" + devId + "]데이터 수신");
					System.out.println("[RX" + devId + "]Device ID: " + id);
					System.out.println("[RX" + devId + "]Action Code: " + tempAction);
					System.out.println("[RX" + devId + "]Data: " + data);
					
					if(device.getId() == id) {
						if(tempAction == 1) { // action: 1 -> 열림
							db.insertStatus(id, "열림", data);
							System.out.println("[RX" + devId + "]status테이블에 정보가 저장되었습니다.(열림)");
						}
						
						else if(tempAction == 0) { // action: 0 -> 닫힘
							db.insertStatus(id, "닫힘", data);
							System.out.println("[RX" + devId + "]status테이블에 정보가 저장되었습니다.(닫힘)");
						}
						
					}
//                  ///////////호출방식///////////////////////////////////
//                  lock.lock();
//               try {
//                  tco.setTco(4);
//                  System.out.println("RX : tcb = 4");
//               }
//               finally {
//                  lock.unlock();
//               }
//               //notifyAll();
//                  /////////////////////////////////////////////////

				}
				if (messageOP == 7) {
					heartTimePrev = heartTimeCurrent;
					lock.lock();
					try {
	            	  sleep(1);
	                  tco.setTco(8);
					} catch(InterruptedException e) {
					} finally {
	                  lock.unlock();
					}
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("[RX" + devId + "] Thread EXIT");
	}

	
	
	

}
