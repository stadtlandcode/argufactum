<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf" %>
	<title><spring:message code="forgotPassword.title" /> - <spring:message code="applicationName" /></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf" %>
	<div id="content">
		<div class="page-header">
			<h1><spring:message code="forgotPassword.title" /></h1>
		</div>
		
		<form:form modelAttribute="forgotPasswordForm" method="POST">
			<%@ include file="/WEB-INF/jspf/errorList.jspf" %>
			<label for="email"><spring:message code="forgotPassword.email" /></label>
			<form:input tabindex="1" id="email" path="email" cssClass="text" />
			
			<br /><br />
			<button tabindex="2" type="submit" class="btn btn-success">
				<i class="icon-ok icon-white"></i>
				<spring:message code="forgotPassword.submit" />
			</button>
		</form:form>
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	<script type="text/javascript">
		$(document).ready(function () {
			$('button').blockDoubleClick();
			$('#email').focus();
		});
	</script>
</div>
</body>
</html>