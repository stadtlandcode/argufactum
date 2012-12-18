<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="messageCode" required="true" description="messageCode for spring:message tag" %>
<%@ attribute name="number" required="false" description="number to display" %>
<%@ attribute name="numberAttrName" required="false"description="name of attribute who contains the number to display - only required when jsTemplate is true" %>
<%@ attribute name="jsTemplate" required="false" type="java.lang.Boolean" description="output for JavaScript-Template?" %>
<c:choose>
	<c:when test="${jsTemplate}">
		{{if ${numberAttrName} == 1}}
			<spring:message code="${messageCode}.1" arguments="{{= ${numberAttrName}}}" />
		{{else}}
			<spring:message code="${messageCode}" arguments="{{= ${numberAttrName}}}" />
		{{/if}}
	</c:when>
	<c:otherwise>
		<c:choose>
			<c:when test="${number == 1}"><spring:message code="${messageCode}.1" arguments="${number}" /></c:when>
			<c:otherwise><spring:message code="${messageCode}" arguments="${number}" /></c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>