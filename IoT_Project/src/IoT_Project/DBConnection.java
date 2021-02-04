package IoT_Project;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

public class DBConnection {
	private Connection con = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;
	
	public DBConnection() {
		String jdbcUrl = "jdbc:mysql://localhost:3306/iotDB?serverTimezone=Asia/Seoul";
		String dbId = "ks";
		String dbPass = "ks";
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		try {
			con = DriverManager.getConnection(jdbcUrl, dbId, dbPass);
			System.out.println("데이터베이스와 연결되었습니다. " + "(DB ID: " + dbId +")");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* device 테이블 insert 쿼리 */
	public int insertDevice(String macAddr) {
		int id = -1;
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String query = "insert into device(mac_addr, reg_date) values (?, ?)";

		try {
			pstmt = con.prepareStatement(query); 
			pstmt.setString(1, macAddr);
			pstmt.setTimestamp(2, timestamp);
			pstmt.executeUpdate();
			
			System.out.println("데이터베이스에 데이터가 저장되었습니다. (Table: device)");
			System.out.println("(정보)");
			System.out.println("MAC Address: " + macAddr);
			
			// 등록 후 디바이스 id 반환
			query = "select id from device where mac_addr = ?";
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, macAddr);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				id = rs.getInt("id");
				System.out.println("Device ID: " + id);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			return id;
		}
	}
	
	/* status 테이블 insert 쿼리 */
	public void insertStatus(int deviceId, String action, int sensorData) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis()); // 임시
		// 디바이스 id, 열림/닫힘정보, 센서데이터, 감지시간
		String query = "insert into status(device_id, action, sensor_data, time) values(?, ?, ?, ?)";
		
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, deviceId);
			pstmt.setString(2, action);
			pstmt.setInt(3, sensorData);
			pstmt.setTimestamp(4, timestamp); // 임시
			pstmt.executeUpdate();
			
			System.out.println("데이터베이스에 데이터가 저장되었습니다. (Table: status)");
			System.out.println("(정보)");
			System.out.println("Device ID: " + deviceId);
			System.out.println("Action: " + action);
			System.out.println("Sensor Data: " + sensorData);
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* device 테이블 위치 수정 쿼리 */
	public void update(int deviceId, String position) {
		String query = "update device set position=? where id=?";
		
		try {
			pstmt = con.prepareStatement(query);
			pstmt.setString(1, position);
			pstmt.setInt(2, deviceId);
			pstmt.executeUpdate(); 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/* DB연결종료 메소드 */
	public void connectionClose() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public Connection getConnection() {
		return con;
	}
}
