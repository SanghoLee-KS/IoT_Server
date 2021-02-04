package IoT_Project;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.locks.ReentrantLock;

public class Tx_Thread extends Thread {
	private Socket socket;
	private Device device;
	private TCO tco;
	private OutputStream os = null;
	ReentrantLock lock = new ReentrantLock();
	
	public Tx_Thread(TCO tco, Socket socket, Device device) {
		this.socket = socket;
		this.device = device;
		this.tco = tco;
	}
	
	@Override
	public void run() {

		super.run();
		System.out.println("Tx_Thread start");
	
		try {
			os= socket.getOutputStream();
		}catch (Exception e) {
			e.printStackTrace();
		}
			ByteBuffer sendByteBuffer = null;
			sendByteBuffer = ByteBuffer.allocate(20); //op 1 + mac 6 + '\0' + id 4 + action 1 + '\0' 3 + Data 4
			sendByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
			
		while(true) {
			if(this.socket == null) {
				System.out.println("Tx : socket disabled");
				break;
			}
	
			try {
				sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
					
			if( (tco.getTco()) == 4) { 
				System.out.println("TX : case 4 start");
				/* 제어 메시지 전송 */
				sendByteBuffer.put((byte)4);
				
				sendByteBuffer.put(device.getMac());
				//sendByteBuffer.put(new byte[6-device.getMac().length]);
				
				sendByteBuffer.put((byte)0);
				
				sendByteBuffer.putInt(device.getId());

				//action
				//sendByteBuffer.put(?);
				
				//data
				//sendByteBuffer.putInt();
				
				///////////////////////////////////////////////
				sendByteBuffer.put((byte)5);
				sendByteBuffer.put((byte)0);
				sendByteBuffer.put((byte)0);
				sendByteBuffer.put((byte)0);
				
				sendByteBuffer.putInt(6);
				///////////////////////////////////////////////
				
				System.out.println(sendByteBuffer.array());
				
				try {
					os.write(sendByteBuffer.array());
					os.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				sendByteBuffer.clear();
				
				lock.lock();
				try {
					tco.setTco(0);
				}
				finally {
					lock.unlock();
				}
				
			}
				
			else if ( (tco.getTco()) == 10 ) { 
				//TODO: 종료&초기화 메시지 차이점 찾기
				//종료 : socket.close(); close(); Rx->
				
				//TODO: DELETE * FROM Devcie where id = (device.id) DISTINCT(?);
				
				sendByteBuffer.put((byte)10);
				
				sendByteBuffer.put(device.getMac());
				//sendByteBuffer.put(new byte[6-device.getMac().length]);
				
				sendByteBuffer.put((byte)0);
				
				sendByteBuffer.putInt(device.getId());
				
				
				System.out.println(sendByteBuffer.array());
				try {
					os.write(sendByteBuffer.array());
					os.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				sendByteBuffer.clear();
				
				/* 초기화 메시지 전송 */
				lock.lock();
				try {
					tco.setTco(0);
				}
				finally {
					lock.unlock();
				}
			}
				
			else {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
					//wait(); //Thread 자원낭비를 막기 위해 txThread는 필요할 때만 깨울 예정.
					//TODO: 버튼 클릭하면 이 함수 호출시켜주세요. notifyAll();
			}
			
		}
		
		 
        System.out.println("While Out");
		
		
		
	}
}