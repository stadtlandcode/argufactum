"use strict";
(function(argue) {
	var processIds = {
		getColumn: function(processId, ancestorElement) {
			return $(ancestorElement).find('form input[name="processId"][value="' + processId + '"]').closest('form').parent();
		},
		generateId: function() {
			return Math.floor(Math.random()*1000000000000);
		}
	};
	
	var optionHandler = {
		init: function() {
			$('#create-option-button').click(function() { optionHandler.addOption(null); });
			this.initColumn($('.comparison-table thead'));
		},
		initColumn: function(ancestor) {
			$(ancestor).find('form').ajaxSubmit(optionHandler.postAjax);
			$(ancestor).find('.option').autoGrowInput({minWidth: 160}).autoHideInput().submitClosest('change');
		},
		addOption: function(option) {
			var columnTmpl = $.tmpl("newOption", { id: argue.comparison.topicId, processId: processIds.generateId() });
			if(option != null) {
				this.convertToPersistentOption(columnTmpl, option);
				this.editOptionLabel(columnTmpl, option.label);
			}
			this.initColumn(columnTmpl);
			
			$('.comparison-table thead td.option-tools').before(columnTmpl);
			if(option == null) {
				$('.comparison-table thead .option:last').focus();
			}
			
			$('.comparison-table tbody tr').append('<td></td>');
		},
		editOption: function(option) {
			this.editOptionLabel(this.getColumn(option.id), option.label);
		},
		editOptionLabel: function(column, label) {
			$(column).find('.option').val(label);
		},
		getColumn: function(optionId) {
			return $('.comparison-table thead input[name="id"][value="' + optionId + '"]').closest('th');
		},
		convertToPersistentOption: function (column, option) {
			$(column).find('input[type="hidden"]').val('');
			$(column).find('form').prepend($.tmpl("editOptionFormPartial", option));
		},
		postAjax: {
			success: function (option) {
				if(optionHandler.getColumn(option.id).length < 1) {
					optionHandler.convertToPersistentOption(processIds.getColumn(option.processId, $('.comparison-table thead')), option);
				}
			}
		}
	};
	
	var criterionHandler = {
		init: function() {
			$('#create-criterion-button').click(function() { criterionHandler.addCriterion(null); });
			this.initColumn($('.comparison-table tbody'));
		},
		initColumn: function(ancestor) {
			$(ancestor).find('form').ajaxSubmit(criterionHandler.postAjax);
			$(ancestor).find('.criterion').autoHideInput().autoGrowInput({minWidth: 160}).submitClosest('change');
			$(ancestor).find('.importance-slider').slider({
				range: "min",
				value: 50,
				min: 1,
				max: 100,
				step: 1
			});
		},
		addCriterion: function(criterion) {
			var row = $('<tr></tr>');
			var columnTmpl = $.tmpl("newCriterion", { id: argue.comparison.topicId, processId: processIds.generateId() });
			if(criterion != null) {
				this.convertToPersistentCriterion(columnTmpl, criterion);
				this.editCriterionLabel(columnTmpl, criterion.label);
			}
			this.initColumn(columnTmpl);
			definitionsHandler.initCriterion(columnTmpl);
			
			$(row).append(columnTmpl);
			$('.comparison-table thead th').each(function() {
				$(row).append('<td></td>');
			});
			$(row).append('<td></td>');
			$(row).appendTo('.comparison-table tbody');
			
			if(criterion == null) {
				$('.comparison-table tbody tr:last .criterion').focus();
			}
		},
		editCriterion: function(criterion) {
			this.editCriterionLabel(this.getColumn(criterion.id), criterion.label);
		},
		editCriterionLabel: function(column, label) {
			$(column).find('.criterion').val(label);
		},
		getColumn: function(criterionId) {
			return $('.comparison-table tbody th input[name="id"][value="' + criterionId + '"]').closest('th');
		},
		convertToPersistentCriterion: function (column, criterion) {
			$(column).find('input[type="hidden"]').val('');
			$(column).find('form').prepend($.tmpl("editCriterionFormPartial", criterion));
		},
		postAjax: {
			success: function (criterion) {
				if(criterionHandler.getColumn(criterion.id).length < 1) {
					criterionHandler.convertToPersistentCriterion(processIds.getColumn(criterion.processId, $('.comparison-table tbody')), criterion);
				}
			}
		}
	};
	
	var inputMethodHandler = {
		getTemplateObject: function(column, definitionSet) {
			return {
				definitions: definitionSet.definitions,
				definitionSetId: definitionSet.id
			};
		},
		swapTo: function(column, definitionSet) {
			var matchingElement = $(column).find('.value-input-' + definitionSet.inputMethod);
			if(matchingElement.length <= 0) {
				matchingElement = $(column).find('.value-input-' + definitionSet.inputMethod + '-' + definitionSet.id);
			}
			if(matchingElement.length > 0) {
				$(matchingElement).show().siblings().hide();
			} else {
				$(column).find('.value-input').hide();
				this[definitionSet.inputMethod](column, this.getTemplateObject(column, definitionSet));
			}
		},
		STARS: function(column, templateObject) {
			$(column).append($.tmpl("inputStars", templateObject));
			$(column).find('.star').rating();
		},
		SELECT: function(column, templateObject) {
			$(column).append($.tmpl("inputSelect", templateObject));
		},
		TEXT: function(column, templateObject) {
			$(column).append($.tmpl("inputText", templateObject));
		},
		CHECKBOX: function(column, templateObject) {
			$(column).append($.tmpl("inputCheckbox", templateObject));
		}
	};
	
	var definitionsHandler = {
		globalDefinitionSets: null,
		additionalDefinitionSets: null,
		initDropdowns: function() {
			if(this.globalDefinitionSets == null) {
				/* TODO error-handling */
				return false;
			}
			var that = this;
			$('.comparison-table tbody th').each(function() {
				that.initCriterion($(this));
			});
		},
		initCriterion: function(criterionElement) {
			this.createDropdown($(criterionElement).find('.data-type-menu'), [], $(criterionElement).find('input[name=definitionSetId]').val());
		},
		createDropdown: function(element, additionalDefinitionSets, activeDefinitionSetId) {
			var that = this;
			var mergedDefinitionSets = that.globalDefinitionSets.concat(additionalDefinitionSets);
			var dataTypes = {
				rating: {
					definitionSets: that.filterDefinitionSets(mergedDefinitionSets, "RATING"),
				},
				text: {
					id: function() {
						var definitionSets = that.filterDefinitionSets(additionalDefinitionSets, "TEXT");
						if(definitionSets.length == 0) {
							definitionSets = that.filterDefinitionSets(that.globalDefinitionSets, "TEXT");
						}
						return (definitionSets.length == 0) ? null : definitionSets[0].id;
					}
				},
				boolean: {
					globalDefinitionSets: that.filterDefinitionSets(that.globalDefinitionSets, "BOOLEAN")
				},
				number: {
					definitionSets: that.filterDefinitionSets(mergedDefinitionSets, "NUMBER")
				}
			};
			$(element).prepend($.tmpl("definitionsDropdown", dataTypes));
			$(element).find('ul').find('li.selectable:first').addClass('default');
			$(element).superfish().selectMenu(definitionsHandler.onDropdownChange);
			if(activeDefinitionSetId) {
				$(element).find('li[data-definition-set-id="' + activeDefinitionSetId + '"].selectable').click();
			}
			if($(element).find('li.active').length < 1) {
				$(element).children('li.default:first').click();
			}
		},
		editDropdown: function(element, additionalDefinitionSets) {
			
		},
		filterDefinitionSets: function (definitionSets, dataType) {
			return definitionSets.filter(function(value) {
				return value.dataType == dataType;
			});
		},
		getDefinitionSet: function (definitionSetId) {
			var idFilter = function(value) { return value.id == definitionSetId; };
			var definitionSet = this.globalDefinitionSets.filter(idFilter);
			if(!definitionSet) {
				definitionSet = this.additionalDefinitionSets.filter(idFilter);
			}
			return definitionSet[0];
		},
		getDefinitionIdInputElement: function(descendantOfForm) {
			return $(descendantOfForm).closest('form').find('input[name=definitionSetId]');
		},
		onDropdownChange: function() {
			// set definitionSetId in form and submit
			var activeDefinitionSetId = $(this).closest('.data-type-menu').find('li[data-definition-set-id].active').attr('data-definition-set-id');
			var idInput = $(this).closest('form').find('input[name="definitionSetId"]');
			var isPersisted = $(this).closest('form').find('input[name="processId"]').length < 1;
			if($(idInput).val() != activeDefinitionSetId) {
				$(idInput).val(activeDefinitionSetId);
				if(isPersisted) {
					$(this).submitClosest();
				}
			}
			
			// modify input
			var definitionSet = definitionsHandler.getDefinitionSet(activeDefinitionSetId);
			$(this).closest('tr').children('td:not(:last)').each(function() {
				inputMethodHandler.swapTo($(this), definitionSet);
			});
		},
	};
	
	argue.comparison = {
		topicId: null,
		init: function(topicId, contextPath) {
			this.topicId = topicId;
			argue.url.setContextPath(contextPath);
			
			this.initTemplates();
			optionHandler.init();
			criterionHandler.init();
		},
		initTemplates: function() {
			$.get(argue.url('/jsTemplates/comparison'), function(data) {
				$('body').append(data);
				$('#js-tmpl-new-option').template("newOption");
				$('#js-tmpl-edit-option-form-partial').template("editOptionFormPartial");
				$('#js-tmpl-new-criterion').template("newCriterion");
				$('#js-tmpl-edit-criterion-form-partial').template("editCriterionFormPartial");
				$('#js-tmpl-definitions-dropdown-selectable-entry').template("definitionsDropdownSelectableEntry");
				$('#js-tmpl-definitions-dropdown').template("definitionsDropdown");
				$('#js-tmpl-input-select').template("inputSelect");
				$('#js-tmpl-input-text').template("inputText");
				$('#js-tmpl-input-checkbox').template("inputCheckbox");
				$('#js-tmpl-input-stars').template("inputStars");
				definitionsHandler.globalDefinitionSets = $.parseJSON($('#global-definition-sets').text());
				definitionsHandler.initDropdowns();
			});
		},
		cometHandler: {
			createOption: function(result) { optionHandler.addOption(result); },
			editOption: function(result) { optionHandler.editOption(result); },
			createCriterion: function(result) { criterionHandler.addCriterion(result); },
			editCriterion: function(result) { criterionHandler.editCriterion(result); }
		}
	};
}(argue));
