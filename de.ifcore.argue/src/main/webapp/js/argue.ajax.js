(function($, argue) {
	"use strict";
	
	$(document).ajaxError(function(event, jqXHR) {
		if(jqXHR.status === 901) {
			$(argue).triggerHandler('sessionTimeout');
		}
	});
	
	var triggerHandler = function(data) {
		if(data && data.broadcastSubject) {
			$(argue).triggerHandler(data.broadcastSubject, data);
		}
	};
	
	argue.comet = {
		resourceId: $.atmosphere.guid(),
		isSubscribed: function() {
			return $.atmosphere.requests.length > 0;
		},
		triggerConnect: function() {
			$(argue).triggerHandler('comet.stateChange', 'connect');
			$(argue).triggerHandler('comet.connected');
		},
		triggerDisconnect: function() {
			$(argue).triggerHandler('comet.stateChange', 'disconnect');
			$(argue).triggerHandler('comet.disconnected');
		},
		subscribe: function(url) {
			if(!url || argue.comet.isSubscribed()) {
				return false;
			}
			
			var request = {
				url: url + '&resourceId=' + argue.comet.resourceId,
				transport: 'long-polling',
				fallbackTransport: 'long-polling',
				timeout: 900000,
				maxRequest: 10,
				onMessage: argue.comet.handleMessage,
				onOpen: argue.comet.triggerConnect,
				onReconnect: argue.comet.triggerConnect,
				onClose: argue.comet.triggerDisconnect
			};
			$.atmosphere.subscribe(request);
		},
		unsubscribe: function() {
			$.atmosphere.unsubscribe();
		},
		handleMessage: function(response) {
			if(response.status === 200) {
				var data = response.responseBody;
				if(data && data.length > 0) {
					triggerHandler($.parseJSON(data));
				}
			}
		}
	};
	
	argue.post = function(url, data) {
		if(argue.comet.isSubscribed()) {
			data.push({'name' : 'cometResourceId', 'value' : argue.comet.resourceId});
		}
		return $.post(url, data);
	};
	
	argue.get = function(url) {
		var jqXHR = $.get(argue.url(url));
		var contentId = $('.content:visible').attr('id');

		var getContainer = function() {
			return $('#' + contentId).find('.global-ajax-indicator-container');
		}
		var decreaseCounter = function() {
			argue.get.counter[contentId]--;
			if(argue.get.counter[contentId] === 0) {
				getContainer().removeAjaxIndicator();
			}
		}

		if(argue.get.counter[contentId] === undefined) {
			argue.get.counter[contentId] = 0;
		}
		argue.get.counter[contentId]++;
		getContainer().addAjaxIndicator();
		jqXHR.always(decreaseCounter);
		return jqXHR;
		
	};
	argue.get.counter = {};
	
	argue.ajaxLinkHandler = function(pathName, pathNameIndex, viewName, contentProvider) {
		var that = this;
		argue.ajaxLinks.handler.push(that);
		
		this.handles = function(event) {
			return (event.pathNames[pathNameIndex] && event.pathNames[pathNameIndex] === pathName);
		};
		
		this.handle = function (event) {
			var content = contentProvider(event);
			if(content.length <= 0) {
				var jqXHR = argue.get(event.path + '/ajax');
				jqXHR.done(that.appendContent);
			} else {
				that.show(content);
			}
		};
		
		this.appendContent = function(data) {
			var previousContentLength = $('.content').length;
			$('.content:last').after(data);
			if(previousContentLength === $('.content').length) {
				return false;
			}
			
			var content = $('.content:last');
			if($(content).attr('id') === $(content).prev().attr('id')) {
				$(content).remove();
				return false;
			}
			
			$(argue).triggerHandler(viewName + '.loaded', content);
			that.show(content);
		}
		
		this.show = function(element) {
			$('.content').hide();
			$(element).show();
			$('html').scrollTop(0);
			
			that.swapDocumentTitle(element);
		};
		
		this.swapDocumentTitle = function(element) {
			if($(element).data('page-title')) {
				document.title = $(element).data('page-title').replace(/(\r\n|\n|\r)/gm,"");
			} else {
				document.title = argue.ajaxLinks.firstDocumentTitle;
			}
		};
	};
	
	argue.ajaxLinks = {
		firstDocumentTitle: document.title,
		handler: [],
		init: function() {
			if(window.history.pushState === undefined) {
				return false;
			}
			
			$.address.state('/').init(function() {
				$('a.ajax-link').address();
			}).change(argue.ajaxLinks.onAddressChange);
		},
		onAddressChange: function (event) {
			for (var i=0; i < argue.ajaxLinks.handler.length; i++) {
				var handler = argue.ajaxLinks.handler[i];
				if(handler.handles(event)) {
					handler.handle(event);
					break;
				}
			}
		}
	};
	
	argue.listAjaxErrors = function(element, jqXHR) {
		if(!jqXHR || !jqXHR.responseText) {
			return false;
		}
		
		var result = $.parseJSON(jqXHR.responseText);
		var errorMsg = '';
		var appendToErrorMsg = function (index, value) {
			if(errorMsg.length > 0) {
				errorMsg += '<br />';
			}
			errorMsg += value;
		}
		
		$.each(result.globalErrors, appendToErrorMsg);
		$.each(result.fieldErrors, appendToErrorMsg);
		
		$(element).find('.alert').remove();
		$.tmpl('error', {
			'errorMsg': errorMsg
		}).prependTo(element);
	};
	
	$.fn.addAjaxIndicator = function() {
		return this.each(function() {
			if($(this).find('.ajax-indicator').length > 0) {
				return false;
			}
			
			var element = $(this);
			if($(this).is('form') && $(this).find('button').length > 0) {
				element = $(this).find('button:last').parent();
			}
			
			$(element).append('<div class="ajax-indicator" />');
		});
	};
	$.fn.removeAjaxIndicator = function() {
		return this.find('.ajax-indicator').remove();
	};
	
	$.fn.ajaxSubmit = function(options) {
		var opts = $.extend({}, $.fn.ajaxSubmit.defaults, options);
		var formElement = this;
		
		var callErrorHandler = function(jqXHR) {
			if(opts.onError) {
				opts.onError(formElement, jqXHR);
			}
		};
		
		var callSuccessHandler = function(data, successCode, jqXHR) {
			if(!data) {
				callErrorHandler(jqXHR);
			}
			else {
				if(opts.triggerHandler) {
					triggerHandler(data);
				}
				if(opts.onSuccess) {
					opts.onSuccess(formElement, data);
				}
			}
		};
		
		var callCompleteHandler = function() {
			$(formElement).removeAjaxIndicator();
			if(opts.onComplete) {
				opts.onComplete(formElement);
			}
		};
		
		return formElement.submit(function() {
			var jqXHR = argue.post($(this).attr('action'), $(this).serializeArray());
			jqXHR.done(callSuccessHandler);
			jqXHR.fail(callErrorHandler);
			jqXHR.always(callCompleteHandler);

			if(opts.indicateAjax) {
				$(this).addAjaxIndicator();
			}
			
			return false;
		});
	};
	$.fn.ajaxSubmit.defaults = {
		indicateAjax: true,
		triggerHandler: true,
		onSuccess: null,
		onError: argue.listAjaxErrors,
		onComplete: null
	};
}(jQuery, argue));