<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="argue" uri="http://www.if-core.de/tags/argue"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ attribute name="argument" required="true" type="java.lang.Object" description="instance of class CreateArgumentModel" %>
<%@ attribute name="number" required="true" description="number of this argument" %>
<%@ attribute name="relevances" required="true" type="java.lang.Object" description="relevances (ascending)" %>
<%@ attribute name="jsTemplate" required="true" type="java.lang.Boolean" description="output for JavaScript-Template?" %>
<li class="argument box show-on-hover-trigger" data-argument-id="${argument.id}" data-text-id="${argument.textId}" data-relevance="${argument.relevance.value}" data-user-relevance="${argument.personalRelevance}" data-timestamp="${argument.creationLog.timestamp}">
	<div class="stats-box">
		<div class="stats-box-main">${number}.</div>
		<tags:relevanceStats jsTemplate="${jsTemplate}" relevances="${relevances}" title="${argument.relevance.label}" numericValue="${argument.relevance.numericValue}" />
		<div class="stats-box-secondary-split">
			<tags:argumentFactStats argument="${argument}" />
		</div>
	</div>
	
	<div class="box-content">
		<a href='<spring:url value="${argument.url}" />' class="box-text ajax-link"><c:out value="${argument.text}" /></a>
		
		<tags:creationLog creationLog="${argument.creationLog}" attribution="${argument.attribution}" modificationLog="${argument.modificationLog}" jsTemplate="${jsTemplate}" />
	</div>
</li>