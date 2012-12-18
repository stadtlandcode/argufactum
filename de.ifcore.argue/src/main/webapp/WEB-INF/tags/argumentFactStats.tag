<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ attribute name="argument" required="true" type="java.lang.Object" description="instance of class CreateArgumentModel" %>
<div class="stats-box-secondary stats-box-secondary${argument.numberOfConfirmativeFacts}" title='<spring:message code="argument.stats.facts.confirmative.full" arguments="${argument.numberOfConfirmativeFacts}" />'>
	<a href='<spring:url value="${argument.url}" />' class="ajax-link">
		<spring:message code="argument.stats.facts.confirmative" arguments="${argument.numberOfConfirmativeFacts}" />
	</a>
</div>
<div class="stats-box-secondary stats-box-secondary${argument.numberOfDebilitativeFacts}" title='<spring:message code="argument.stats.facts.debilitative.full" arguments="${argument.numberOfDebilitativeFacts}" />'>
	<a href='<spring:url value="${argument.url}" />' class="ajax-link">
		<spring:message code="argument.stats.facts.debilitative" arguments="${argument.numberOfDebilitativeFacts}" />
	</a>
</div>