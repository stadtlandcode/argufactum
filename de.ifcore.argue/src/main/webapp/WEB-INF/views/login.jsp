<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title><spring:message code="login.title" /> - <spring:message code="applicationName" /></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div id="content">
		<div class="page-header">
			<h1><spring:message code="login.title" /></h1>
		</div>
		<%@ include file="/WEB-INF/jspf/errorMessage.jspf"%>

		<c:if test="${not empty SPRING_SECURITY_LAST_EXCEPTION.message}">
			<div class="alert alert-error">
				<c:out value="${SPRING_SECURITY_LAST_EXCEPTION.message}"/>
			</div>
		</c:if>
		
		<div class="row">
			<div class="span5">
				<form action="<spring:url value="/login/process" />" method="POST">
					<input type="hidden" name="_spring_security_remember_me" value="1" />
				
					<label for="login-username"><spring:message code="login.username" /></label>
					<input type="text" id="login-username" name="username" tabindex="1" />
					<span class="help-inline">
						<a href='<spring:url value="/registration/usernamePassword" />'>
							<spring:message code="login.registrationLinkText" />
						</a>
					</span>
					
					<label for="login-password"><spring:message code="login.password" /></label>
					<input type="password" id="login-password" name="password" tabindex="2" />
					<span class="help-inline">
						<!-- <a href='<spring:url value="/forgotPassword" />' class="supplemental-line"><spring:message code="login.forgotPassword" /></a>  -->
					</span>
					
					<br /><br />
					<button tabindex="3" type="submit" class="btn btn-success">
						<i class="icon-ok icon-white"></i>
						<spring:message code="login.submit" />
					</button>
				</form>
			</div>
			<div class="span7">
				<p><spring:message code="login.openId" /></p>
				<div class="foreign-auth-form-box">
					<form action="<spring:url value="/login/openid/process" />" method="POST">
						<button type="submit" class="btn-img auth-provider-button auth-provider-icon-google" name="openid_identifier" value="https://www.google.com/accounts/o8/id"></button> 
						<button type="submit" class="btn-img auth-provider-button auth-provider-icon-yahoo" name="openid_identifier" value="http://yahoo.com/"></button> 
					</form>
					<form action="<spring:url value="/signin/twitter"/>" method="POST">
						<button type="submit" class="btn-img auth-provider-button auth-provider-icon-twitter"></button>
					</form>
					<form action="<spring:url value="/signin/facebook"/>" method="POST">
						<input type="hidden" name="scope" value="email" />
						<button type="submit" class="btn-img auth-provider-button auth-provider-icon-facebook"></button>
					</form>
				</div>
			</div>
		</div>
		<br /><br />
		<h2><spring:message code="login.registration.title" /></h2>
		<br />
		<div class="row">
			<div class="span7">
				<p><spring:message code="login.registration.openId" /></p>
				<p><spring:message code="login.registration.usernamePassword" /></p>
				<br />
				<%@ include file="/WEB-INF/jspf/registration/usernamePasswordForm.jspf" %>
			</div>
			<div class="span5">
				<div class="well">
					<h3><spring:message code="registration.advantages.title" /></h3>
					<br />
					<jsp:include page="/WEB-INF/help/${helpLocale}/registration/advantages.jspf" />
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	<script type="text/javascript">
		$(document).ready(function () {
			$('.has-tooltip').tooltip();
			$('input.has-popover').initPopover();
			$('button').blockDoubleClick();
			$('#login-username').focus();
			$('#city').prev('label').andSelf().hide();
		});
	</script>
</div>
</body>
</html>