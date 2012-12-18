<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
<title><spring:message code="thankyou.title" /> - <spring:message code="applicationName" /></title>
</head>
<body>
	<div class="container">
		<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
		<div id="content">
			<%@ include file="/WEB-INF/jspf/successMessage.jspf"%>
		
			<div class="page-header">
				<h1><spring:message code="thankyou.title" /></h1>
			</div>
			
			<p><spring:message code="thankyou.1" /></p>
		</div>
		<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	</div>
</body>
</html>
		