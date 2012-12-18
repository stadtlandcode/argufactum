<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="argue" uri="http://www.if-core.de/tags/argue"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ attribute name="topics" required="true" type="java.lang.Object" description="instances of class TopicModel" %>
<%@ attribute name="messageCodeOfTitle" required="false" description="messageCode for h2 title" %>
<%@ attribute name="boxClass" required="true" description="css class for div container" %>
<div class="${boxClass}">
	<c:if test="${messageCodeOfTitle != null}">
		<h2><spring:message code="${messageCodeOfTitle}" /></h2>
	</c:if>
	<ul class="topic-list topic-list-medium">
		<c:forEach items="${topics}" var="topic" varStatus="status">
			<tags:topicBox topic="${topic}" number="${status.count}" />
		</c:forEach>
	</ul>
</div>