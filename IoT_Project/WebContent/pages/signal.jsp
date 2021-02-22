<%@page import="IoT_Project.TcList"%>
<%@page import="javax.swing.event.TableColumnModelListener"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="IoT_Project.DBConnection"%>
<%@ page import="IoT_Project.IoT_Server" %>
<%@ page import="IoT_Project.ThreadController"%>
<%@ page import="java.sql.*"%>
<%@ page import="java.util.ArrayList"%>

<% request.setCharacterEncoding("utf-8"); %>

<% int id = Integer.parseInt(request.getParameter("deviceId")); %>

<%	
	
	IoT_Server server = IoT_Server.getInstance();
	server.sendSignal(id); // 얻은 device id(제어 신호를 보내고 싶은 디바이스 id)
							   // server로 전송

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