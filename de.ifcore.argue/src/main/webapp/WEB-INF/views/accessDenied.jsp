<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
<title><spring:message code="accessDenied.title" /> - <spring:message code="applicationName" /></title>
</head>
<body>
	<div class="container">
		<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
		<div id="content">
			<div class="page-header">
				<h1><spring:message code="accessDenied.title" /></h1>
			</div>
			
			<p><spring:message code="accessDenied.1" /></p>
			<p><spring:message code="accessDenied.2" /></p>
		</div>
		<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	</div>
</body>
</html>
		