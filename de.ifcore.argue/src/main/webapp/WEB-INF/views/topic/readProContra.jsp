<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title><%@ include file="/WEB-INF/jspf/topic/title.jspf"%></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div class="content topic-content" data-topic-id="${topic.id}">
		<%@ include file="/WEB-INF/jspf/topic/bodyHeader.jspf"%>
		<%@ include file="/WEB-INF/jspf/topic/proContraBodyContent.jspf"%>
		<%@ include file="/WEB-INF/jspf/topic/comments.jspf"%>
	</div>
	<tags:copyright editable="${editable}" license="${topic.license}" urlOfOriginal="${topic.urlOfOriginal}" />
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>

	<c:set var="viewName" value="proContra" />
	<%@ include file="/WEB-INF/jspf/topic/proContraBodyFooter.jspf"%>
</div>
</body>
</html>