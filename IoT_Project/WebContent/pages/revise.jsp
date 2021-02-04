<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ page import="IoT_Project.DBConnection"%>

<title>디바이스 설정</title>
<style type="text/css">
	html, body {
		text-align: center;
	}
	
</style>
<script type="text/javascript" src="js/jquery-3.5.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function() {
		$("#ok").click(function() {
			// 웹 위치 정보 바꾸기
			var count = opener.parent.getId();
			$("#count").val(count);
			
			var parentId = "position" + count;
			var text = $("#position").val();

			opener.document.getElementById(parentId).innerHTML = text;
		});
		
		$("#cancel").click(function() {
			window.self.close();
		});
	});
	
</script>
</head>
<body>
	<p style="color: gray;">위치 정보 수정</p>
	<form action="revisePro.jsp" method="post">
		<input type="text" id="position" name="position" placeholder="위치를 입력하세요.">
		<input type="text" id="count" name="count" style="display: none">
		<br><br>
		<button id="ok">확인</button>
		<button type="button" id="cancel">취소</button>
		<br><br><br><br><br>
		<p style="color: gray;">디바이스 초기화</p>
		<button id="reset">초기화</button>
	</form>
</body>
</html>