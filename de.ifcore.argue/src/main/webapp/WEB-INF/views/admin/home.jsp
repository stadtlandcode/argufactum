<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="argue" uri="http://www.if-core.de/tags/argue" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title>Startpage - Admin - <spring:message code="applicationName" /></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div id="content">
		<div class="page-header" id="home-page-header">
			<h1>Startpage Administration</h1>
		</div>
		
		<form action='<spring:url value="/admin/home/updateSelectedTopics" />' method="POST">
			<button type="submit" class="btn">Update Selected Topics</button>
		</form>
		<br />
		
		<form action='<spring:url value="/admin/home/updateLatestTopics" />' method="POST">
			<button type="submit" class="btn">Update Latest Topics</button>
		</form>
		<br />
		
		<form action='<spring:url value="/admin/home/updateTopicsByCategory" />' method="POST">
			<button type="submit" class="btn">Update Topics by Category</button>
		</form>
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
</div>
</body>
</html>