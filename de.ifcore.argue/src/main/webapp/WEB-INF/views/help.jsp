<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
<title><spring:message code="help.title" /> - <spring:message code="applicationName" /></title>
</head>
<body data-spy="scroll" data-target=".subnav" data-offset="60">
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div id="content" class="text-content">
		<div class="page-header">
			<h1><spring:message code="help.title" /></h1>
		</div>
		<jsp:include page="/WEB-INF/help/${helpLocale}/help/nav.jspf" />

		<jsp:include page="/WEB-INF/help/${helpLocale}/help/text.jspf" />
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	<script type="text/javascript">
		$(document).ready(function () {
			argue.subnav.init();
		});
	</script>
</div>
</body>
</html>