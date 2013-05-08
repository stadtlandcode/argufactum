'use strict';

(function(angular, a) {
	var evaluateModule = angular.module('evaluate');

	evaluateModule.directive('dragWeight', function() {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('dragstart', function(event) {
					event.originalEvent.dataTransfer.setData('weightValue', scope.weight.value);
					event.originalEvent.dataTransfer.setData('argumentNumber', scope.argument.number);
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
					var argumentNumber = event.originalEvent.dataTransfer.getData('argumentNumber');
					var weightValue = event.originalEvent.dataTransfer.getData('weightValue');
					var weight = a.evaluate.findWeight(argumentNumber, weightValue, scope.weights);

					var argumentWeights = a.evaluate.getWeightsForArgument(weight.argument, scope.weights);
					_.each(argumentWeights, function(argumentWeight) {
						argumentWeight.attachedTo = null;
						argumentWeight.attachedToIndex = 0;
					});

					weight.attachedTo = element.data('plate');
					weight.attachedToIndex = a.evaluate.getWeightCount(scope.weights)[element.data('plate')];
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
