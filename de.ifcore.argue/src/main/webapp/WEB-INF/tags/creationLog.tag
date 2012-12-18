<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ attribute name="creationLog" required="true" type="java.lang.Object" description="instance of class LogModel" %>
<%@ attribute name="attribution" required="false" type="java.lang.Object" description="instance of class AttributionModel" %>
<%@ attribute name="modificationLog" required="false" type="java.lang.Object" description="instance of class LogModel" %>
<%@ attribute name="jsTemplate" required="false" type="java.lang.Boolean" description="output for JavaScript-Template?" %>
<p class="author-note">
	<abbr class="timeago" title="${creationLog.dateTimeGmt}">${creationLog.dateTimeLocalized}</abbr>
	<spring:message code="argument.author" arguments="${creationLog.authorName}" htmlEscape="true" />
	<c:if test="${attribution != null && attribution.offerAttributionList}">
		<abbr class="attribution" title='<spring:message code="attribution.popover.title" /> <c:out value="${attribution.authors}" />'><spring:message code="attribution.link" /></abbr>
	</c:if>
	<c:if test="${modificationLog != null && !jsTemplate}">
		<tags:modificationLog modificationLog="${modificationLog}"></tags:modificationLog>
	</c:if>
</p>