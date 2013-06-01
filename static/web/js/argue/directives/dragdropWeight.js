'use strict';

(function(angular, a) {
	var evaluateModule = angular.module('evaluate');

	evaluateModule.directive('dragWeight', function() {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('dragstart', function(event) {
					event.originalEvent.dataTransfer.setData('weightId', scope.weight.id);
				});
			}
		};
	});

	evaluateModule.directive('dropWeight', function() {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('drop', function(event) {
					event.preventDefault();
					var weightId = event.originalEvent.dataTransfer.getData('weightId');
					var weight = a.evaluate.findWeight(scope.weights, weightId);

					weight.attachedTo = element.data('plate');
					scope.scale = a.evaluate.calculateScale(scope.weights);
					scope.$digest();
				});
				element.on('dragover', function(event) {
					event.preventDefault();
				});
			}
		};
	});
})(angular, argue);
