<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ attribute name="topicThesis" required="true" description="TopicThesis (see accordant enum)" %>
<%@ attribute name="topicThesisMessageCode" required="true" description="messageCode for TopicThesis" %>
<%@ attribute name="arguments" required="true" type="java.lang.Object" description="instances of CreateArgumentModel" %>
<%@ attribute name="relevances" required="true" type="java.lang.Object" description="relevances (ascending)" %>
<div class="span6 show-on-hover-trigger ${topicThesis}">
	<h2><spring:message code="heading.${topicThesisMessageCode}" /></h2>
	
	<ul class="argument-list unstyled">
		<c:set var="number" value="0" />
		<c:forEach items="${arguments}" var="argument" varStatus="nestedLoopVar">
			<c:if test="${argument.topicThesis == topicThesis}">
				<c:set var="number" value="${number + 1}" />
				<tags:argumentBox jsTemplate="false" number="${number}" argument="${argument}" relevances="${relevances}" />
			</c:if>
		</c:forEach>
	</ul>
</div>