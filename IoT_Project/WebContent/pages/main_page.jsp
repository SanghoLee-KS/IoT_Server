<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<html>
<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="web-css.css" />

<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script>
	$(document).ready(function() {
		$("#devices").load("deviceTable.jsp");
		$("#log").load("logTable.jsp");
		
		function updateTables() {
			// ajax --> 필요한 정보만 업데이트 하여 처리비용감소 목적
			$("#devices").load("deviceTable.jsp");
			$("#log").load("logTable.jsp");	
		}
		
		setInterval(updateTables, 2000); // 2초에 한번씩 정보 불러오기
	});
	
</script>

<title>침입 감지 시스템</title>
</head>
<body>
	<p style="font-size: 20px; margin-top: 30px">관리자 인터페이스</p>
	<div id="devices"></div>
	
	<p style="font-size: 18px; ">전체 로그 정보</p>
	<div id="log"></div>

</body>
</html>