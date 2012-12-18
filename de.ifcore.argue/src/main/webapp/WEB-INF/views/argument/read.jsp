<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="argue" uri="http://www.if-core.de/tags/argue"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title><%@ include file="/WEB-INF/jspf/argument/title.jspf"%></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div class="content argument-content" id="argument-content-${argument.id}" data-argument-id="${argument.id}">
		<%@ include file="/WEB-INF/jspf/argument/bodyContent.jspf"%>
	</div>
	<tags:copyright editable="${editable}" license="${topic.license}" />
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	
	<c:set var="viewName" value="argument" />
	<%@ include file="/WEB-INF/jspf/topic/proContraBodyFooter.jspf"%>
</div>
</body>
</html>