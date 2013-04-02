'use strict';

(function(c, _) {
	c.array = {
		remove: function(array, valueToReject) {
			var indexToReject = _.indexOf(array, valueToReject);
			array.splice(indexToReject, 1);
		},
		rejectOne: function(array, iterator) {
			var valueToReject = _.find(array, iterator);
			if (valueToReject) {
				this.remove(array, valueToReject);
			}
		},
		rejectAll: function(array, iterator) {
			var valuesToReject = _.filter(array, iterator);
			_.each(valuesToReject, _.bind(function(valueToReject) {
				this.remove(array, valueToReject);
			}, this));
		}
	};
})(comparison, _);
