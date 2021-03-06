'use strict';

(function(angular) {
	var tableModule = angular.module('table');

	tableModule.directive('slider', function() {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				$(element).slider({
					min: 0,
					max: 10,
					value: scope.ratingOfCriterium(scope.criterium),
					slide: function(event, ui) {
						scope.setRating(event.target.dataset['objectId'], ui.value);
					}
				});
			}
		};
	});
})(angular);
