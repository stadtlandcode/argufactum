(function(argue) {
	"use strict";
	
	var pageElements = {
		getContent: function() {
			return $('.topic-content');
		},
		getPageHeader: function() {
			return $('.topic-page-header');
		},
		getTitle: function() {
			return pageElements.getPageHeader().find('h1');
		},
		getTerm: function() {
			return pageElements.getTitle().children('span');
		},
		getTermOnArguments: function() {
			return $('.argument-page-header h1 a');
		},
		getDefinition: function() {
			return pageElements.getPageHeader().find('.definition');
		},
		getCategory: function() {
			return pageElements.getTitle().children('small');
		},
		getCreationLog: function() {
			return pageElements.getPageHeader().find('.author-note');
		}
	};
	
	
	var editDialog = {
		init: function() {
			$('.edit-topic-form input[name="id"]').val(argue.topic.getId());
			$('.edit-topic-form').submit(editDialog.handleSubmit).ajaxSubmit({'onSuccess': editDialog.close});
		},
		handleSubmit: function(event) {
			var changed = false;
			if($(this).find('input[name="term"]').val()) {
				var termChanged = $(this).find('input[name="term"]').val() != pageElements.getTerm().text();
				var definitionChanged = $(this).find('input[name="definition"]').val() != pageElements.getDefinition().text();
				var categoryChanged = $(this).find('select').val() != pageElements.getCategory().data('categoryId');
				changed = termChanged || definitionChanged || categoryChanged;
			}
			
			if(!changed) {
				editDialog.close();
				argue.stopEvent(event);
			}
		},
		setAlert: function(tmpl) {
			$('#edit-topic .alert').remove();
			if(tmpl) {
				$('#edit-topic .modal-body').append(tmpl);
			}
		},
		isOpen: function() {
			return $('#edit-topic').is(':visible');
		},
		open: function() {
			$('#edit-topic').modal('show');
			editDialog.setAlert();
			$('.edit-topic-form input[name="term"]').val(pageElements.getTerm().text()).focus();
			$('.edit-topic-form input[name="definition"]').val(pageElements.getDefinition().text());
			$('.edit-topic-form select').val(pageElements.getCategory().data('categoryId'));
		},
		close: function() {
			$('#edit-topic').modal('hide');
		}
	};
	$(argue).on('templates.loaded.editDialog', editDialog.init);
	
	var edit = function(event, data) {
		if(pageElements.getPageHeader().data('termId') != data.termId) {
			pageElements.getPageHeader().data('termId', data.termId);
			pageElements.getTerm().text(data.text);
			pageElements.getTermOnArguments().text(data.text);
			pageElements.getDefinition().text(data.definition);
			pageElements.getCreationLog().updateModificationLog(data);

			if(editDialog.isOpen() && data.modificationLog && data.modificationLog.userId !== argue.getUserId()) {
				editDialog.setAlert($.tmpl("topicEditedByAnotherUser", { term: data.text }));
			}
		}
		
		if(data.category) {
			if(pageElements.getCategory().length <= 0) {
				pageElements.getTitle().append($('<small></small>'));
			}
			pageElements.getCategory().data('categoryId', data.category.id).text(data.category.name);
		} else {
			pageElements.getCategory().remove();
		}
	};
	$(argue).on('topic.editTerm', edit);
	
	
	var shareDialog = {
		init: function() {
			shareDialog.visibilityForm.init();
			shareDialog.accessRightForm.init();	
		},
		visibilityForm: {
			init: function() {
				$('#set-visibility-form input[name="id"]').val(pageElements.getContent().data('topicId'));
				$('#set-visibility-form').submit(shareDialog.visibilityForm.handleSubmit).ajaxSubmit({ 'onError': shareDialog.visibilityForm.reset });
			},
			getCheckbox: function() {
				return $('#set-visibility-form input[name="visibility"]');
			},
			reset: function() {
				shareDialog.visibilityForm.getCheckbox().prop('checked', argue.topic.isPublic());
			},
			handleSubmit: function(event) {
				if(shareDialog.visibilityForm.getCheckbox().is(':checked') === argue.topic.isPublic()) {
					argue.stopEvent(event);
				}
			}
		},
		accessRightForm: {
			init: function() {
				$('#add-access-right-form input[name="id"]').val(pageElements.getContent().data('topicId'));
				$('#add-access-right-form').submit(shareDialog.accessRightForm.handleSubmit).ajaxSubmit({ 'onSuccess': shareDialog.accessRightForm.reset });
				$('#add-access-right-form input[name="contact"]').autocomplete();
			},
			getContactInput: function() {
				return $('#add-access-right-form input[name="contact"]');
			},
			reset: function() {
				shareDialog.accessRightForm.getContactInput().val('');
				$('#add-access-right-form .alert').remove();
			},
			handleSubmit: function(event) {
				if(!$(this).find('input[name="contact"]').val()) {
					argue.stopEvent(event);
				}
			}
		},
		open: function(accessRightsUrl) {
			$('#share-topic').modal('show');
			shareDialog.visibilityForm.reset();
			shareDialog.accessRightForm.reset();
			shareDialog.accessRightForm.getContactInput().focus();
			
			if(!shareDialog.accessRightsLoaded()) {
				$.tmpl('shareTopicLoader').appendTo('.access-rights-list tbody');
				shareDialog.loadAccessRights(accessRightsUrl);
			}
		},
		loadAccessRights: function(accessRightsUrl) {
			var jqXHR = $.get(accessRightsUrl);
			jqXHR.done(shareDialog.listAccessRights);
		},
		listAccessRights: function(data) {
			if(!data) {
				return false;
			}
			
			$('.access-rights-list tbody tr').remove();
			$.each(data.accessRights, function(index, value) {
				shareDialog.addAccessRight(value);
			});
		},
		accessRightsLoaded: function() {
			return $('.access-rights-list tbody tr').length > 0;
		},
		addAccessRight: function(data) {
			var tmpl = $.tmpl('accessRight', data);
			tmpl.appendTo($('.access-rights-list tbody'));
			tmpl.find('form').ajaxSubmit().find('i').submitClosest('click');
		},
		removeAccessRight: function(data) {
			$('.access-rights-list tbody tr[data-access-right-id="' + data.id + '"]').remove();
		}
	};
	$(argue).on('templates.loaded.shareDialog', shareDialog.init);
	
	var accessRights = {
		add: function(event, data) {
			if(data.writeAccess) {
				argue.comet.subscribe(argue.topic.opts.cometUrl);
			}
			if(shareDialog.accessRightsLoaded()) {
				shareDialog.addAccessRight(data);
			}
		},
		remove: function(event, data) {
			var affected = data.userId === argue.getUserId();
			if(affected) {
				var effect = argue.topic.isPublic() ? 'readOnly' : 'absolutely';
				$('#access-denied span').hide().filter('.access-denied-' + effect).show();
				$('#access-denied').modal('show');
				$('#access-denied').on('hide', function() {
					if(effect === 'absolutely') {
						argue.redirect($('#access-denied').data('exitUrl'));
					} else {
						argue.reload();
					}
				});
			}

			shareDialog.removeAccessRight(data);
		}
	};
	$(argue).on('topic.addAccessRight', accessRights.add);
	$(argue).on('topic.removeAccessRight', accessRights.remove);
	
	var editVisibility = function(event, data) {
		pageElements.getPageHeader().data('visibility', data.visibility);
		if(data.visibility === 'PRIVATE') {
			$('.copyright').remove();
		} else {
			$('footer').before($.tmpl('copyright'));
		}
	};
	$(argue).on('topic.editVisibility', editVisibility);
	
	var restoreTerm = function() {
		argue.history.closeDialog();
		editDialog.open();
		$('.edit-topic-form').find('input[name="term"]').val($(this).siblings('span').text());
		$('.edit-topic-form').find('input[name="definition"]').val($(this).siblings('.definition').text());
		$('.edit-topic-form').submit();
	};
	
	var addTools = function() {
		if(!argue.topic.opts.editable) {
			return false;
		}
		
		$.tmpl('topicTools', {
			topicId: argue.topic.getId()
		}).prependTo(pageElements.getPageHeader());
		
		$('.edit-topic-button').click(editDialog.open);
		$('.share-topic-button').click(function() { shareDialog.open($(this).data('accessRightsUrl')); });
		$('.topic-copy-button').click(function() { $(this).siblings('form').submit(); });
		$('.topic-history-button').click(function() { argue.history.load($(this).data('historyUrl'), restoreTerm); });
	};
	$(argue).on('topic.loaded.tools', argue.waitForTmpl(addTools));
	
	var statusBar = {
		onCometStatusChange: function(event, state) {
			$('#status').showConditional(state === 'disconnect');
		}
	};
	$(argue).on('comet.stateChange.statusBar', statusBar.onCometStatusChange);
	
	var sessionTimeoutDialog = {
		init: function() {
			$('#session-timeout .btn-reload').click(function() {
				location.reload();
			});
		},
		open: function() {
			$('#session-timeout').modal('show');
		}
	};
	$(argue).on('templates.loaded.sessionTimeoutDialog', sessionTimeoutDialog.init);
	$(argue).on('sessionTimeout.dialog', sessionTimeoutDialog.open);
	
	var requiresLoginDialog = {
		init: function() {
			$('button.requires-login').click(requiresLoginDialog.open);
		},
		open: function() {
			$('#requires-login').modal('show');
		}
	}
	$(argue).on('templates.loaded.requiresLogin', requiresLoginDialog.init);
	
	var vote = {
		blockSubmit: false,
		init: function() {
			$('#vote-topic-form').ajaxSubmit({'onSuccess': vote.onSuccess, 'onError': vote.onError, 'onComplete': vote.onComplete});
			$('#vote-topic-form button').click(vote.onClick);
		},
		resetButtons: function(formElement) {
			$(formElement).find('buttons').removeClass('active');
			if($(formElement).data('vote') === 'true') {
				$(formElement).find('button[data-vote="up"]').addClass('active');
			}
			if($(formElement).data('vote') === 'false') {
				$(formElement).find('button[data-vote="down"]').addClass('active');
			}
		},
		onClick: function(event) {
			if(!vote.blockSubmit) {
				$(this).siblings('button').removeClass('active');
				$(this).toggleClass('active');
				$(this).siblings('input[name="vote"]').val($(this).data('vote'));
				vote.setFormMethod($(this).parent());
				vote.blockSubmit = true;
				$(this).parent().submit();
			}
		},
		setFormMethod: function(formElement) {
			var formMethod = $(formElement).find('.active').length > 0 ? "POST" : "DELETE";
			$(formElement).find('input[name="_method"]').val(formMethod);
		},
		onSuccess: function(formElement, data) {
			$(formElement).data('vote', data.vote);
			vote.resetButtons(formElement);
		},
		onError: function(formElement, jqXHR) {
			vote.resetButtons(formElement);
		},
		onComplete: function(formElement) {
			vote.blockSubmit = false;
		}
	};
	$(argue).on('topic.loaded.vote', vote.init);
	
	argue.topic = {
		opts: {},
		init: function(options) {
			var opts = $.extend(argue.topic.defaults, options);
			argue.topic.opts = opts;
			
			argue.url.setContextPath(opts.contextPath);
			argue.templates.load(opts.templateUrl);
			if(opts.subscribeCometUrl) {
				argue.comet.subscribe(opts.cometUrl);
			}
			
			$(argue).triggerHandler(opts.viewName + '.loaded', $('.content:first'));
		},
		getId: function() {
			return pageElements.getContent().data('topicId');
		},
		isPublic: function() {
			return pageElements.getPageHeader().data('visibility') === 'PUBLIC';
		}
	};
	argue.topic.defaults = {
		contextPath: '/',
		cometUrl: null,
		templateUrl: null,
		viewName: null,
		subscribeCometUrl: false,
		editable: false,
		voteable: false
	};
	
	argue.history = {
		load: function(url, restoreFunction) {
			var jqXHR = argue.get(url);
			jqXHR.done(function(data) {
				argue.history.openDialog(data, restoreFunction);
			});
		},
		openDialog: function(data, restoreFunction) {
			argue.assertValidData(data);
			$('#history table').remove();
			$('#history .modal-body').append($.tmpl('historyTable', data));
			$('#history .modal-body abbr.timeago').timeago();
			$('#history button').click(restoreFunction);
			$('#history').modal('show');
		},
		closeDialog: function() {
			$('#history').modal('hide');
		}
	};
	
	$.fn.updateModificationLog = function(data) {
		this.find('.editor-note').remove();
		if(data && data.modificationLog) {
			var fragment = $.tmpl("modificationLog", data);
			$(fragment).find('abbr.timeago').timeago();
			this.append(fragment);
		}
		return this;
	};
}(argue));
