(function($) {
	"use strict";
	$.fn.textareaHotkeys = function(options) {
		var settings = $.extend({
	      'escCallback': null
	    }, options);
		
		return this.each(function () {
			if(this.type !== 'textarea') {
				return false;
			}
			
			// submit on return
			$(this).bind('keydown', 'return', function() {
				$(this).closest('form').submit();
				return false;
			});
			
			// cancel on esc (when callback provided)
			if(settings['escCallback']) {
				$(this).bind('keydown', 'esc', function() {
					settings['escCallback']();
					return false;
				});
			}
		});
	};
}(jQuery));