<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title><%@ include file="/WEB-INF/jspf/topic/title.jspf"%></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div class="content topic-content">
		<%@ include file="/WEB-INF/jspf/topic/bodyHeader.jspf"%>
		<%@ include file="/WEB-INF/jspf/topic/comparisonBodyContent.jspf"%>
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	<%@ include file="/WEB-INF/jspf/jqueryTimeAgo.jspf"%>
	<script src="<spring:url value="/js/jquery.ui.core.js" />" type="text/javascript"></script>
	<script src="<spring:url value="/js/jquery.ui.widget.js" />" type="text/javascript"></script>
	<script src="<spring:url value="/js/jquery.ui.mouse.js" />" type="text/javascript"></script>
	<script src="<spring:url value="/js/jquery.ui.slider.js" />" type="text/javascript"></script>
	<script src="<spring:url value="/js/argue.comparison.js" />" type="text/javascript"></script>
	<script type="text/javascript">
		$(document).ready(function () {
			argue.comparison.init(${topic.id}, '<spring:url value="/" />');
		});
	</script>
</div>
</body>
</html>