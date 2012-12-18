<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="argue" uri="http://www.if-core.de/tags/argue" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title><tags:searchTitle searchTerm="${searchForm.searchTerm}" /> - <spring:message code="applicationName" /></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div id="content">
		<%@ include file="/WEB-INF/jspf/successMessage.jspf"%>
		
		<div class="page-header" id="home-page-header">
			<h1><tags:searchTitle searchTerm="${searchForm.searchTerm}" /></h1>
		</div>
		
		<form:form modelAttribute="searchForm" method="GET" action="/search" class="error-form">
			<%@ include file="/WEB-INF/jspf/errorList.jspf" %>
		</form:form>
		<c:if test="${searchResult != null}">
			<div class="row">
				<tags:topicList topics="${searchResult.topics}" boxClass="span9" />
			</div>
		</c:if>
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	<%@ include file="/WEB-INF/jspf/jqueryTimeAgo.jspf"%>
	<script type="text/javascript">
		$(document).ready(function () {
			$('abbr.timeago').timeago();
			$('.navbar-search input').focus();
		});
	</script>
</div>
</body>
</html>