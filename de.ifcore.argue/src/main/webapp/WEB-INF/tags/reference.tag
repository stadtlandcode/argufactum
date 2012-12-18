<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="reference" required="true" type="java.lang.Object" description="instance of class ReferenceModel" %>
<%@ attribute name="jsTemplate" required="true" type="java.lang.Boolean" description="output for JavaScript-Template?" %>
<span data-reference-id="${reference.id}" title="${reference.creationLog.dateTimeLocalized} <spring:message code="reference.author" arguments="${reference.creationLog.authorName}" htmlEscape="true" />">
	<c:choose>
		<c:when test="${jsTemplate}">
			{{if url}}<a href="{{= url}}" target="_blank" rel="nofollow">{{= text}}</a>
			{{else}}{{= text}}
			{{/if}}
		</c:when>
		<c:otherwise>
			<c:choose>
				<c:when test="${reference.url != null}"><a href="${reference.url}" target="_blank" rel="nofollow">${reference.text}</a></c:when>
				<c:otherwise>${reference.text}</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>
</span>
