<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf" %>
	<title><spring:message code="registration.title" /> - <spring:message code="applicationName" /></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf" %>
	<div id="content">
		<div class="page-header">
			<h1><spring:message code="registration.title" /></h1>
		</div>

		<form:form modelAttribute="userForeignAuthenticationRegistrationForm" method="POST" class="inline">
			<form:hidden path="authenticationUuid" />
			<%@ include file="/WEB-INF/jspf/errorList.jspf" %>
			
			<label for="username"><spring:message code="registration.username" /></label>
			<c:set var="dataOriginalTitle"><spring:message code="registration.username.popover.title" /></c:set>
			<c:set var="dataContent"><spring:message code="registration.username.popover.content" /></c:set>
			<form:input tabindex="1" path="username" cssClass="has-popover" data-original-title="${dataOriginalTitle}" data-content="${dataContent}" />
			<span class="help-line"><a href='<spring:url value="/login" />'>bereits registriert?</a></span>
			
			<label for="email"><spring:message code="registration.email" /></label>
			<form:input tabindex="2" path="email" cssClass="text" />
			
			<br /><br />
			<button type="submit" class="btn btn-success">
				<i class="icon-ok icon-white"></i>
				<spring:message code="registration.submit" />
			</button>
		</form:form>
		
		<form action='<spring:url value="/registration/deleteForeignAuthentication" />' method="GET" class="inline">
			<button type="submit" class="btn btn-danger">
				<i class="icon-remove icon-white"></i>
				<spring:message code="registration.cancel" />
			</button>
		</form>
		<br /><br />

		<p><spring:message code="registration.foreignAuthentication.privacy" /></p>
		<%@ include file="/WEB-INF/jspf/registration/privacy.jspf" %>
		<br /><br />
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	<script type="text/javascript">
		$(document).ready(function () {
			$('input.has-popover').initPopover();
			$('button').blockDoubleClick();
			$('#username').focus();
		});
	</script>
</div>
</body>
</html>