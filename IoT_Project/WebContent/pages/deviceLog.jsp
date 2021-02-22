<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="IoT_Project.DBConnection"%>
<%@ page import="java.sql.*"%>
<%request.setCharacterEncoding("utf-8"); %>

<% 
	int id = Integer.parseInt(request.getParameter("deviceId"));
	
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style type="text/css">
	#logBox {
		text-align: center;
		line-height: 2.5em;
		border-collapse: collapse;
		width: 60%;
		margin: auto;
		border-bottom: 2px solid #d8d8d8;
	}
	
	#head {
	background-color: #c4ddfe;
	font-weight: bold;
}

</style>
</head>
<body>
<table id="logBox" border="0">
		<tr id="head">
			<td width="350">측정시간</td>
			<td width="350">위치</td>
			<td width="200">상태정보</td>
			<td width="200">측정값</td>
		</tr>
	<%	
		DBConnection db = new DBConnection();
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			/* 전체 로그 조회, 측정 시간 오름차순 */
			String query = 
				"SELECT status.time, device.position, status.action, status.sensor_data " +
				"FROM device, status " +
				"WHERE device.id=? AND status.device_id=? " +
				"ORDER BY status.time asc";
			
			pstmt = con.prepareStatement(query);
			pstmt.setInt(1, id);
			pstmt.setInt(2, id);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				Timestamp time = rs.getTimestamp("time");
				String position = rs.getString("position");
				String doorState = rs.getString("action");
				int sensorData = rs.getInt("sensor_data");
			
		%>
		<tr>
			<% if (doorState.equals("열림")) {%>
				<td width="350" style="color: red"><%=time %></td>
				<td width="350" style="color: red"><%=position %></td>
				<td width="200" style="color: red"><%=doorState %></td>
				<td width="200" style="color: red"><%=sensorData %></td>
			<%} if (doorState.equals("닫힘")) {%>
				<td width="350"><%=time %></td>
				<td width="350"><%=position %></td>
				<td width="200"><%=doorState %></td>
				<td width="200"><%=sensorData %></td>
			<%} %>
		</tr>
		
		<%
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		%>
	</table>
	
</body>
</html>