<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<div class="content topic-content" style="display:none;" data-topic-id="${topic.id}" data-page-title="<%@ include file="/WEB-INF/jspf/topic/title.jspf"%>">
	<%@ include file="/WEB-INF/jspf/topic/bodyHeader.jspf"%>
	<%@ include file="/WEB-INF/jspf/topic/proContraBodyContent.jspf"%>
</div>
