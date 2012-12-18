<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
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
		<div class="row">
			<div class="span8">
				<%@ include file="/WEB-INF/jspf/registration/usernamePasswordForm.jspf" %>
			</div>
			<div class="span4">
				<div class="well">
					<jsp:include page="/WEB-INF/help/${helpLocale}/registration/advantages.jspf" />
				</div>
			</div>
		</div>
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	<script type="text/javascript">
		$(document).ready(function () {
			$('input.has-popover').initPopover();
			$('button').blockDoubleClick();
			$('#username').focus();
			$('#city').prev('label').andSelf().hide();
		});
	</script>
</div>
</body>
</html>