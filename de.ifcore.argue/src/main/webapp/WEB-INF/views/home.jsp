<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="argue" uri="http://www.if-core.de/tags/argue" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title><spring:message code="home.title" /> - <spring:message code="applicationName" /></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div id="content">
		<%@ include file="/WEB-INF/jspf/successMessage.jspf"%>
		
		<div class="page-header" id="home-page-header">
			<h1><spring:message code="home.title.greeting" /></h1>
			<div class="well">
				<a class="close" data-dismiss="alert">×</a>
				<jsp:include page="/WEB-INF/help/${helpLocale}/home/greeting.jspf" />
				<p><em><spring:message code="home.betaNotice" /></em></p>
			</div>
		</div>
		
		<div class="row">
			<tags:topicList messageCodeOfTitle="home.selectedTopics" topics="${selectedTopics}" boxClass="span6" />
			<tags:topicList messageCodeOfTitle="home.latestTopics" topics="${latestTopics}" boxClass="span6" />
		</div>
		
		<div class="categories-topic-list">
			<h2><spring:message code="home.popularTopics" /></h2>
			
			<div class="row">
				<c:forEach items="${topicsByCategory}" var="category" varStatus="status">
					<tags:smallTopicList category="${category}" />
				</c:forEach>
			</div>
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