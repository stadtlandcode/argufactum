<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<%@ attribute name="fact" required="true" type="java.lang.Object" description="instance of class CreateFactModel" %>
<%@ attribute name="number" required="true" description="number of this argument" %>
<%@ attribute name="relevances" required="true" type="java.lang.Object" description="relevances (ascending)" %>
<%@ attribute name="jsTemplate" required="true" type="java.lang.Boolean" description="output for JavaScript-Template?" %>
<li class="fact box show-on-hover-trigger" data-fact-id="${fact.id}" data-text-id="${fact.textId}" data-relevance="${fact.relevance.value}" data-user-relevance="${fact.personalRelevance}" data-timestamp="${fact.creationLog.timestamp}">
	<div class="stats-box">
		<div class="stats-box-main">${number}.</div>
		<tags:relevanceStats jsTemplate="${jsTemplate}" relevances="${relevances}" title="${fact.relevance.label}" numericValue="${fact.relevance.numericValue}" />
		<div class="stats-box-secondary stats-box-secondary${fact.numberOfReferences}">
			<tags:singularPluralMessage messageCode="fact.stats.references" number="${fact.numberOfReferences}" jsTemplate="${jsTemplate}" numberAttrName="numberOfReferences" />
		</div>
	</div>

	<div class="box-content">
		<span class="box-text"><c:out value="${fact.text}" /></span>
		
		<tags:creationLog creationLog="${fact.creationLog}" attribution="${fact.attribution}" modificationLog="${fact.modificationLog}" jsTemplate="${jsTemplate}" />

		<p class="references">
			<spring:message code="references" />:
			<c:choose>
				<c:when test="${jsTemplate}">
					{{each references}}
						<tags:reference reference="${reference}" jsTemplate="true" />
					{{/each}}
				</c:when>
				<c:otherwise>
					<c:forEach items="${fact.references}" var="reference">
						<tags:reference reference="${reference}" jsTemplate="false" />
					</c:forEach>
				</c:otherwise>
			</c:choose>
		</p>
	</div>
</li>