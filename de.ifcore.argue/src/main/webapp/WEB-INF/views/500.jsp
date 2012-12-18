<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="argue" uri="http://www.if-core.de/tags/argue" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title><spring:message code="error.500.title" /> - <spring:message code="applicationName" /></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div id="content">
		<div class="page-header">
			<h1><spring:message code="error.500.title" /></h1>
		</div>
		
		<p><spring:message code="error.500.1" /></p>
		<p><spring:message code="error.500.2" /></p>
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
</div>
</body>
</html>