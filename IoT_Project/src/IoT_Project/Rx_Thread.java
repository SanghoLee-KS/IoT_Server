package IoT_Project;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.concurrent.locks.ReentrantLock;

public class Rx_Thread extends Thread{
	private Socket socket;
	private Device device;
	private TCO tco;
	private BufferedInputStream bis = null;	
	ReentrantLock lock = new ReentrantLock();
	
	public Rx_Thread(TCO tco, Socket socket, Device device) {
		this.socket = socket;
		this.device = device;
		this.tco  = tco;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		System.out.println("Rx_Thread start");

		
		
		
		
		try {
			bis = new BufferedInputStream(socket.getInputStream());
			byte[] buff = new byte[1024];
			int read = 0;
			
			while(true) {
				if(this.socket == null) {
					break;
				}
				
				read = bis.read(buff, 0, 1024);
				if(read < 0) {
					break;
				}
				
				byte messageOP;
				//System.arraycopy(buff, 0, tempArr, 0, 1);
				messageOP = buff[0];
				
				//op3 -> sensing value
				if(messageOP == 3) {
					ByteBuffer	tempBuffer;
					byte[] tempMac = new byte[6];
	            	System.arraycopy(buff, 1, tempMac, 0, 6);
	            	//Mac일치하는지 확인해야하는가?
	            	
	            	
	            	byte[] tempId = new byte[4];
	            	System.arraycopy(buff,  8, tempId, 0, 4);
	            	tempBuffer = ByteBuffer.wrap(tempId);
	            	int id = tempBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
	            	//ID일치하는지 확인해야하는가?
	            	
	            	
	            	byte tempAction = buff[13];
	            	System.out.println("Action : "+ tempAction);
	            	
	            	byte[] tempData = new byte[4];
	            	tempBuffer.clear();
	            	System.arraycopy(buff, 17, tempData, 0, 4);
	            	tempBuffer = ByteBuffer.wrap(tempData);
	            	int data = tempBuffer.order(ByteOrder.LITTLE_ENDIAN).getInt();
	            	System.out.println("Data : "+ data);
	            	
	            	//TODO : Insert into StatusTable
	            	
//	            	///////////호출방식///////////////////////////////////
//	            	lock.lock();
//					try {
//						tco.setTco(4);
//						System.out.println("RX : tcb = 4");
//					}
//					finally {
//						lock.unlock();
//					}
//					//notifyAll();
//	            	/////////////////////////////////////////////////
					
				}
		            	
		            	
				
				
				
				
				
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	
	
	
	public String byteToHex(byte[] b) {
        StringBuilder sb = new StringBuilder();
       for (int i = 5; i>=0; i--) {
	       	if(b[i] < 16) {
	       		System.out.print("0");
	       	}
	       	//System.out.print(b[i]);
	       	sb.append(String.format("%02x", b[i]&0xff));
	       	if (i > 0) {
	       		sb.append(String.format(":"));
	       	}
       	
       }

       return sb.toString();
   }
	
	
}
