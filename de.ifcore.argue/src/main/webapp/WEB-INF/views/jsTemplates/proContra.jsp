<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags"%>
<script type="text/x-jquery-tmpl" id="js-tmpl-argument">
	<tags:argumentBox jsTemplate="true" number="{{= number}}" argument="${argument}" relevances="${relevances}" />
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-argumentFactStat">
	<tags:argumentFactStats argument="${argument}" />
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-modificationLog">
	<tags:modificationLog modificationLog="${argument.modificationLog}"></tags:modificationLog>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-editArgueBox">
	<form action='<spring:url value="/{{= entityName}}/ajax" />' method="POST" class="edit-form">
		<input type="hidden" name="_method" value="PUT" />
		<input type="hidden" name="id" value="{{= id}}" />

		<textarea name="text" maxlength="255">{{= text}}</textarea>

		<br />
		<button type="submit" class="btn btn-success">
			<i class="icon-ok icon-white"></i>
			<spring:message code="button.save" />
		</button>

		<button type="button" class="btn btn-danger cancel-button">
			<i class="icon-remove icon-white"></i>
			<spring:message code="button.cancel" />
		</button>
	</form>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-argumentCreateForm">
	<form action='<spring:url value="/argument/ajax" />' method="POST" class="create-argument-form">
		<input type="hidden" name="topicId" value="{{= parentId}}" />
		<input type="hidden" name="topicThesis" value="{{= type}}" />
	
		<textarea tabindex="{{= textTabindex}}" name="thesis" class="span6" placeholder='<spring:message code="argument.thesis.placeholder" />' maxlength="255" />
		
		<br />
		<button tabindex="{{= submitButtonTabindex}}" type="submit" class="btn btn-success">
			<i class="icon-plus icon-white"></i>
			<spring:message code="argument.button.create" />
		</button>
	</form>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-argumentTools">
	<div class="box-tools show-on-hover">
		<form action='<spring:url value="/argument/vote/ajax" />' method="POST" class="stars star-rating-read">
			<input type="hidden" name="id" value="{{= id}}" />
			<input type="hidden" name="relevance" value="" />

			<c:forEach items="${relevancesReversed}" var="relevance">
				<div data-star-relevance="${relevance}" class="star-rating"><a title='<spring:message code="Relevance.argument.personal.${relevance}" />'></a></div>
			</c:forEach>
			<div data-relevance="" class="rating-cancel"><a title='<spring:message code="Relevance.argument.personal.remove" />'></a></div>
		</form>

		<div class="tool-buttons">
			<button type="button" class="btn edit-box-button" title='<spring:message code="argument.button.edit" />'>
				<i class="icon-pencil"></i>
			</button>
		
			<form action='<spring:url value="/argument/ajax" />' method="POST" class="delete-box-form">
				<input type="hidden" name="_method" value="DELETE" />
				<input type="hidden" name="id" value="{{= id}}" />
				
				<button type="button" class="btn" title='<spring:message code="argument.button.delete" />'>
					<i class="icon-trash"></i>
				</button>
			</form>

			<button type="button" class="btn box-history-button" title='<spring:message code="argument.button.history" />' data-history-url='<spring:url value="/argument/{{= id}}/history/ajax" />'>
				<i class="icon-time"></i>
			</button>
		</div>
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-argumentDeletedByAnotherUser">
	<div class="alert alert-error">
		<a class="close" data-dismiss="alert">x</a>
		<spring:message code="argument.edit.deletedByAnotherUser" />
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-factDeletedByAnotherUser">
	<div class="alert alert-error">
		<a class="close" data-dismiss="alert">x</a>
		<spring:message code="fact.edit.deletedByAnotherUser" />
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-argumentEditedByAnotherUser">
	<div class="alert alert-info">
		<a class="close" data-dismiss="alert">x</a>
		<spring:message code="argument.edit.editedByAnotherUser" arguments="{{= text}}" />
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-factEditedByAnotherUser">
	<div class="alert alert-info">
		<a class="close" data-dismiss="alert">x</a>
		<spring:message code="fact.edit.editedByAnotherUser" arguments="{{= text}}" />
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-factCreateForm">
	<form action='<spring:url value="/fact/ajax" />' method="POST" class="create-fact-form">
		<input type="hidden" name="argumentId" value="{{= parentId}}" />
		<input type="hidden" name="factType" value="{{= type}}" />
		
		{{if type == 'CONFIRMATIVE'}}
			<textarea tabindex="{{= textTabindex}}" name="text" class="span6" placeholder='<spring:message code="fact.text.placeholder" />' maxlength="255" />
		{{else}}
			<textarea tabindex="{{= textTabindex}}" name="text" class="span6" placeholder='<spring:message code="debilitation.text.placeholder" />' maxlength="255" />
		{{/if}}
		<label>
			<spring:message code="reference" />:
			<input type="text" name="reference" class="span4" tabindex="{{= textTabindex}}" placeholder='<spring:message code="reference.text.placeholder" />' maxlength="255" />
		</label>

		<button type="submit" class="btn btn-success" tabindex="{{= submitButtonTabindex}}">
			<i class="icon-plus icon-white"></i>
			{{if type == 'CONFIRMATIVE'}}
				<spring:message code="fact.button.create" />
			{{else}}
				<spring:message code="debilitation.button.create" />
			{{/if}}
		</button>
	</form>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-fact">
	<tags:factBox jsTemplate="true" fact="${fact}" number="{{= number}}" relevances="${relevances}" />
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-factTools">
	<div class="box-tools show-on-hover">
		<form action='<spring:url value="/fact/vote/ajax" />' method="POST" class="stars star-rating-read">
			<input type="hidden" name="id" value="{{= id}}" />
			<input type="hidden" name="relevance" value="" />
			<c:forEach items="${relevancesReversed}" var="relevance">
				<div data-star-relevance="${relevance}" class="star-rating"><a title='<spring:message code="Relevance.fact.personal.${relevance}" />'></a></div>
			</c:forEach>
			<div data-relevance="" class="rating-cancel"><a title='<spring:message code="Relevance.fact.personal.remove" />'></a></div>
		</form>

		<div class="tool-buttons">
			<button type="button" class="btn edit-box-button" title='<spring:message code="fact.button.edit" />'>
				<i class="icon-pencil"></i>
			</button>
		
			<form action='<spring:url value="/fact/ajax" />' method="POST" class="delete-box-form">
				<input type="hidden" name="_method" value="DELETE" />
				<input type="hidden" name="id" value="{{= id}}" />
				
				<button type="button" class="btn" title='<spring:message code="fact.button.delete" />'>
					<i class="icon-trash"></i>
				</button>
			</form>

			<button type="button" class="btn box-history-button" title='<spring:message code="fact.button.history" />' data-history-url='<spring:url value="/fact/{{= id}}/history/ajax" />'>
				<i class="icon-time"></i>
			</button>
		</div>
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-factReferenceStat">
	<div class="stats-box-secondary stats-box-secondary{{= numberOfReferences}}">
		<tags:singularPluralMessage messageCode="fact.stats.references" jsTemplate="true" numberAttrName="numberOfReferences" />
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-reference">
	<tags:reference reference="${reference}" jsTemplate="true" />
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-createReferenceLink">
	<span class="create-reference"><a class="action"><spring:message code="reference.create" /></a></span>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-deleteReferenceForm">
	<form action='<spring:url value="/reference/ajax" />' method="POST" class="delete-reference-form">
		<input type="hidden" name="_method" value="DELETE" />
		<input type="hidden" name="id" value="{{= referenceId}}" />
		<i class="icon-trash"></i>
	</form>
</script>
<div id="create-reference" class="modal-tmpl">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">×</a>
		<h3><spring:message code="reference.create" /></h3>
	</div>
	<form action='<spring:url value="/reference/ajax" />' method="POST" class="create-reference-form">
		<input type="hidden" name="factId" value="" />

		<div class="modal-body">
			<label>
				<spring:message code="reference.text.placeholder" />:
				<input type="text" name="text" class="span4" tabindex="1000" placeholder='<spring:message code="reference.text.placeholder" />' maxlength="255" />
			</label>
		</div>
		<div class="modal-footer">
			<button type="button" data-dismiss="modal" class="btn btn-danger">
				<i class="icon-remove icon-white"></i>
				<spring:message code="button.cancel" />
			</button>

			<button type="submit" class="btn btn-success" tabindex="1010">
				<i class="icon-ok icon-white"></i>
				<spring:message code="button.save" />
			</button>
		</div>
	</form>
</div>
<script type="text/x-jquery-tmpl" id="js-tmpl-topicTools">
	<div class="pull-right">
		<div class="btn-toolbar">
			<div class="btn-group">
				<a class="btn edit-topic-button"><i class="icon-pencil"></i> <spring:message code="topic.button.edit" /></a>
				<a class="btn dropdown-toggle" data-toggle="dropdown">
					<span class="caret"></span>
				</a>
				<ul class="dropdown-menu">
					<li>
						<a class="topic-history-button" data-history-url='<spring:url value="/topic/{{= topicId}}/history/ajax" />'><i class="icon-time"></i> <spring:message code="topic.button.history" /></a>
					</li>
					<li>
						<a class="topic-copy-button"><i class="icon-share-alt"></i> <spring:message code="topic.button.copy" /></a>
						<form action='<spring:url value="/topic/copy" />' method="POST">
							<input type="hidden" name="id" value="{{= topicId}}" />
						</form>
					</li>
				</ul>
			</div>
			<button type="button" class="btn btn-info share-topic-button" data-access-rights-url='<spring:url value="/topic/{{= topicId}}/accessRights/ajax" />'>
				<i class="icon-share icon-white"></i>
				<spring:message code="topic.share" />
			</button>
		</div>
	</div>
</script>
<div id="edit-topic" class="modal-tmpl">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">×</a>
		<h3><spring:message code="topic.edit" /></h3>
	</div>
	<form action='<spring:url value="/topic/ajax" />' method="POST" class="edit-topic-form">
		<input type="hidden" name="_method" value="PUT" />
		<input type="hidden" name="id" value="" />

		<div class="modal-body">
			<label>
				<spring:message code="topic.term" />:
				<input type="text" name="term" class="span5" tabindex="1000" placeholder='<spring:message code="topic.firstTerm" />' maxlength="125" />
			</label>
			<label>
				<spring:message code="topic.definition" />:
				<input type="text" name="definition" class="span5" tabindex="1010" placeholder='<spring:message code="topic.definition.placeholder" />' maxlength="255" />
			</label>
			<label>
				<spring:message code="topic.category" />:
				<select name="categoryId" tabindex="1020">
					<option value="0">---</option>
					<c:forEach items="${categories}" var="category">
						<option value="${category.id}">${category.name}</option>
					</c:forEach>
				</select>
			</label>
		</div>
		<div class="modal-footer">
			<button type="button" data-dismiss="modal" class="btn btn-danger">
				<i class="icon-remove icon-white"></i>
				<spring:message code="button.cancel" />
			</button>

			<button type="submit" class="btn btn-success" tabindex="1030">
				<i class="icon-ok icon-white"></i>
				<spring:message code="button.save" />
			</button>
		</div>
	</form>
</div>
<script type="text/x-jquery-tmpl" id="js-tmpl-topicEditedByAnotherUser">
	<div class="alert alert-info">
		<a class="close" data-dismiss="alert">x</a>
		<spring:message code="topic.edit.editedByAnotherUser" arguments="{{= term}}" />
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-argumentTypeTools">
	<div class="btn-group show-on-hover box-type-tools">
		<a class="btn dropdown-toggle" data-toggle="dropdown">
			<span class="caret"></span>
		</a>
		<ul class="dropdown-menu">
			<li data-dialog="edit-proContraTheses"><a><i class="icon-pencil"></i> <spring:message code="topic.thesis.dropdown.editLabel" /></a></li>
			<li data-dialog="restore"><a data-ajax-url='<spring:url value="/proContra/{{= parentId}}/{{= type}}/deletedArguments/ajax" />'><i class="icon-repeat"></i> <spring:message code="topic.thesis.dropdown.restoreArgument" /></a></li>
		</ul>
	</div>
</script>
<div id="edit-proContraTheses" class="modal-tmpl">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">×</a>
		<h3><spring:message code="proContra.theses.edit" /></h3>
	</div>
	<form action='<spring:url value="/proContra/theses/ajax" />' method="POST" class="edit-pro-contra-theses-form">
		<input type="hidden" name="_method" value="PUT" />
		<input type="hidden" name="topicId" value="" />

		<div class="modal-body">
			<label>
				<spring:message code="proContra.theses.messageCodeAppendix" />:
				<select name="messageCodeAppendix" tabindex="1010">
					<c:forEach items="${topicThesesMessageCodeAppendices}" var="messageCodeAppendix">
						<option value="${messageCodeAppendix}"><spring:message code="TopicTheses.${messageCodeAppendix}" /></option>
					</c:forEach>
				</select>
			</label>
		</div>
		<div class="modal-footer">
			<button type="button" data-dismiss="modal" class="btn btn-danger">
				<i class="icon-remove icon-white"></i>
				<spring:message code="button.cancel" />
			</button>

			<button type="submit" class="btn btn-success" tabindex="1020">
				<i class="icon-ok icon-white"></i>
				<spring:message code="button.save" />
			</button>
		</div>
	</form>
</div>
<div id="history" class="modal-tmpl">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">×</a>
		<h3><spring:message code="history" /></h3>
	</div>
	<div class="modal-body">
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-success" tabindex="2010">
			<i class="icon-check icon-white"></i>
			<spring:message code="button.ok" />
		</button>
	</div>
</div>
<script type="text/x-jquery-tmpl" id="js-tmpl-historyTable">
	<table class="table table-bordered table-striped" data-entity-id="{{= id}}">
		<thead>
			<tr>
				<th><spring:message code="history.event" /></th>
				<th><spring:message code="history.author" /></th>
				<th><spring:message code="history.date" /></th>
			</tr>
		</thead>
		<tbody>
			{{each events}}
				<tr>
					<td>
						{{html description}}
						{{if restorable}}
							<button type="submit" class="btn btn-tiny">
								<i class="icon-repeat"></i>
								<spring:message code="history.restoreText" />
							</button>
						{{/if}}
					</td>
					<td>{{= authorName}}</td>
					<td><abbr class="timeago" title="{{= dateTimeGmt}}">{{= dateTimeLocalized}}</abbr></td>
				</tr>
			{{/each}}
		</tbody>
	</table>	
</script>
<div id="restore" class="modal-tmpl">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">×</a>
	</div>
	<div class="modal-body">
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-success" tabindex="2010">
			<i class="icon-check icon-white"></i>
			<spring:message code="button.ok" />
		</button>
	</div>
</div>
<script type="text/x-jquery-tmpl" id="js-tmpl-argumentRestoreTitle">
	<h3><spring:message code="argument.restore.title" arguments="{{= typeText}}" /></h3>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-factRestoreTitle">
	<h3><spring:message code="fact.restore.title" arguments="{{= typeText}}" /></h3>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-restoreTable">
	<table class="table table-bordered table-striped">
		<thead>
			<tr>
				<th><spring:message code="restore.text" /></th>
				<th><spring:message code="restore.author" /></th>
				<th><spring:message code="restore.date" /></th>
			</tr>
		</thead>
		<tbody>
			{{each entities}}
				<tr data-id="{{= id}}">
					<td>
						{{= text}}
						<form action='<spring:url value="/{{= entityName}}/restore/ajax" />' method="POST">
							<input type="hidden" name="id" value="{{= id}}" />
							<button type="submit" class="btn btn-tiny">
								<i class="icon-repeat"></i>
								<spring:message code="restore.button.submit" />
							</button>
						</form>
					</td>
					<td>{{= authorName}}</td>
					<td><abbr class="timeago" title="{{= dateTimeGmt}}">{{= dateTimeLocalized}}</abbr></td>
				</tr>
			{{/each}}
		</tbody>
	</table>	
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-factTypeTools">
	<div class="btn-group show-on-hover box-type-tools">
		<a class="btn dropdown-toggle" data-toggle="dropdown">
			<span class="caret"></span>
		</a>
		<ul class="dropdown-menu">
			<li data-dialog="restore"><a data-ajax-url='<spring:url value="/argument/{{= parentId}}/deletedFacts/{{= type}}/ajax" />'><i class="icon-repeat"></i> <spring:message code="factType.dropdown.restoreFact" arguments="{{= typeText}}" /></a></li>
		</ul>
	</div>
</script>
<div id="share-topic" class="modal-tmpl">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">×</a>
		<h3><spring:message code="topic.share" /></h3>
	</div>
	<div class="modal-body">
		<h4><spring:message code="topic.visibility" /></h4>
		<form action='<spring:url value="/topic/setVisibility/ajax" />' method="POST" id="set-visibility-form">
			<input type="hidden" name="id" value="" />
			
			<div class="control-group">
				<div class="controls">
					<label class="checkbox">
						<input tabindex="1010" type="checkbox" name="visibility" value="PUBLIC" />
						<spring:message code="topic.visibility.public" />
					</label>
				</div>
			</div>
			<p class="set-public-notice">
				<spring:message code="copyright.cc.setPublicNotice" />
			</p>
			
			<button type="submit" class="btn btn-success" tabindex="1020">
				<i class="icon-white icon-ok"></i>
				<spring:message code="button.save" />
			</button>
		</form>
		<br /><br />
		
		<h4><spring:message code="topic.share.accessRights" /></h4>
		<div class="access-rights-list">
			<table class="table table-bordered table-condensed table-striped">
				<thead>
					<tr>
						<th><spring:message code="topic.share.contact" /></th>
						<th><spring:message code="topic.share.permission" /></th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		
		<form action='<spring:url value="/topic/share/ajax" />' method="POST" class="form-inline" id="add-access-right-form">
			<input type="hidden" name="id" value="" />

			<spring:message code="topic.share.new" />:<br />

			<input name="contact" type="text" class="input-large" placeholder='<spring:message code="topic.share.contact" />' tabindex="1100" data-source-url='<spring:url value="/usernames/autocomplete/" />' data-min-length="0" />
			<select name="permission" class="input-medium" title='<spring:message code="topic.share.permission" />' tabindex="1110">
				<c:forEach items="${permissions}" var="permission">
					<option value="${permission}"><spring:message code="topic.share.permission.${permission}" /></option>
				</c:forEach>
			</select>
			
			<button type="submit" class="btn btn-success" tabindex="1120">
				<i class="icon-white icon-ok"></i>
				<spring:message code="topic.share" />
			</button>
		</form>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn" tabindex="3010">
			<i class="icon-check"></i>
			<spring:message code="button.ok" />
		</button>
	</div>
</div>
<script type="text/x-jquery-tmpl" id="js-tmpl-copyright">
	<tags:copyright editable="true" license="CC" />
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-shareTopicLoader">
	<tr>
		<td colspan="2"><spring:message code="topic.share.accessRights.loading" /> <div class="ajax-indicator"></div></td>
	</tr>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-accessRight">
	<tr data-access-right-id="{{= id}}" data-user-id="{{= userId}}">
		<td>
			{{= contactName}}
			{{if deletable}}
				<form action='<spring:url value="/topic/share/ajax" />' method="POST">
					<input type="hidden" name="_method" value="DELETE" />
					<input type="hidden" name="id" value="{{= id}}" />

					<i class="icon-trash action"></i>
				</form>
			{{/if}}
		</td>
		<td>
			{{= accessRight}}
		</td>
	</tr>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-argumentReorder">
	<div id="reorder-arguments" class="reorder-tool pull-right">
		<div class="btn-group">
			<a class="btn dropdown-toggle" data-toggle="dropdown">
				<spring:message code="argument.button.reorder" />
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><strong><spring:message code="argument.orderBy" />:</strong></li>
				<li data-order-by="relevance"><a><spring:message code="argument.orderBy.relevance" /></a></li>
				<li data-order-by="personalRelevance"><a><spring:message code="argument.orderBy.personalRelevance" /></a></li>
			</ul>
		</div>
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-factReorder">
	<div class="reorder-facts reorder-tool pull-right">
		<div class="btn-group">
			<a class="btn dropdown-toggle" data-toggle="dropdown">
				<spring:message code="fact.button.reorder" />
				<span class="caret"></span>
			</a>
			<ul class="dropdown-menu">
				<li><strong><spring:message code="fact.orderBy" />:</strong></li>
				<li data-order-by="relevance"><a><spring:message code="fact.orderBy.relevance" /></a></li>
				<li data-order-by="personalRelevance"><a><spring:message code="fact.orderBy.personalRelevance" /></a></li>
			</ul>
		</div>
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-error">
	<div class="alert alert-error">
		<a class="close" data-dismiss="alert">×</a>
		{{html errorMsg}}
	</div>
</script>
<div id="access-denied" class="modal-tmpl" data-exit-url='<spring:url value="/" />'>
	<div class="modal-header">
		<h3><spring:message code="topic.accessDenied.title" /></h3>
	</div>
	<div class="modal-body">
		<span class="access-denied-absolutely hide"><spring:message code="topic.accessDenied.absolutely" /></span>
		<span class="access-denied-readOnly hide"><spring:message code="topic.accessDenied.readOnly" /></span>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn btn-success">
			<i class="icon-remove icon-white"></i>
			<spring:message code="topic.accessDenied.exit.button" />
		</button>
	</div>
</div>
<div id="session-timeout" class="modal-tmpl">
	<div class="modal-header">
		<a class="close" data-dismiss="modal">×</a>
		<h3><spring:message code="topic.sessionTimeout.title" /></h3>
	</div>
	<div class="modal-body">
		<p><spring:message code="topic.sessionTimeout.1" /></p>
		<p><spring:message code="topic.sessionTimeout.2" /></p>
	</div>
	<div class="modal-footer">
		<button type="button" data-dismiss="modal" class="btn">
			<i class="icon-remove"></i>
			<spring:message code="topic.sessionTimeout.closeDialog" />
		</button>
		<button type="button" class="btn btn-reload">
			<i class="icon-repeat"></i>
			<spring:message code="topic.sessionTimeout.reload" />
		</button>
	</div>
</div>
<%@ include file="/WEB-INF/jspf/requiresLogin.jspf"%>
<script type="text/x-jquery-tmpl" id="js-argumentConfirmDelete"><spring:message code="argument.delete.confirm" /></script>
<script type="text/x-jquery-tmpl" id="js-factConfirmDelete"><spring:message code="fact.delete.confirm" /></script>
<script type="text/x-jquery-tmpl" id="js-confirmDeleteReference"><spring:message code="reference.delete.confirm" /></script>
<div id="status" style="display:none;">
	<span id="comet-status-text"><spring:message code="comet.offline" /></span>
	<i class="icon-offline" id="comet-status-icon"></i>
</div>