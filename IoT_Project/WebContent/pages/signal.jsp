<%@page import="javax.swing.event.TableColumnModelListener"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="IoT_Project.DBConnection"%>
<%@ page import="IoT_Project.IoT_Server" %>
<%@ page import="IoT_Project.ThreadController"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.ArrayList"%>

<%request.setCharacterEncoding("utf-8"); %>

<% 
	int id = Integer.parseInt(request.getParameter("deviceId"));
%>

<%
	ArrayList<ThreadController> tcList = IoT_Server.getTcList();
	System.out.println(tcList.get(0));
	for(ThreadController tc : tcList) {
		System.out.println(tc);
		
	}

	int index;
	if((index = tcList.indexOf(id)) != -1) { // tcList에서 해당 디바이스 id 찾기
		System.out.println(tcList.get(index));
	}
	
	else {
		System.out.println("찾고자하는 디바이스가 tcList에 존재하지 않습니다.(Device ID: " + id + ")");
	}
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