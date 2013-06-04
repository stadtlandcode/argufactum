'use strict';

(function(angular, a, _) {
	var evaluateModule = angular.module('evaluate');

	evaluateModule.directive('dragWeight', function() {
		return {
			restrict: 'A',
			link: function(scope, element, attrs) {
				element.on('dragstart', function(event) {
					event.originalEvent.dataTransfer.setData('weightId', scope.weight.id);

					if (scope.argument) {
						a.evaluate.assignWeight(scope.weight, scope.argument, scope.weights);
					}

					_.each(document.getElementsByClassName('droparea'), function(dropArea) {
						dropArea.setAttribute('class', 'droparea droparea-active');
					});
				});

				element.on('dragend', function(event) {
					_.each(document.getElementsByClassName('droparea'), function(dropArea) {
						dropArea.setAttribute('class', 'droparea');
					});
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

					scope.updateScale();
					scope.$digest();
				});

				element.on('dragover', function(event) {
					event.preventDefault();
				});
			}
		};
	});
})(angular, argue, _);
