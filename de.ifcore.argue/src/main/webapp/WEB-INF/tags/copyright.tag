<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="license" required="true" description="license of document" %>
<%@ attribute name="editable" required="true" type="java.lang.Boolean" description="is the document editable?" %>
<%@ attribute name="urlOfOriginal" required="false" type="java.lang.Object" description="instance of TopicUrl" %>
<c:if test="${urlOfOriginal != null || license == 'CC'}">
	<br />
</c:if>
<c:if test="${urlOfOriginal != null}">
	<p class="copy-notice">
		<spring:message code="topic.copied" />
		<a href='<spring:url value="${urlOfOriginal}" />'><spring:message code="topic.copied.linkToOriginal" /></a>
	</p>
</c:if>
<c:if test="${license == 'CC'}">
	<p class="copyright">
		<spring:message code="copyright.cc" />
		<c:if test="${editable}">
			<spring:message code="copyright.cc.editorNotice" />
		</c:if>
	</p>
</c:if>