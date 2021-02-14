package IoT_Project;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.sql.*;
import java.util.ArrayList;

public class IoT_Server {
	public static final int PORT = 12344;
 	private static ArrayList<ThreadController> tcList = new ArrayList<ThreadController>();
 	
 	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ServerSocket serverSock = null;
		
		try {
			//Server Socekt 생성
			serverSock = new ServerSocket(PORT);
			System.out.println("Server for Arduino Started");
			
			//Accept (클라이언트로부터 연결요청 대기)
			while(true) {
				Socket socket = serverSock.accept();
				System.out.printf("Arduino is conneted(IP: %s, Port: %d)\n",
						socket.getInetAddress(), socket.getPort());
				
				Device device = new Device();
				
				BufferedInputStream bis = null;
				bis = new BufferedInputStream(socket.getInputStream());
				byte[] buff = new byte[1024];
				int read = 0;
				
				while(true) {
					if(socket == null) {
						break;
					}
					
					read = bis.read(buff, 0, 1024);
					
					if(read < 0) {
						break;
					}
					
					byte opCode;
					//System.arraycopy(buff, 0, tempArr, 0, 1);
					opCode = buff[0];
					
					//Op 1 : REQ_ID {op[1], mac[6],...}
					if(opCode == (byte)1) {
						byte[] tempMac = new byte[6];
		            	System.arraycopy(buff, 1, tempMac, 0, 6);
		            	
		            	String mac = byteToHex(tempMac);
	            		ByteBuffer sendByteBuffer = null;
		    			sendByteBuffer = ByteBuffer.allocate(20); //op 1 + mac 6 + '\0' + id 4 + action 1 + Data 4
		    			sendByteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		    			
		    			
		            	//TODO : SELECT ID FROM DEVICE where mac = (this.mac)
		    			
		    			DBConnection db = new DBConnection();
		    			Connection con = db.getConnection();
		    			PreparedStatement pstmt = null;
		    			ResultSet rs = null;
		    			
		    			String query = "select id from device where mac_addr=?";
		    			
		    			try {
		    				pstmt = con.prepareStatement(query);
		    				pstmt.setString(1, mac);
		    				rs = pstmt.executeQuery();
		    				
		    				if(rs.next()) { // 이미 디바이스가 등록된 경우
		    					device.setMac(tempMac);
		    					int id = rs.getInt("id");
		    					device.setId(id);
		    					System.out.println("MAC : "+ mac + "은 이미 등록된 단말기입니다. (ID : " + id + " )");
		    				
		 		    		}
		    				
		    				else { // 새로운 디바이스 등록
		    					device.setMac(tempMac);
		    					int deviceId = db.insertDevice(mac); // device 정보 insert
		    					db.insertStatus(deviceId, "닫힘", 0); // status 정보(초기상태) insert
		    					device.setId(deviceId);
		    					System.out.println("MAC : "+ mac + "이 등록되었습니다. (ID : " + deviceId + " )");
		    				}
		    				
		    			} catch(SQLException e) {
		    				e.printStackTrace();
		    			}
		    			
		    			sendByteBuffer.put((byte)2); //op = 2
		    			sendByteBuffer.put(device.getMac()); //mac
		    			sendByteBuffer.put((byte)0);
		    			sendByteBuffer.putInt(device.getId()); //id
		            	
		    			OutputStream os = socket.getOutputStream();
		    			os.write(sendByteBuffer.array());
		    			os.flush();
		    			
		    			TCO tco = new TCO();	//thread control bit
		    			Rx_Thread rx_Thread = new Rx_Thread(tco, socket, device);
						Tx_Thread tx_Thread = new Tx_Thread(tco, socket, device);
						
						tcList.add(new ThreadController(device.getId(), tco, rx_Thread, tx_Thread));
						
						rx_Thread.start();
						tx_Thread.start();
		    			
		    			break;
					}
					//op != 1
					else {
						System.out.printf("Disconneted(IP: %s, Port: %d)\n",
								socket.getInetAddress(), socket.getPort());
						socket.close();
						break;
					}
				}	
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void log(ByteBuffer buf)
	{
		System.out.println(buf.position() + " ~ " + buf.limit() + " [" + new String(buf.array()) + "]");
	}		
	
	
	public static String byteToHex(byte[] b) {
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
	
	// 방법 1
	public static void sendSignal(int deviceId) {
		for(ThreadController tc : tcList) {
			if(tc.getDeviceId() == deviceId) {
				// id값이 같으면 tx 쓰레드 실행
				// --> tcList 비어있다고 뜸(참조 불가능)
			}
		}
		
	}
	/*
	// 방법 2 파일입출력으로 tcList 저장하기
	// --> Socket은 파일입출력으로 저장 불가
	public static void sendSignal(int deviceId, int index) {
		try {
			fileInputStream = new FileInputStream("/Users/lim/eclipse-workspace/IoT_Project/src/IoT_Project/tcList");
			objectInputStream = new ObjectInputStream(fileInputStream);
			
			ArrayList<ThreadController> list = (ArrayList<ThreadController>)objectInputStream.readObject();
			
			for(ThreadController tc : list) {
				System.out.println("tc device id: " + tc.getDeviceId());
				if(tc.getDeviceId() == deviceId) {
					tc.getTco().setTco(index);
					System.out.println("변경완료");
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	*/
}


