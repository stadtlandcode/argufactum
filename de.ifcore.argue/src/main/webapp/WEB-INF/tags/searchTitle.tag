<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ attribute name="searchTerm" required="true" %>
<c:choose>	
	<c:when test="${searchTerm == null || fn:length(searchTerm) < 1}"><spring:message code="search.title" arguments="-" htmlEscape="true" /></c:when>
	<c:otherwise><spring:message code="search.title" arguments="${searchForm.searchTerm}" htmlEscape="true" /></c:otherwise>
</c:choose>
