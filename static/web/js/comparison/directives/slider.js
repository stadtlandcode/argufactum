'use strict';

(function(angular) {
	var tableModule = angular.module('table');

	tableModule.directive('slider', function() {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				$(element).slider({
					min: 0,
					max: 100,
					value: 50,
					slide: function(event, ui) {
						scope.setRating(ui.value);
					}
				});
			}
		};
	});
})(angular);
