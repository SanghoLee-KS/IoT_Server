<%@page import="java.awt.Window"%>
<%@page import="IoT_Project.DBConnection"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.sql.*"%>

<%
	
	DBConnection db = new DBConnection();
	Connection con = db.getConnection();
	PreparedStatement pstmt = null;
	ResultSet rs = null;

%>

<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="web-css.css" />
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script type="text/javascript">

</script>
</head>
<body>
	<table id="logBox" border="0">
		<tr>
			<td width="350">측정시간</td>
			<td width="350">위치</td>
			<td width="200">상태정보</td>
			<td width="200">측정값</td>
		</tr>
		<%
		try {
			/* 전체 로그 조회, 측정 시간 오름차순 */
			String query = 
				"SELECT status.time, device.position, status.action, status.sensor_data " +
				"FROM device, status " +
				"WHERE device.id = status.device_id " +
				"ORDER BY status.time asc";
			
			pstmt = con.prepareStatement(query);
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