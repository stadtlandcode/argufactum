<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ attribute name="facts" required="true" type="java.lang.Object" description="instances of class CreateFactModel" %>
<%@ attribute name="type" required="true" description="FactType (see enum)" %>
<%@ attribute name="relevances" required="true" type="java.lang.Object" description="relevances (ascending)" %>
<div class="span6 show-on-hover-trigger ${type}">
	<h2><spring:message code="facts.${type}" /></h2>
	
	<ul class="fact-list unstyled">
		<c:set var="number" value="0" />
		<c:forEach items="${facts}" var="fact" varStatus="loopVar">
			<c:if test="${fact.type == type}">
				<c:set var="number" value="${number + 1}" />
				<tags:factBox jsTemplate="false" fact="${fact}" number="${number}" relevances="${relevances}" />
			</c:if>
		</c:forEach>
	</ul>
</div>