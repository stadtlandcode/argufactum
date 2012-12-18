<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="argue" uri="http://www.if-core.de/tags/argue"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ attribute name="topic" required="true" type="java.lang.Object" description="instance of class TopicModel" %>
<%@ attribute name="number" required="true" description="number of Topic" %>
<li class="box topic show-on-hover-trigger">
	<div class="stats-box">
		<div class="stats-box-main">${number}.</div>
		<div class="stats-box-rating">
			<div class="rating-bar">
				<div class="rating-bar-like" style="width:${topic.upVoteBarPercent}%"></div>
				<div class="rating-bar-dislike" style="width:${topic.downVoteBarPercent}%"></div>
			</div>
		</div>
		<div class="stats-box-secondary stats-box-secondary${topic.numberOfMainEntities}" title='<spring:message code="argument.stats.facts.confirmative.full" arguments="${topic.numberOfMainEntities}" />'>
			<a href='<argue:topicUrl topic="${topic}" />' class="ajax-link">
				${topic.numberOfMainEntities} ${topic.labelOfMainEntities}
			</a>
		</div>
		<div class="stats-box-secondary stats-box-secondary${topic.numberOfSecondaryEntities}" title='<spring:message code="argument.stats.facts.confirmative.full" arguments="${topic.numberOfSecondaryEntities}" />'>
			<a href='<argue:topicUrl topic="${topic}" />' class="ajax-link">
				${topic.numberOfSecondaryEntities} ${topic.labelOfSecondaryEntities}
			</a>
		</div>
	</div>

	<div class="box-content">
		<div class="topic-term">
			<a href='<argue:topicUrl topic="${topic}" />'><c:out value="${topic.term}" /></a>
			<span class="category">${topic.category.name}</span>
		</div>
		<p class="definition"><c:out value="${topic.definition}" /></p>
		<tags:creationLog creationLog="${topic.creationLog}" modificationLog="${topic.modificationLog}" />
	</div>
	
	<div class="views show-on-hover"><spring:message code="home.topic.numberOfViews" arguments="${topic.numberOfViews}" /></div>
</li>