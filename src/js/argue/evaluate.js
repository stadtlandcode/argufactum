'use strict';

(function(angular, a, _, Modernizr) {
	var evaluateModule = angular.module('evaluate', ['ui.bootstrap.accordion', 'ui.bootstrap.collapse', 'template/accordion/accordion.html']);

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
		plateIds: ['pro', 'contra'],
		getScale: function(weights) {
			_.each(['pro', 'contra'], _.bind(function(plate) {
				this.scale[plate] = {
					'totalWeight': 0,
					'translate': '0,0',
					'animateMotion': '0,0;0,0',
					'weights': []
				};
			}, this));

			this.scale.leaning = 0;
			this.scale.winner = {
				choice: 'neutral',
				adjective: ''
			};
			this.updateScale(weights);
			return this.scale;
		},
		updateScale: function(weights) {
			this.scale.css = '';
			this.updatePlateWeights(this.scale.pro, this.weightsOfPlate('pro', weights));
			this.updatePlateWeights(this.scale.contra, this.weightsOfPlate('contra', weights));

			this.updateLeaning(this.scale.pro.totalWeight, this.scale.contra.totalWeight);

			this.updatePlateWeightsPosition(this.scale.pro.weights, 'pro');
			this.updatePlateWeightsPosition(this.scale.contra.weights, 'contra');

			this.updateWinner(this.scale.leaning, this.scale.pro.totalWeight, this.scale.contra.totalWeight);
		},
		updateWinner: function(leaning, proTotalWeight, contraTotalWeight) {
			var winnerChoice = proTotalWeight > contraTotalWeight ? 'positive' : 'negative';
			var winnerAdjective = null;
			if (proTotalWeight === contraTotalWeight) {
				winnerChoice = proTotalWeight < 1 ? 'neutral' : 'draw';
			} else {
				var winnerPercent = Math.round(Math.abs(leaning) / this.maxLeaning * 100);
				console.log(winnerPercent);
				winnerAdjective = 'größtenteils';
				if (winnerPercent > 80) {
					winnerAdjective = 'absolut';
				} else if (winnerPercent < 20) {
					winnerAdjective = 'knapp';
				}
			}

			this.scale.winner = {
				choice: winnerChoice,
				adjective: winnerAdjective
			};
		},
		updateLeaning: function(proTotalWeight, contraTotalWeight) {
			this.scale.previousLeaning = this.scale.leaning;
			this.scale.leaning = this.calculateLeaning(proTotalWeight, contraTotalWeight);

			_.each(this.plateIds, _.bind(function(plateId) {
				if (a.browser.isSafari() || !Modernizr.smil) {
					this.scale[plateId].translate = this.translatePlate(this.scale.leaning, plateId);
				}
				this.scale[plateId].animateMotion = this.animatePlate(this.scale.previousLeaning, this.scale.leaning, plateId);
			}, this));
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
		animatePlate: function(previousLeaning, currentLeaning, side) {
			var animateMotion = '';
			if (previousLeaning >= currentLeaning) {
				for ( var leaning = previousLeaning; leaning >= currentLeaning; leaning--) {
					animateMotion += this.translatePlate(leaning, side) + ';';
				}
			} else {
				for ( var leaning = previousLeaning; leaning <= currentLeaning; leaning++) {
					animateMotion += this.translatePlate(leaning, side) + ';';
				}
			}
			return animateMotion.substring(0, animateMotion.length - 1);
		},
		translatePlate: function(leaning, side) {
			var translate = {
				x: Math.pow(leaning, 2) * 0.018,
				y: leaning * 2
			};
			var flip = (side === 'pro') ? 'y' : 'x';
			translate[flip] = translate[flip] * -1;
			return translate.x + ',' + translate.y;
		},
		updatePlateWeights: function(plate, weightsOfPlate) {
			plate.totalWeight = 0;
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

				var translateY = 92 + (14 * (4 - weight.weight.value));
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
		getNextFreeColorNumber: function(objects) {
			var colors = [1, 2, 3, 4, 5, 6, 7, 8];
			_.each(objects, function(object) {
				if (object.colorNumber) {
					a.array.remove(colors, object.colorNumber);
				}
			});
			return colors[0];
		},
		findWeightOfArgument: function(argument, weights) {
			return _.find(weights, function(weight) {
				return weight.argument && weight.argument.number == argument.number;
			});
		},
		unassignWeight: function(weight) {
			weight.argument.weight = null;
			weight.argument = null;
			weight.colorNumber = 0;
			weight.attachedTo = null;
		},
		assignWeight: function(weight, argument, weights) {
			var colorNumber;
			var attachedTo;
			var currentWeight = this.findWeightOfArgument(argument, weights);

			if (weight.argument && weight.argument !== argument) {
				this.unassignWeight(weight);
			}

			if (currentWeight) {
				colorNumber = currentWeight.colorNumber;
				attachedTo = currentWeight.attachedTo;
				this.unassignWeight(currentWeight);
			} else {
				colorNumber = a.evaluate.getNextFreeColorNumber(weights);
				attachedTo = null;
			}

			weight.colorNumber = colorNumber;
			weight.attachedTo = attachedTo;
			weight.argument = argument;
			weight.argument.weight = weight;
		}
	};

	evaluateModule.controller('EvaluateCtrl', function($scope) {
		$scope.model = a.storage.getModel();
		$scope.weights = a.evaluate.getWeights();
		$scope.scale = a.evaluate.getScale($scope.weights);

		$scope.assignWeight = function(weight, argument) {
			a.evaluate.assignWeight(weight, argument, $scope.weights);
			if (weight.attachedTo) {
				$scope.updateScale();
			} else {
				$scope.scale.css = 'help-assign';
			}
		};
		$scope.unassignArgument = function(argument) {
			a.evaluate.unassignWeight(argument.weight);
			$scope.updateScale();
		};
		$scope.updateScale = function() {
			a.evaluate.updateScale($scope.weights);

			_.each(document.getElementsByClassName('scaleAnimation'), function(animationElement) {
				animationElement.beginElement();
			});
		};

		$scope.bindArgument = function(argument) {
			console.log(argument);
		};
		$scope.unanimatedLeaning = function() {
			return (a.browser.isSafari() || !Modernizr.smil) ? $scope.scale.leaning : 0;
		};
	});
})(angular, argue, _, Modernizr);