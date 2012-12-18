(function(argue) {
	"use strict";
	var reorder = {
		relevances: ["HIGH", "AVERAGE", "LOW", ""],
		getTimestamps: function(elements) {
			var timestamps = [];
			$(elements).each(function() {
				timestamps.push($(this).data('timestamp'));
			});
			timestamps.sort(function(a,b) { return a - b; });
			return timestamps;
		},
		generic: function(elements, firstArray, firstDataAttr) {
			var timestamps = reorder.getTimestamps(elements);
			var prevElement = null;
			for(var i = 0; i < firstArray.length; i++) {
				var matchingElements = $(elements).filterByData(firstDataAttr, firstArray[i]);
				for(var j = 0; j < timestamps.length; j++) {
					$(matchingElements).filterByData('timestamp', timestamps[j]).each(function() {
						if(prevElement !== null) {
							$(this).insertAfter(prevElement);
						}
						prevElement = $(this);
					});
				}
			}
		},
		by: {
			relevance: function() {
				reorder.generic($(this), reorder.relevances, "relevance");
			},
			personalRelevance: function() {
				reorder.generic($(this), reorder.relevances, "userRelevance");
			}
		}
	};
	
	var restoreDialog = {
		load: function(url, title, entityName) {
			var jqXHR = argue.get(url);
			jqXHR.done(function(data) {
				restoreDialog.open(data, title, entityName);
			});
		},
		open: function(data, title, entityName) {
			argue.assertValidData(data);
			data['entityName'] = entityName;
			
			$('#restore h3, #restore table').remove();
			$('#restore .modal-header').append(title);
			$.tmpl('restoreTable', data).appendTo($('#restore .modal-body'));
			$('#restore table abbr.timeago').timeago();
			$('#restore table form').ajaxSubmit({ 'onSuccess': restoreDialog.removeEntity });
			$('#restore').modal('show');
		},
		removeEntity: function(form, data) {
			$('#restore table tr').filterByData('id', data.id).remove();
		}
	};
	
	var editTopicThesesDialog = {
		init: function() {
			$('#edit-proContraTheses form').submit(editTopicThesesDialog.onSubmit).ajaxSubmit({'onSuccess': editTopicThesesDialog.close});
		},
		open: function() {
			$('#edit-proContraTheses input[name="topicId"]').val(argue.topic.getId());
			$('#edit-proContraTheses').modal('show');
			$('#edit-proContraTheses').find('select').val(editTopicThesesDialog.getCurrentMessageCodeAppendix()).focus();
		},
		close: function() {
			$('#edit-proContraTheses').modal('hide');
		},
		getCurrentMessageCodeAppendix: function() {
			return $('.topic-content .box-list-container').data('messageCodeAppendix');
		},
		onSubmit: function(event) {
			if(editTopicThesesDialog.getCurrentMessageCodeAppendix() === $('#edit-proContraTheses').find('select').val()) {
				argue.stopEvent(event);
				editTopicThesesDialog.close();
			}
		}
	};
	$(argue).on('templates.loaded.editTopicThesesDialog', editTopicThesesDialog.init);
	
	var editTopicTheses = function(event, data) {
		if(editTopicThesesDialog.getCurrentMessageCodeAppendix() !== data.thesesMessageCodeAppendix) {
			$('.topic-content .box-list-container').data('messageCodeAppendix', data.thesesMessageCodeAppendix);
			$('.topic-content .PRO h2, h2.PRO span:first').text(data.proThesis);
			$('.topic-content .CONTRA h2, h2.CONTRA span:first').text(data.contraThesis);
		}
	};
	$(argue).on('proContraTheses.edit', editTopicTheses);
	
		
	var ArgueBox = function(entityName, group, opts) {
		this.entityName = entityName;
		this.relevances = ['LOW', 'AVERAGE', 'HIGH'];
		var that = this;
		
		var getChildPage = function(id) {
			return $('#' + entityName + '-content-' + id);
		};
		
		this.create = function(data, number) {
			data['number'] = number;
			if(data['url']) {
				data.url = argue.url(data.url);
			}
			var fragment = $.tmpl(entityName, data);
			$(fragment).find('abbr.timeago').timeago();
			$(fragment).updateModificationLog(data);
			that.relevance.updateStars(fragment);
			that.userRelevance.restore(fragment, data['userRelevanceVotes']);
			
			that.addTools(fragment);
			
			return fragment;
		};

		this.addTools = function(box) {
			if(!argue.topic.opts.editable && !argue.topic.opts.voteable) {
				return false;
			}
			
			$.tmpl(entityName + 'Tools', {
				id: $(box).data(entityName + 'Id')
			}).appendTo(box);
			
			if(argue.topic.opts.voteable) {
				$(box).find('.stars').stars().stars(getNumberForRelevance(that.userRelevance.getCurrent(box))).on('rating.change.byClick', that.userRelevance.onChange).submit(that.userRelevance.onSubmit).ajaxSubmit({
					'onSuccess': that.userRelevance.onSuccess,
					'onError': that.userRelevance.onError
				});
			}
			
			if(!argue.topic.opts.editable) {
				$(box).find('.tool-buttons').remove();
				$(box).find('.box-tools').addClass('box-vote');
			} else {
				$(box).find('.edit-box-button').click(that.editForm.open);
				$(box).find('.delete-box-form').ajaxSubmit().find('button').submitClosest('click', $('#js-' + entityName + 'ConfirmDelete').text());
				$(box).find('.box-history-button').click(function() { argue.history.load($(this).data('historyUrl'), group.restoreBox); });
			}
		};
		
		this.edit = function(event, data, box) {
			if($(box).data('textId') !== data.textId) {
				$(box).data('textId', data.textId);
				$(box).find('.box-text').text(data.text);
				$(box).find('.author-note').updateModificationLog(data);

				getChildPage(data.id).find('.page-header h2 span:last').text(data.text);
				
				if($(box).find('.edit-form:visible')) {
					that.editForm.alert(box, $.tmpl(entityName + "EditedByAnotherUser", data));
				}
			}
		};
		
		this.editForm = {
			alert: function(box, tmpl) {
				var form = $(box).find('.edit-form');
				$(form).find('.alert').remove();
				$(form).find('button:first').before(tmpl);
			},
			isOpen: function(box) {
				return $(box).find('.edit-form').is(':visible');
			},
			open: function() {
				var box = $(this).closest('.box');
				that.editForm.closeAll();
				
				$(box).find('.box-content, .box-tools').hide();
				$.tmpl("editArgueBox", {
					id: $(box).data(entityName + 'Id'),
					text: $(box).find('.box-text').text(),
					entityName: entityName
				}).appendTo(box);
				
				$(box).find('textarea').focus().textareaHotkeys({ escCallback: that.editForm.closeAll });
				$(box).find('.edit-form').submit(that.editForm.onSubmit).ajaxSubmit({'onSuccess': that.editForm.closeAll});
				$(box).find('.edit-form .cancel-button').click(that.editForm.closeAll);
			},
			closeAll: function() {
				var boxList = $('.box .edit-form').closest('.box');
				$(boxList).find('.box-content, .box-tools').show();
				$('.box .edit-form').remove();
				$(boxList).triggerHandler('editForm.close');
			},
			onSubmit: function(event) {
				if($(this).find('textarea').val() === $(this).parent().find('.box-text').text() || !$(this).find('textarea').val()) {
					argue.stopEvent(event);
					that.editForm.closeAll();
				}
			}
		};
		
		this.remove = function(event, data, box) {
			if(that.editForm.isOpen(box)) {
				$(box).on('editForm.close', function() { $(argue).triggerHandler(entityName + '.delete', data, box); });
				that.editForm.alert(box, $.tmpl(entityName + "DeletedByAnotherUser"));
				argue.stopEvent(event);
			} else {
				var list = $(box).closest('ul');
				$(box).remove();
				$(argue).triggerHandler(entityName + '.postDelete', list);
			}
		};
		
		// relevance
		var getRelevanceForNumber = function(number) {
			return number > 0 ? that.relevances[number - 1] : '';
		};
		var getNumberForRelevance = function(relevance) {
			return $.inArray(relevance, that.relevances) + 1;
		};
		this.userRelevance = {
			getCurrent: function(box) {
				return $(box).data('userRelevance');
			},
			onChange: function(event, numberOfStars) {
				$(this).children('input[name="relevance"]').val(getRelevanceForNumber(numberOfStars));
				$(this).submit();
			},
			onSubmit: function(event) {
				var currentVal = $(this).children('input[name="relevance"]').val();
				if(currentVal === that.userRelevance.getCurrent($(this).closest('.box'))) {
					argue.stopEvent(event);
					that.userRelevance.onError($(this));
				}
			},
			onError: function(form) {
				$(form).stars(getNumberForRelevance(that.userRelevance.getCurrent($(form).closest('.box')))).triggerHandler('rating.unlock');
			},
			onSuccess: function(form) {
				$(form).closest('.box').data('userRelevance', $(form).children('input[name="relevance"]').val());
				$(form).triggerHandler('rating.unlock');
			},
			restore: function(box, relevances) {
				if(!relevances) {
					return false;
				}
				
				for (var i = 0; i < relevances.length; i++) {
					var userRelevance = relevances[i];
					if(userRelevance.userId === argue.getUserId()) {
						$(box).data('userRelevance', userRelevance.relevance);
						break;
					}
				}
			}
		};
		this.relevance = {
			edit: function(event, data, box) {
				if($(box).length <= 0 || $(box).data('relevance') === data.relevance.value) {
					return false;
				}
				
				$(box).data('relevance', data.relevance.value === null ? "" : data.relevance.value);
				$(box).find('.stats-box-relevance').attr('title', data.relevance.label);
				that.relevance.updateStars(box);
			},
			updateStars: function(box) {
				$(box).find('.stats-box-relevance').stars({'order': 'asc', 'readOnly': true}).stars(getNumberForRelevance($(box).data('relevance')));
			}
		};
	};
	
	var ArgueBoxGroup = function(contentSelector, entityName, options) {
		var opts = $.extend({}, ArgueBoxGroup.defaults, options);
		var that = this;
		var boxHandler = new ArgueBox(entityName, that, opts);
		this.entityName = entityName;
		this.boxHandler = boxHandler;
		
		this.getContent = function(parentId) {
			if(parentId && opts.parentList) {
				return $(contentSelector).filterByData(opts.parentEntityName + 'Id', parentId);
			} else {
				return $(contentSelector);
			}
		};
		this.getParentId = function(descendant) {
			var content = $(descendant).closest(contentSelector);
			return $(content).data(opts.parentEntityName + 'Id');
		};
		this.get = function(selector, parentId) {
			return that.getContent(parentId).find(selector);
		};
		this.getEntity = function(id) {
			return that.get('li.' + entityName).filterByData(entityName + 'Id', id);	
		};
		this.getTypeContainer = function(type, parentId) {
			return that.get('.' + type, parentId);
		};
		this.getList = function(type, parentId) {
			return $(that.getTypeContainer(type, parentId)).find('ul.' + entityName + '-list');
		};
		
		this.add = function(event, data) {
			if(that.getEntity(data.id).length > 0) {
				return true;
			}
			var list = that.getList(data.type, data.parentId);
			var argueBox = boxHandler.create(data, $(list).children().length + 1);
			$(list).append(argueBox);
			that.updateParentStats(data.parentId);
		};
		
		this.onDelete = function(event, list) {
			if(opts.parentList !== null) {
				var parentId = that.getParentId(list);
				that.updateParentStats(parentId);
			}
			that.renumerate(list);
		};
		
		this.updateParentStats = function(parentId) {
			if(parentId && opts.parentList !== null) {
				opts.parentList.boxHandler.updateStats(opts.parentList.getEntity(parentId), that.getContent(parentId));
			}
		};
		
		this.renumerate = function(list) {
			var currentNumber = 0;
			$(list).children('.box').each(function() {
				currentNumber++;
				$(this).find('.stats-box-main').text(currentNumber + '.');
			});
		};
		
		// context of this: restore button in history dialog
		this.restoreBox = function() {
			var box = that.getEntity($(this).closest('table').data('entityId'));
			argue.history.closeDialog();
			$(box).find('.edit-box-button').click();
			$(box).find('textarea').val($(this).siblings('span').text());
			$(box).find('.edit-form').submit();
		};
		
		this.setupReorderTool = function(content) {
			if(argue.topic.opts.voteable) {
				$.tmpl(entityName + 'Reorder').prependTo($(content).find('.box-list-container > div:last'));
				$(content).find('.reorder-tool li[data-order-by]').click(that.reorder);
			}
		};
		this.reorder = function() {
			var f = reorder.by[$(this).data('orderBy')];
			var content = $(this).closest('.content');
			if(f) {
				for (var i = 0; i < opts.types.length; i++) {
					var type = opts.types[i];
					var list = $(content).find('.' + type + ' ul');
					
					f.call($(list).find('li.box'));
					that.renumerate(list);
				}
			}
		};
		
		// tools per type (list)
		this.setupTypeTools = function(content) {
			if(!argue.topic.opts.editable) {
				return false;
			}
			
			for (var i = 0; i < opts.types.length; i++) {
				var type = opts.types[i];
				var parentId = $(content).data(opts.parentEntityName + 'Id');
				var typeTitle = $(that.getTypeContainer(type, parentId)).find('h2');
				var restoreTitle = $.tmpl(entityName + 'RestoreTitle', { typeText: $(typeTitle).text() });
				var fragment = $.tmpl(entityName + "TypeTools", {
					parentId: parentId,
					type: type,
					typeText: $(typeTitle).text()
				});
				$(fragment).find('li[data-dialog="restore"] a').data('restoreTitle', restoreTitle).click(function() {
					restoreDialog.load($(this).data('ajaxUrl'), $(this).data('restoreTitle'), entityName);
				});
				$(typeTitle).after(fragment);
			}
		};
		
		// init
		this.createForms = {
			setup: function(content) {
				if(!argue.topic.opts.editable) {
					return false;
				}
				for (var i = 0; i < opts.types.length; i++) {
					var type = opts.types[i];
					var baseTabindex = (i + 1) * 10;
					var parentId = $(content).data(opts.parentEntityName + 'Id');
					var fragment = $.tmpl(entityName + "CreateForm", {
						parentId: parentId,
						type: type,
						textTabindex: baseTabindex,
						submitButtonTabindex: baseTabindex+1
					});
					$(fragment).submit(that.createForms.onSubmit).ajaxSubmit({'onSuccess': that.createForms.onSuccess}).find('textarea').textareaHotkeys();
					$(that.getTypeContainer(type, parentId)).append(fragment);
				}
				
				if($(content).find('li.box').length <= 3) {
					$(content).find('.create-' + entityName + '-form:first textarea').focus();
				}
			},
			onSubmit: function(event) {
				if(!$(this).find('textarea').val()) {
					argue.stopEvent(event);
				} 
			},
			onSuccess: function(form) {
				$(form).find('textarea, input[type="text"]').val('');
			}
		};
		this.setupTools = function(content) {
			$(content).find('.box').each(function() {
				boxHandler.addTools(this);
			});
		};
		
		var handlerSetupDone = false;
		this.setupEventHandler = function() {
			if(handlerSetupDone) {
				return false;
			}
			
			var routeEvent = function(handler) {
				return function(event, data) {
					var entity = that.getEntity(data.id);
					if($(entity).length > 0) {
						handler(event, data, entity);
					}
				};
			};
			var triggerPostCreate = function(event, data) {
				$(argue).triggerHandler(entityName + '.postCreate', data);
			};
			
			$(argue).on(entityName + '.editRelevance', routeEvent(boxHandler.relevance.edit));
			$(argue).on(entityName + '.edit', routeEvent(boxHandler.edit));
			$(argue).on(entityName + '.delete', routeEvent(boxHandler.remove));
			$(argue).on(entityName + '.postDelete', that.onDelete);
			$(argue).on(entityName + '.create', that.add);
			$(argue).on(entityName + '.create', triggerPostCreate);
			handlerSetupDone = true;
		};
		
		$(argue).on(opts.viewName + '.loaded.argueBox.setupEvents', that.setupEventHandler);
		$(argue).on(opts.viewName + '.loaded.argueBox.createForms', argue.waitForTmpl(that.createForms.setup));
		$(argue).on(opts.viewName + '.loaded.argueBox.setupTools', argue.waitForTmpl(that.setupTools));
		$(argue).on(opts.viewName + '.loaded.argueBox.setupReorderTool', argue.waitForTmpl(that.setupReorderTool));
		$(argue).on(opts.viewName + '.loaded.argueBox.setupTypeTools', argue.waitForTmpl(that.setupTypeTools));
	};
	ArgueBoxGroup.defaults = {
		parentList: null,
		parentEntityName: null,
		viewName: 'topic',
		types: []
	};
	
	
	// arguments
	var argumentList = new ArgueBoxGroup('.topic-content', 'argument', {
		parentEntityName: 'topic',
		viewName: 'proContra',
		types: ['PRO', 'CONTRA']
	});

	argumentList.boxHandler.updateStats = function(box, childContent) {
		$(box).find('.stats-box-secondary-split .stats-box-secondary').remove();
		$.tmpl("argumentFactStat", {
			numberOfConfirmativeFacts: $(childContent).find('.CONFIRMATIVE > ul').children().length,
			numberOfDebilitativeFacts: $(childContent).find('.DEBILITATIVE > ul').children().length,
			url: $(box).find('a.box-text').attr('href')
		}).appendTo($(box).find('.stats-box-secondary-split'));
	};
	
	argumentList.setupEditThesesTool = function(content) {
		$(content).find('.box-type-tools li[data-dialog="edit-proContraTheses"] a').click(editTopicThesesDialog.open);
	};
	$(argue).on('proContra.loaded.argueBox.setupEditThesesTool', argue.waitForTmpl(argumentList.setupEditThesesTool));
	
	// facts
	var factList = new ArgueBoxGroup('.argument-content', 'fact', {
		parentList: argumentList,
		parentEntityName: 'argument',
		viewName: 'argument',
		types: ['CONFIRMATIVE', 'DEBILITATIVE']
	});
	
	factList.boxHandler.setNumberOfReferences = function(event, referenceList) {
		var box = $(referenceList).closest('.box');
		
		var numberOfReferences = $(referenceList).children('span:not(.create-reference)').length;
		$(box).find('.stats-box-secondary').remove();
		$.tmpl('factReferenceStat', {
			numberOfReferences: numberOfReferences
		}).appendTo($(box).find('.stats-box'));
	};
	$(argue).on('reference.postCreate reference.postDelete', factList.boxHandler.setNumberOfReferences);
	
	// references
	var createReferenceDialog = {
		init: function() {
			$('#create-reference form').submit(createReferenceDialog.onSubmit).ajaxSubmit({'onSuccess': createReferenceDialog.close});
		},
		open: function() {
			// context of this: button to add a reference inside a fact box
			var box = $(this).closest('.box');
			$('#create-reference input[name="factId"]').val($(box).data('factId'));
			$('#create-reference').modal('show');
			$('#create-reference').find('input[type="text"]').val('').focus();
		},
		close: function() {
			$('#create-reference').modal('hide');
		},
		onSubmit: function(event) {
			if(!$(this).find('input[type="text"]').val()) {
				argue.stopEvent(event);
			}
		}
	};
	$(argue).on('templates.loaded.createReferenceDialog', createReferenceDialog.init);

	var reference = {
		create: function(data) {
			var fragment = $.tmpl("reference", data);
			reference.setupRemoveTool(fragment);
			return fragment;
		},
		setupRemoveTool: function(element) {
			$.tmpl("deleteReferenceForm", {
				referenceId: $(element).data('referenceId')
			}).appendTo(element);
			$(element).find('.delete-reference-form').ajaxSubmit().find('.icon-trash').submitClosest('click', $('#js-confirmDeleteReference').text());
		}
	};
	
	var references = {
		add: function(event, data) {
			var list = $('.fact').filterByData('factId', data.factId).find('.references');
			
			if(references.get(list, data.id).length === 0) {
				var newReference = reference.create(data);
				
				if($(list).children('.create-reference').length > 0) {
					$(list).children('.create-reference').before(newReference);
				} else {
					$(list).append(newReference);
				}
				
				$(argue).trigger('reference.postCreate', list);
			}
		},
		get: function(list, id) {
			return $(list).find('span').filterByData('referenceId', id);
		},
		remove: function(event, data) {
			var element = references.get($('.references'), data.id);
			var list = $(element).closest('.references');
			if(element.length === 0) {
				return false;
			}
			
			$(element).remove();
			$(argue).trigger('reference.postDelete', list);
		},
		addTools: function(content) {
			if(!argue.topic.opts.editable) {
				return false;
			}

			var referenceLists = $(content).find('.references');
			$(referenceLists).find('span').each(function() { reference.setupRemoveTool(this); });
			$.tmpl("createReferenceLink").appendTo(referenceLists);
			$(referenceLists).find('.create-reference').click(createReferenceDialog.open);
		}
	};
	$(argue).on('reference.create', references.add);
	$(argue).on('reference.delete', references.remove);
	$(argue).on('argument.loaded.references.setupTools', argue.waitForTmpl(references.addTools));
	$(argue).on('fact.postCreate', function(event, data) {
		references.addTools(factList.getEntity(data.id));
	});
	
	// general
	$(argue).on('proContra.loaded.setupTopic', function(event, content) {
		$(argue).triggerHandler('topic.loaded', content);
	});
	
	$(argue).on('proContra.loaded.setupAjaxLinks argument.loaded.setupAjaxLinks', function(event, content) {
		if(argue.ajaxLinks.handler.length > 0) {
			return false;
		}
		new argue.ajaxLinkHandler('argument', 4, 'argument', function(event) { return $('#argument-content-' + event.pathNames[5]); });
		new argue.ajaxLinkHandler('topic', 0, 'proContra', function() { return $('.topic-content'); });
		argue.ajaxLinks.init();
	});

	$(argue).on('proContra.loaded.setupCommon argument.loaded.setupCommon', function(event, content) {
		$(content).find('abbr.timeago').timeago();
		$(content).find('button').blockDoubleClick();
		$(content).find('.show-on-hover-trigger').hoverOnTouch();
		$(content).find('.author-note .attribution').tooltip({placement: 'bottom'});
	});
	
	// publish
	argue.proContra = {};
	argue.proContra.reorder = reorder;
	argue.proContra.restoreDialog = restoreDialog;
	argue.proContra.editTopicThesesDialog = editTopicThesesDialog;
	argue.proContra.argueBox = ArgueBox;
	argue.proContra.argueBoxGroup = ArgueBoxGroup;
	argue.proContra.reference = reference;
	argue.proContra.argumentList = argumentList;
	argue.proContra.factList = factList;
}(argue));