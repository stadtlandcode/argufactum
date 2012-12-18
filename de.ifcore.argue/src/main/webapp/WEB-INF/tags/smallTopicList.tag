<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="argue" uri="http://www.if-core.de/tags/argue"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ attribute name="category" required="true" type="java.lang.Object" description="instance of class CategoryTopicsModel" %>
<div class="span6">
	<h3><a href='<argue:categoryUrl category="${category}" />'>${category.name}</a> <small><spring:message code="home.category.numberOfTopics" arguments="${category.totalNumberOfTopics}" /></small></h3>
	<ul class="topic-list topic-list-small unstyled">
		<c:forEach items="${category.topics}" var="topic">
			<li class="topic-term">
				<a href='<argue:topicUrl topic="${topic}" />'><c:out value="${topic.term}" /></a>
				<tags:creationLog creationLog="${topic.creationLog}" modificationLog="${topic.modificationLog}" />
			</li>
		</c:forEach>
	</ul>
</div>