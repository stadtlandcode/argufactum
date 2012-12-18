<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="title" required="true" description="title of stats-fragment (current total-relevance)" %>
<%@ attribute name="relevances" required="true" type="java.lang.Object" description="relevances (ascending)" %>
<%@ attribute name="jsTemplate" required="true" type="java.lang.Boolean" description="output for JavaScript-Template?" %>
<%@ attribute name="numericValue" required="false" type="java.lang.Object" description="numeric mean of given relevance votes" %>
<div class="stats-box-relevance star-rating-read" title="${title}">
	<c:forEach items="${relevances}" var="relevance">
		<div data-star-relevance="${relevance}" class='star-rating star-rating-readonly<c:if test="${!jsTemplate && numericValue >= relevance.byteValue}"> star-rating-on</c:if>'><a></a></div>
	</c:forEach>
</div>