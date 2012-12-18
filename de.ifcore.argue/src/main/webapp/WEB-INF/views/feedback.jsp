<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
<title><spring:message code="feedback.title" /> - <spring:message code="applicationName" /></title>
</head>
<body>
	<div class="container">
		<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
		<div id="content">
			<div class="page-header">
				<h1><spring:message code="feedback.title" /></h1>
			</div>
			
			<div class="row">
				<div class="span7">
					<p><spring:message code="feedback.intro.1" /></p>
					<p><spring:message code="feedback.intro.2" /></p>
					<br />
					<%@ include file="/WEB-INF/jspf/errorMessage.jspf"%>
					<p><spring:message code="feedback.form.title" /></p>
					<form action='<spring:url value="/feedback" />' method="POST">
						<textarea name="text" class="span6 feedback"></textarea>
						<label class="checkbox">
							<input type="checkbox" name="grantPublication" value="1" />
							<spring:message code="feedback.form.grantPublication" />
						</label>
						<br />
						
						<button type="submit" class="btn btn-success">
							<i class="icon-ok icon-white"></i>
							<spring:message code="feedback.form.submit" />
						</button>
					</form>
				</div>
				<div class="span5">
					<div class="well">
						<jsp:include page="/WEB-INF/help/${helpLocale}/feedback/letusknow.jspf" />
					</div>
				</div>
			</div>
		</div>
		<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
		<script type="text/javascript">
			$(document).ready(function () {
				$('textarea.feedback').focus();
				$('button').blockDoubleClick();
				$('a.email').convertEmail({source: 'title'});
			});
		</script>
	</div>
</body>
</html>
