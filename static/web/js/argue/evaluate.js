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
		getNextFreeColorNumber: function() {
			var colors = [1, 2, 3, 4, 5, 6, 7, 8];
			_.each(this.attachedWeights, function(weight) {
				a.array.remove(colors, weight.argument.colorNumber);
			});
			return colors[0];
		},
		findWeight: function(weights, id) {
			return _.find(weights, function(weight) {
				return weight.id == id;
			});
		},
		getWeights: function() {
			var weights = [];
			var id = 1;
			for ( var value = 1; value <= 4; value++) {
				for ( var index = value; index <= 4; index++) {
					weights.push({
						'id': id++,
						'index': index,
						'value': value,
						'argument': null,
						'colorNumber': 0,
						'attachedTo': null
					});
				}
			}

			return weights;
		},
		getPlateIndexOfWeight: function(weight, weights) {
			var plateIndex = 0;
			_.find(weights, function(weightToCount) {
				if (weightToCount.attachedTo === weight.attachedTo) {
					plateIndex++;
				}
				return weightToCount === weight;
			});
			return plateIndex;
		},
	};

	evaluateModule.controller('EvaluateCtrl', function($scope) {
		$scope.model = a.storage.getModel();
		$scope.weights = a.evaluate.getWeights();
		$scope.scale = a.evaluate.calculateScale();

		$scope.scaleForWeight = function(weight) {
			return weight.value / 10;
		};
		$scope.translateValuesForWeight = function(weight) {
			var translateX = a.evaluate.getPlateIndexOfWeight(weight, $scope.weights) * 70;
			if (weight.attachedTo === 'contra') {
				translateX += 523;
			} else {
				translateX -= 50;
			}
			var translateY = (50 + (550 / (weight.value * 0.9)));
			console.log(weight.value + ': ' + translateX + ', ' + translateY);
			return translateX + ', ' + translateY;
		};
	});
})(angular, argue, _);