'use strict';

(function(q, _) {
	q.array = {
		remove: function(array, valueToReject) {
			var indexToReject = _.indexOf(array, valueToReject);
			array.splice(indexToReject, 1);
		}
	};
})(questionnaire, _);
