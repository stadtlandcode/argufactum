<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="argue" uri="http://www.if-core.de/tags/argue" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title><spring:message code="category.topics.title" arguments="${category.name}" /> - <spring:message code="applicationName" /></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div id="content">
		<%@ include file="/WEB-INF/jspf/successMessage.jspf"%>
		
		<div class="page-header">
			<h1><spring:message code="category.topics.title" arguments="${category.name}" /></h1>
		</div>
		
		<div class="row">
			<tags:topicList topics="${topics}" boxClass="span9" />
		</div>
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	<%@ include file="/WEB-INF/jspf/jqueryTimeAgo.jspf"%>
	<script type="text/javascript">
		$(document).ready(function () {
			$('abbr.timeago').timeago();
		});
	</script>
</div>
</body>
</html>