<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" buffer="128kb"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<script type="text/x-jquery-tmpl" id="js-tmpl-new-option">
	<th>
		<%@ include file="/WEB-INF/jspf/topic/createOption.jspf"%>
	</th>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-edit-option-form-partial">
	<%@ include file="/WEB-INF/jspf/topic/editOptionPartial.jspf"%>
</script>

<script type="text/x-jquery-tmpl" id="js-tmpl-new-criterion">
	<th>
		<%@ include file="/WEB-INF/jspf/topic/createCriterion.jspf"%>
	</th>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-edit-criterion-form-partial">
	<%@ include file="/WEB-INF/jspf/topic/editCriterionPartial.jspf"%>
</script>
<script type="application/json" id="global-definition-sets">${globalDefinitionSets}</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-definitions-dropdown-selectable-edit">
	<li><a><spring:message code="criterion.definitionSet.edit" /></a></li>
	<li><a><spring:message code="criterion.definitionSet.delete" /></a></li>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-definitions-dropdown-selectable-entry">
	<li class="selectable" data-definition-set-id="{{= id}}">
		<a><i class="icon-ok"></i> {{= name}}</a>
		{{if !global}}
			<ul class="dropdown-menu sub-menu">
				{{tmpl "#js-tmpl-definitions-dropdown-selectable-edit"}}
			</ul>
		{{/if}}
	</li>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-definitions-dropdown-number-entry">
	<li class="selectable" data-definition-set-id="{{= id}}">
		<a><i class="icon-ok"></i> {{= name}}</a>
		<ul class="dropdown-menu sub-menu">
			<li><strong><spring:message code="criterion.definitionSet.properties" />:</strong></li>
			<li><a>
				<spring:message code="criterion.numberDefinitionSet.order" />
				{{if numberDefinition.sortOrder == 'ASC'}}
					<spring:message code="criterion.numberDefinitionSet.ascOrder" />
				{{else}}
					<spring:message code="criterion.numberDefinitionSet.descOrder" />
				{{/if}}
			</a></li>
			<li><a>
				<spring:message code="criterion.numberDefinitionSet.min" />
				{{if numberDefinition.min != null}}
					{{= numberDefinition.min}}
				{{else}}
					<spring:message code="criterion.numberDefinitionSet.min.undefined" />
				{{/if}}
			</a></li>
			<li><a>
				<spring:message code="criterion.numberDefinitionSet.max" />
				{{if numberDefinition.max != null}}
					{{= numberDefinition.max}}
				{{else}}
					<spring:message code="criterion.numberDefinitionSet.max.undefined" />
				{{/if}}
			</a></li>
			{{if !global}}
				<li class="divider"></li>
				{{tmpl "#js-tmpl-definitions-dropdown-selectable-edit"}}
			{{/if}}
		</ul>
	</li>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-definitions-dropdown">
	<li><strong><spring:message code="criterion.dataType" />:</strong></li>
	<li class="selectable default" data-datatype="RATING">
		<a><i class="icon-ok"></i> <spring:message code="CriterionDataType.RATING" /></a>

		<ul class="dropdown-menu sub-menu">
			{{tmpl(rating.definitionSets) "#js-tmpl-definitions-dropdown-selectable-entry"}}
			<li class="divider"></li>
			<li><a><spring:message code="criterion.definitionSet.create" /></a></li>
		</ul>
	</li>
	<li class="selectable" data-datatype="TEXT" data-definition-set-id="{{= text.id}}">
		<a><i class="icon-ok"></i> <spring:message code="CriterionDataType.TEXT" /></a>
	</li>
	<li class="selectable" data-datatype="BOOLEAN">
		<a><i class="icon-ok"></i> <spring:message code="CriterionDataType.BOOLEAN" /></a>

		<ul class="dropdown-menu sub-menu">
			{{tmpl(boolean.globalDefinitionSets) "#js-tmpl-definitions-dropdown-selectable-entry"}}
		</ul>
	</li>
	<li class="selectable" data-datatype="NUMBER">
		<a><i class="icon-ok"></i> <spring:message code="CriterionDataType.NUMBER" /></a>
		<ul class="dropdown-menu sub-menu">
			{{tmpl(number.definitionSets) "#js-tmpl-definitions-dropdown-number-entry"}}
			<li class="divider"></li>
			<li><a><spring:message code="criterion.definitionSet.create" /></a></li>
		</ul>
	</li>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-input-text">
	<input name="string" type="text" value="" class="value-input value-input-TEXT" />
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-input-checkbox">
	<div class="value-input value-input-CHECKBOX">
		<input name="string" type="checkbox" value="1" /> <spring:message code="value.yes" />
	</div>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-input-select">
	<select name="string" class="value-input value-input-SELECT-{{= definitionSetId}}">
		{{tmpl(definitions) "#js-tmpl-input-select-option"}}
	</select>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-input-select-option">
	<option value="{{= string}}">{{= string}}</option>
</script>
<script type="text/x-jquery-tmpl" id="js-tmpl-input-stars">
	<div class="value-input value-input-STARS">
		<input name="string" type="radio" class="star" value="<spring:message code="value.rating.stars" arguments="1,5" />" />
		<input name="string" type="radio" class="star" value="<spring:message code="value.rating.stars" arguments="2,5" />" />
		<input name="string" type="radio" class="star" value="<spring:message code="value.rating.stars" arguments="3,5" />" />
		<input name="string" type="radio" class="star" value="<spring:message code="value.rating.stars" arguments="4,5" />" />
		<input name="string" type="radio" class="star" value="<spring:message code="value.rating.stars" arguments="5,5" />" />
	</div>
</script>