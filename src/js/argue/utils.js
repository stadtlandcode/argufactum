'use strict';

(function(a, _) {
	a.array = {
		remove: function(array, valueToReject) {
			var indexToReject = _.indexOf(array, valueToReject);
			array.splice(indexToReject, 1);
		}
	};

	a.browser = {
		isSafari: function() {
			return navigator.userAgent.indexOf('Safari') != -1 && navigator.userAgent.indexOf('Chrome') == -1;
		}
	};
})(argue, _);
