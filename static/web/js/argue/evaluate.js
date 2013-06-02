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
		scale: {},
		getScale: function(weights) {
			_.each(['pro', 'contra'], _.bind(function(plate) {
				this.scale[plate] = {
					'totalWeight': 0,
					'translate': '0,0',
					'labelOpacity': 0.1,
					'weights': []
				};
			}, this));

			this.updateScale(weights);
			return this.scale;
		},
		updateScale: function(weights) {
			this.updatePlateWeights(this.scale.pro, this.weightsOfPlate('pro', weights));
			this.updatePlateWeights(this.scale.contra, this.weightsOfPlate('contra', weights));

			this.updateLeaning(this.scale.pro.totalWeight, this.scale.contra.totalWeight);

			this.updatePlateWeightsPosition(this.scale.pro.weights, 'pro');
			this.updatePlateWeightsPosition(this.scale.contra.weights, 'contra');

			var totalWeight = this.scale.pro.totalWeight + this.scale.contra.totalWeight;
			this.updateLabelOpacity(this.scale.pro, totalWeight);
			this.updateLabelOpacity(this.scale.contra, totalWeight);
		},
		updateLeaning: function(proTotalWeight, contraTotalWeight) {
			this.scale.leaning = this.calculateLeaning(proTotalWeight, contraTotalWeight);
			this.scale.pro.translate = this.translatePlate(this.scale.leaning, 'pro');
			this.scale.contra.translate = this.translatePlate(this.scale.leaning, 'contra');
		},
		calculateLeaning: function(proTotalWeight, contraTotalWeight) {
			if (proTotalWeight === 0 && contraTotalWeight === 0) {
				return 0;
			}

			var prefix = proTotalWeight > contraTotalWeight ? -1 : 1;
			var highestValue = Math.max(proTotalWeight, contraTotalWeight);
			var lowestValue = Math.min(proTotalWeight, contraTotalWeight);
			var factor = lowestValue / highestValue;
			return Math.round(this.maxLeaning - (this.maxLeaning * factor)) * prefix;
		},
		translatePlate: function(leaning, side) {
			var translate = {
				x: Math.pow(leaning, 2) * 0.018,
				y: leaning * 2
			};
			var flip = (side === 'pro') ? 'y' : 'x';
			translate[flip] = translate[flip] * -1;
			return translate.x + ', ' + translate.y;
		},
		updateLabelOpacity: function(plate, totalWeight) {
			plate.labelOpacity = plate.totalWeight <= 0 ? 0.1 : Math.max(0.1, plate.totalWeight / totalWeight);
		},
		updatePlateWeights: function(plate, weightsOfPlate) {
			plate.weights.length = 0;
			_.each(weightsOfPlate, function(weight) {
				plate.totalWeight += weight.value;
				plate.weights.push({
					'weight': weight
				});
			});
		},
		updatePlateWeightsPosition: function(weights, side) {
			var translateXBase = (side === 'contra') ? 233 : 0;
			var plateWidth = 124;
			var totalWidth = 0;
			_.each(weights, _.bind(function(weight) {
				totalWidth += this.widthOfWeight(weight.weight);
			}, this));

			var x = totalWidth == plateWidth ? 0 : (plateWidth - totalWidth) / 2;
			_.each(weights, _.bind(function(weight) {
				weight.scale = weight.weight.value / 10;

				var translateY = 91 + (14 * (4 - weight.weight.value));
				var translateX = translateXBase + x;
				weight.translate = translateX + ',' + translateY;

				x += this.widthOfWeight(weight.weight);
			}, this));
		},
		widthOfWeight: function(weight) {
			return (63 + (10 / weight.value)) * (weight.value / 10);
		},
		weightsOfPlate: function(plateName, weights) {
			return _.filter(weights, function(weight) {
				return weight.attachedTo === plateName;
			});
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
		getNextFreeColorNumber: function(weights) {
			var colors = [1, 2, 3, 4, 5, 6, 7, 8];
			_.each(weights, function(weight) {
				if (weight.argument !== null) {
					a.array.remove(colors, weight.argument.colorNumber);
				}
			});
			return colors[0];
		}
	};

	evaluateModule.controller('EvaluateCtrl', function($scope) {
		$scope.model = a.storage.getModel();
		$scope.weights = a.evaluate.getWeights();
		$scope.scale = a.evaluate.getScale($scope.weights);
	});
})(angular, argue, _);