<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="IoT_Project.DBConnection"%>
<%request.setCharacterEncoding("utf-8"); %>

<% 
	int id = Integer.parseInt(request.getParameter("count"));
	String position = request.getParameter("position");
	   
   	DBConnection db = new DBConnection();
	db.update(id, position);
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