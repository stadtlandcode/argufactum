'use strict';

(function(angular, a, _) {
	var evaluateModule = angular.module('evaluate', []);

	a.storage = {
		findArgument: function(number) {
			return _.find(a.model.arguments, function(argument) {
				return argument.number == number;
			});
		},
		getModel: function() {
			return a.model;
		}
	};

	a.evaluate = {
		maxLeaning: 32,
		calculateScale: function(weights) {
			var leaning = this.calculateLeaning(weights);
			var scale = {
				'leaning': leaning,
				'leftPlate': {
					'translate': this.translatePlate(leaning, 'left')
				},
				'rightPlate': {
					'translate': this.translatePlate(leaning, 'right')
				},
				'weightCount': this.getWeightCount(weights)
			};
			return scale;
		},
		getWeightCount: function(weights) {
			var count = {
				'pro': 0,
				'contra': 0
			};
			_.each(weights, function(weight) {
				if (weight.attachedTo) {
					count[weight.attachedTo]++;
				}
			});
			return count;
		},
		translatePlate: function(leaning, side) {
			var translate = {
				x: Math.pow(leaning, 2) * 0.018,
				y: leaning * 2
			};
			var flip = (side === 'left') ? 'y' : 'x';
			translate[flip] = translate[flip] * -1;
			return translate;
		},
		calculateLeaning: function(weights) {
			var totalWeight = {
				'pro': 0,
				'contra': 0
			};

			_.each(weights, function(weight) {
				if (weight.attachedTo) {
					totalWeight[weight.attachedTo] += weight.value;
				}
			});

			if (totalWeight.pro === 0 && totalWeight.contra === 0) {
				return 0;
			}

			var prefix = totalWeight.pro > totalWeight.contra ? -1 : 1;
			var highestValue = Math.max(totalWeight.pro, totalWeight.contra);
			var lowestValue = Math.min(totalWeight.pro, totalWeight.contra);
			var factor = lowestValue / highestValue;
			return Math.round(this.maxLeaning - (this.maxLeaning * factor)) * prefix;
		},
		redrawWeights: function() {
			// attach weights
			_.each(this.attachedWeights, function(weight) {
				var clone = templateWeight.cloneNode(true);
				var plate = plates[weight.argument.thesis];

				// set argument number
				// clone.getElementsByTagName('text')[0].firstChild.nodeValue =
				// weight.argument.number;
				clone.removeAttribute('style');
				clone.setAttribute('id', 'weight' + weight.argument.number);
				clone.setAttribute('class', 'weight weight-colored-' + weight.argument.colorNumber);

				// set proportions
				var scale = weight.value / 10;
				var translateX = weightCount[weight.argument.thesis] * 40;
				if (weight.argument.thesis === 'CONTRA') {
					translateX += 233;
				}
				var translateY = (84.5 - (84.5 * scale)) * 1.667;

				clone.setAttribute('transform', 'translate(' + translateX + ', ' + translateY + ') scale(' + scale + ')');

				plate.appendChild(clone);
				weightCount[weight.argument.thesis]++;
			});
		},
		getNextFreeColorNumber: function() {
			var colors = [1, 2, 3, 4, 5, 6, 7, 8];
			_.each(this.attachedWeights, function(weight) {
				a.array.remove(colors, weight.argument.colorNumber);
			});
			return colors[0];
		},
		findWeight: function(argumentNumber, weightValue, weights) {
			return _.find(weights, function(weight) {
				return weight.value == weightValue && weight.argument.number == argumentNumber;
			});
		},
		getWeightsForArgument: function(argument, weights) {
			return _.filter(weights, function(weight) {
				return weight.argument.number === argument.number;
			});
		},
		getWeights: function(argumentList) {
			var weights = [];
			_.each(argumentList, function(argument) {
				for ( var size = 1; size <= 4; size++) {
					weights.push({
						'value': size,
						'argument': argument,
						'colorNumber': 0,
						'attachedTo': null
					});
				}
			});

			return weights;
		}
	};

	evaluateModule.controller('EvaluateCtrl', function($scope) {
		$scope.model = a.storage.getModel();
		$scope.weights = a.evaluate.getWeights($scope.model.arguments);
		$scope.scale = a.evaluate.calculateScale();

		$scope.weightsForArgument = function(argument) {
			return a.evaluate.getWeightsForArgument(argument, $scope.weights);
		};
		$scope.scaleForWeight = function(weight) {
			return weight.value / 10;
		};
		$scope.translateValuesForWeight = function(weight) {
			console.log($scope.scale.weightCount[weight.attachedTo]);
			var translateX = $scope.scale.weightCount[weight.attachedTo] * 40;
			if (weight.attachedTo === 'contra') {
				translateX += 233;
			}
			var translateY = (159.5 - (84.5 * weight.value / 10)) * 1.667;
			console.log(translateX + ', ' + translateY);
			return translateX + ', ' + translateY;
		};
	});
})(angular, argue, _);