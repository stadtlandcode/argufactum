var argue = { };

(function(argue) {
	"use strict";
	argue.url = function(urlFragment) {
		return argue.url.contextPath && urlFragment.indexOf(argue.url.contextPath) !== 0 ? argue.url.contextPath + urlFragment : urlFragment;
	};
	argue.url.contextPath = null;
	argue.url.setContextPath = function(contextPath) {
		argue.url.contextPath = (contextPath.charAt(contextPath.length-1) === "/") ? contextPath.substring(0, contextPath.length-1) : contextPath;
	};
	
	argue.assertValidData = function(data) {
		if(data === null || data === undefined || typeof data !== "object") {
			throw new Error('invalid data');
		}
	};
	
	argue.subnav = {
		init: function() {
			$(document).on('scroll', argue.subnav.onScroll);
			$(document).triggerHandler('scroll');
		},
		onScroll: function(){
		    if(!$('.subnav').data('top') && !$('.subnav').hasClass('subnav-fixed')) {
		        var offset = $('.subnav').offset()
		        $('.subnav').data('top', offset.top);
		    }
		    $('.subnav').addOrRemoveClass('subnav-fixed', ($('.subnav').data('top') - $('.subnav').outerHeight()) <= $(this).scrollTop());
		}
	};
	
	argue.stopEvent = function(event) {
		event.stopImmediatePropagation();
		event.preventDefault();
	};
	
	argue.confirm = function(text) {
		return confirm(text);
	};
	
	argue.redirect = function(url) {
		window.location.href = $('#access-denied').data('exit-url');
	};
	argue.reload = function() {
		window.location.reload();
	};
	
	argue.getUserId = function() {
		return $('#user').data('user-id');
	};
	
	argue.camelCase = function(text) {
		return text.replace(/-([a-z])/g, function (t) { return t[1].toUpperCase() });
	};	
	
	argue.waitForTmpl = function(eventFunction) {
		return function(event, attr) {
			if(argue.templates.loaded) {
				eventFunction(attr);
			} else {
				argue.waitForTmpl.queue.push({'f': eventFunction, 'attr': attr});
			}
		}
	};
	argue.waitForTmpl.queue = [];
	argue.waitForTmpl.executeQueue = function() {
		for(var i = 0; i < argue.waitForTmpl.queue.length; i++) {
			argue.waitForTmpl.queue[i].f(argue.waitForTmpl.queue[i].attr);
		}
		argue.waitForTmpl.queue = [];
	};
	$(argue).on('templates.loaded.executeDependentEvents', argue.waitForTmpl.executeQueue);
	
	argue.templates = {
		idPrefix: 'js-tmpl-',
		loaded: false,
		load: function(url) {
			if(!url) {
				return false;
			}
			
			var jqXHR = argue.get(url);
			jqXHR.done(argue.templates.handleData);
			return jqXHR;
		},
		isValidData: function(data) {
			return data && data.indexOf(argue.templates.idPrefix) > 0;
		},
		handleData: function (data) {
			if(!argue.templates.isValidData(data)) {
				return false;
			}
			
			$('body').append(data);
			argue.templates.scan();
			$(argue).triggerHandler('templates.loaded');
			argue.templates.loaded = true;
			return true;
		},
		scan: function() {
			$('script[id]').each(function() {
				var id = $(this).attr('id');
				if(id.indexOf(argue.templates.idPrefix) >= 0) {
					$(this).template(id.substr(argue.templates.idPrefix.length));
				}
			});
		}
	};
	
	argue.init = {
		dialogs: function() {
			$('.modal-tmpl').addClass('modal').modal({show: false});
		}
	}
	$(argue).on('templates.loaded.dialogs', argue.init.dialogs);
	
	$.fn.convertEmail = function(options) {
		var opts = $.extend({}, $.fn.convertEmail.defaults, options);
		
		return this.each(function() {
			var source = opts.source === 'text' ? $(this).text() : $(this).attr(opts.source);
			var email = source.replace(' at ', '@');
			
			$(this).attr('href', 'mailto:' + email);
			if(opts.source === 'text') {
				$(this).text(email);
			} else {
				$(this).attr(opts.source, email);
			}
		});
	};
	$.fn.convertEmail.defaults = {
		source: 'text'
	};
	
	$.fn.initPopover = function() {
		return this.popover({trigger: 'focus'}).focusin(function() { $(this).next('.help-line').hide(); }).focusout(function() { $(this).next('.help-line').show(); });
	};
	
	$.fn.submitOnReturn = function() {
		return this.bind('keydown', 'return', function() {
			$(this).closest('form').submit();
			return false;
		});
	};
	
	$.fn.showConditional = function(condition) {
		return (condition) ? $(this).show() : $(this).hide();
	};
	
	$.fn.addOrRemoveClass = function(cssClass, condition) {
		return (condition) ? $(this).addClass(cssClass) : $(this).removeClass(cssClass);
	};
	
	$.fn.filterByData = function(key, value) {
		return $(this).filter(function() {
			return $(this).data(key) === value;
		});
	};
	
	$.fn.hoverOnTouch = function() {
		var elements = this;
		var activateAll = function() {
			$(elements).addClass('hover-in');
		};
		$('body').on('touchstart', activateAll);
		return this;
	};
	
	$.fn.submitClosest = function(event, text) {
		var submit = function () {
			if(!text || argue.confirm(text)) {
				$(this).closest('form').submit();
			}
		}

		return this.each(function() {
			if(!event) {
				submit.call(this);
			} else {
				$(this).on(event, submit);
			}
		});
	};

	var autocompleteRequestActive = false;
	$.fn.autocomplete = function() {
		var loadSuggestions = function() {
			var element = $(this);
			if(!autocompleteRequestActive && (($(element).data('minLength') > 0 && $(element).val().length >= $(element).data('minLength')) || (!$(element).data('source') && $(element).data('minLength') < 1))) {
				autocompleteRequestActive = true;
				var jqXHR = $.get($(element).data('sourceUrl') + $(element).val());
				jqXHR.done(function(data) {
					$(element).data('typeahead', false).data('source', data.suggestions).attr('autocomplete', 'off').typeahead({source: data.suggestions});
				});
				jqXHR.always(function() {
					autocompleteRequestActive = false;
				});
			}
		};
		
		return this.on('keyup', loadSuggestions);
	};
	
	$.fn.siblingsByValue = function(selector, target, orderedBy) {
		return (orderedBy === 'desc' && target === 'greater') || (orderedBy === 'asc' && target === 'lower') ? $(this).prevAll(selector) : $(this).nextAll(selector);
	};

	$.fn.nthChildInDirection = function(selector, number, orderedBy) {
		var numberInDirection = number - 1;
		if(orderedBy === 'desc') {
			numberInDirection = $(this).children(selector).length - number;
		}
		return $(this).children(selector).eq(numberInDirection);
	};
	
	$.fn.blockDoubleClick = function(options) {
		var opts = $.extend({}, $.fn.blockDoubleClick.defaults, options);
		
		var handleClick = function() {
			if($(this).data('blockNextClick')) {
				return false;
			} else {
				setTimeout($.proxy(disableClick, $(this)), opts.delay);
				return true;
			}
		}
		
		var disableClick = function() {
			$(this).data('blockNextClick', true);
			setTimeout($.proxy(enableClick, $(this)), opts.period);
		}
		
		var enableClick = function() {
			$(this).removeData('blockNextClick');
		}
		
		return this.on('click.blockDoubleClick', handleClick);
	};
	$.fn.blockDoubleClick.defaults = {
		delay: 50,
		period: 2000
	};
	
	$.fn.stars = function(options) {
		var opts = $.extend({}, $.fn.stars.defaults, options);
		var mouseOut = true;
		
		var hoverContainer = function(event) {
			mouseOut = event.type === 'mouseleave';
			$(this).addOrRemoveClass('star-rating-read', event.type === 'mouseleave');
		};
		var unlock = function() {
			if(!mouseOut) {
				$(this).removeClass('star-rating-read');
			}
		};
		var getOpts = function(children) {
			return $(children).parent().data('starOpts');
		};
		
		var hoverRating = function(event) {
			$(this).siblingsByValue('.star-rating', 'lower', getOpts(this).order).andSelf().addOrRemoveClass('star-rating-hover', event.type === 'mouseenter');
		};
		var clickRating = function() {
			if(!$(this).parent().hasClass('star-rating-read')) {
				var numberOfStars = 0;
				if($(this).hasClass('star-rating')) {
					activateStar.call(this);
					numberOfStars = $(this).siblings('.star-rating-on').andSelf().length;
				} else {
					$(this).siblings('.star-rating-on').removeClass('star-rating-on');
				}
				
				if(getOpts(this).lockOnChange) {
					$(this).parent().addClass('star-rating-read');
				}
				$(this).trigger('rating.change.byClick', numberOfStars);
			}
		};
		var activateStar = function() {
			$(this).siblingsByValue('.star-rating', 'greater', getOpts(this).order).removeClass('star-rating-on');
			$(this).siblingsByValue('.star-rating', 'lower', getOpts(this).order).andSelf().addClass('star-rating-on');
		};
		
		var setIsRated = function() {
			$(this).addOrRemoveClass('is-rated', $(this).children('.star-rating-on').length > 0);
		};

		return this.each(function() {
			if(typeof options === 'number') {
				if($(this).data('starOpts') !== undefined) {
					if(options > 0) { 
						var star = $(this).nthChildInDirection('.star-rating', options, $(this).data('starOpts').order);
						activateStar.call(star);
					} else {
						$(this).children().removeClass('star-rating-on');
					}
					$(this).trigger('rating.change.manual');
				}
			} else {
				if($(this).data('starOpts') === undefined && !opts.readOnly) {
					$(this).hover(hoverContainer);
					$(this).children('.rating-cancel, .star-rating').hover(hoverRating).click(clickRating);
					$(this).on('rating.change.manual rating.change.byClick', setIsRated);
					if(opts.lockOnChange) {
						$(this).on('rating.unlock', unlock);
					}
				}
				$(this).data('starOpts', opts);
			}
		});
	};
	$.fn.stars.defaults = {
		order: 'desc',
		readOnly: false,
		lockOnChange: true
	};
}(argue));
