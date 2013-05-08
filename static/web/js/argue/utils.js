'use strict';

(function(a, _) {
	a.array = {
		remove: function(array, valueToReject) {
			var indexToReject = _.indexOf(array, valueToReject);
			array.splice(indexToReject, 1);
		}
	};
})(argue, _);
