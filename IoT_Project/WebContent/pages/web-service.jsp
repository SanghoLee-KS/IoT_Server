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
	
	int idCheck = 0;

%>

<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="web-css.css" />
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script type="text/javascript">
	/* '수정' 버튼 이벤트 */
	var tempId;
	function revise(id) {
		window.open('revise.jsp', '수정', 'width=800px,height=400px');
		tempId = id;
	}
	
	function getId() {
		return tempId;
	}
	
	// 일정시간마다 DBConnection의 isUpdated 변수 확인
	
	setTimeout(function () {
	<%
	
	
	%>
	
	}, 3000);
	
	

</script>
<title>침입 감지 시스템</title>
</head>
<body>
	<p style="font-size: 20px; margin-top: 30px">관리자 인터페이스</p>
	<table id="deviceTable" border="0" style="margin-bottom: 100px">
		<tr id="head">
			<td width="200">디바이스ID</td>
			<td width="200">위치</td>
			<td width="200">상태정보</td>
			<td width="200">측정시간</td>
			<td width="200">환경설정</td>
		</tr>

		<%
		
		try {
			/* 가져오기 - 디바이스id, 디바이스 맥주소, 디바이스 위치정보, 문 상태정보, 디바이스 등록일 */
			
			/* 최근 문의 상태정보(열림/닫힘)를 가져오기 위해 status 테이블에 중복된 데이터가 있으면 
			 * 가장 최근의 데이터를 가지고 온다.
			 * 가장 최근 데이터 -> status 테이블에서 id값이 가장 높은 데이터
			 */
			 String query = 
				"SELECT device.id, device.position, status.id, status.action, status.time " +
				"FROM device, status " +
				"WHERE device.id = status.device_id " +
				"AND status.id in (SELECT max(status.id) FROM status GROUP BY status.device_id)";
			 
			 // 쿼리문 실행			
			pstmt = con.prepareStatement(query);
			rs = pstmt.executeQuery();
			
			// 데이터 가져오기
			while (rs.next()) { 
				int statusId = rs.getInt("status.id");  
				int id = rs.getInt("device.id");
				String position = rs.getString("position");
				String doorState = rs.getString("action");
				Timestamp time = rs.getTimestamp("time");
				
				
				
		%>
		<tr id="data">
			<td width="200"><%=id%></td>
			<td id="position<%=id%>"width="200"><%=position%></td>
			<% if (doorState.equals("열림")) {%>
				<td width="200" style="color: red">
					<%=doorState%><br>
					<form action="signal.jsp" target="_blank" method="post">
						<input type="text" value=<%=id%> name="deviceId" style="display: none;" readonly>
						<input type="submit" value="경고신호">	
					</form>
				</td>
				
			<%} if (doorState.equals("닫힘")) {%>
				<td width="200" style="color: green"><%=doorState%></td>
			<%} %>
			<td width="200"><%=time %></td>
			<td width="200">
				<form action="deviceLog.jsp" target="_blank" method="post">
					<input type="text" value="<%=id%>" name="deviceId" style="display: none;" readonly>
					<button type="button" id=<%=id%> onclick="revise(this.id)">수정</button>
					<input type="submit" value="로그확인">
				</form>
			</td>	
		</tr>
		<%
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		%>
	</table>
	
	<p style="font-size: 18px; ">전체 로그 정보</p>
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