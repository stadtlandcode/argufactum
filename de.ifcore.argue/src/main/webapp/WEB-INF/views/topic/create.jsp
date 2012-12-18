<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<%@ include file="/WEB-INF/jspf/headContent.jspf"%>
	<title><spring:message code="header.createTopic" /> - <spring:message code="applicationName" /></title>
</head>
<body>
<div class="container">
	<%@ include file="/WEB-INF/jspf/bodyHeader.jspf"%>
	<div id="content">
		<div class="page-header">
			<h1><spring:message code="topic.new" /></h1>
		</div>
		<form:form modelAttribute="createTopicForm" method="POST" id="create-topic-form" cssClass="form-horizontal">
			<div class="control-group">
				<label class="control-label" for="topic-term"><spring:message code="topic.term" /></label>
				<div class="controls">
					<input tabindex="1" type="text" class="span6" name="term" id="topic-term" placeholder='<spring:message code="topic.firstTerm" />' maxlength="125">
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label" for="topic-definition"><spring:message code="topic.definition" /></label>
				<div class="controls">
					<input tabindex="5" type="text" class="span6" name="definition" id="topic-definition" placeholder='<spring:message code="topic.definition.placeholder" />' maxlength="255">
					<span class="help-inline">(<spring:message code="form.optional" />)</span>
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label"><spring:message code="topic.discussionType" /></label>
				<div class="controls">
					<label class="radio">
						<input tabindex="10" type="radio" name="discussionType" value="PRO_CONTRA" checked="checked" />
						<spring:message code="topic.discussionType.proContra" />
					</label> 
					<label class="radio">
						<input tabindex="11" type="radio" name="discussionType" value="COMPARISON" disabled="disabled" />
						<span class="disabled"><spring:message code="topic.discussionType.comparison" /></span> (<spring:message code="comingSoon" />)
					</label> 
				</div>
			</div>
			
			<div class="control-group">
				<label class="control-label"><spring:message code="topic.visibility" /></label>
				<div class="controls">
					<label class="checkbox">
						<input tabindex="21" type="checkbox" name="visibility" value="PUBLIC" />
						<spring:message code="topic.visibility.public" />
					</label>
					<div id="public-extra" style="display:none;">
						<label>
							<spring:message code="topic.category" />
							<select name="categoryId">
								<option value="0">---</option>
								<c:forEach items="${categories}" var="category">
									<option value="${category.id}">${category.name}</option>
								</c:forEach>
							</select>
							(<spring:message code="form.optional" />)
						</label>
						<br />
						<p><spring:message code="copyright.cc.creationNotice" /></p>
					</div>
				</div>
			</div>
			
			<div class="form-actions">
				<button class="btn btn-primary" type="submit" tabindex="30">
					<i class="icon-white icon-ok"></i>
					<spring:message code="button.save" />
				</button>
			</div>
		</form:form>
	</div>
	<%@ include file="/WEB-INF/jspf/bodyFooter.jspf"%>
	
	<sec:authorize ifNotGranted="ROLE_USER">
		<%@ include file="/WEB-INF/jspf/requiresLogin.jspf"%>
	</sec:authorize>
	
	<script type="text/javascript">
		$(document).ready(function () {
			$('button').blockDoubleClick();
			$('#topic-term').focus();
			$('#requires-login').addClass('modal').modal();
			$.fn.togglePublic = function() {
				return $('#public-extra').showConditional($(this).is(':checked'));
			};
			$('input[name="visibility"]').change(function() { $(this).togglePublic(); }).togglePublic();
		});
	</script>
</div>
</body>
</html>