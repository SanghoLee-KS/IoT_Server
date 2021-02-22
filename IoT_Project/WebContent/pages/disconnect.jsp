<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="IoT_Project.DBConnection"%>
<%@ page import="IoT_Project.IoT_Server" %>
<%@ page import="IoT_Project.ThreadController" %>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.ArrayList"%>

<% request.setCharacterEncoding("utf-8"); %>

<% 
	
	// tcList 받아서 동일한 deviceid 찾아서 리무브
	// tx_thread -> setTco 10번으로 설정
	
	DBConnection db = new DBConnection();
	int id = Integer.parseInt(request.getParameter("deviceId")); 
	ArrayList<ThreadController> tcList = IoT_Server.getTcList();
	
	for(int i=0; i<tcList.size(); i++) {
		if(tcList.get(i).getDeviceId() == id) {
			System.out.println("디바이스 삭제 완료 (Device ID: " + tcList.get(i).getDeviceId() + ")");
			tcList.get(i).getTco().setTco(10);
			tcList.remove(i);
			
			// status 먼저 지우고 device 지워야함
			db.deleteStatus(id);
			db.deleteDevice(id);
			db.connectionClose();
		}
	}
	
%>

<%	
	
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
<script type="text/javascript">
	window.self.close();
</script>
</body>
</html>