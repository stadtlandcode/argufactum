'use strict';

(function(angular, a, _) {
	var evaluateModule = angular.module('evaluate', []);

	a.storage = {
		findArgument: function(number) {
			return _.find(a.model.arguments, function(argument) {
				return argument.number == number;
			});
		}
	};

	a.evaluate = {
		maxLeaning: 32,
		attachedWeights: [],
		lean: function(leaning) {
			var translateY = leaning * 2;
			var translateX = Math.pow(leaning, 2) * 0.018;
			document.getElementById('leftPlate').setAttribute('transform', 'translate(' + translateX + ', ' + (translateY * -1) + ')');
			document.getElementById('rightPlate').setAttribute('transform', 'translate(' + (translateX * -1) + ', ' + translateY + ')');

			document.getElementById('scalebeam').setAttribute('transform', 'rotate(' + leaning + ', 178, 257)');
		},
		recalculateLeaning: function() {
			var totalWeight = {
				'PRO': 0,
				'CONTRA': 0
			};

			_.each(this.attachedWeights, function(weight) {
				totalWeight[weight.argument.thesis] += weight.value;
			});

			var prefix = totalWeight.PRO > totalWeight.CONTRA ? -1 : 1;
			var highestValue = Math.max(totalWeight.PRO, totalWeight.CONTRA);
			var lowestValue = Math.min(totalWeight.PRO, totalWeight.CONTRA);
			var factor = lowestValue / highestValue;
			var leaning = Math.round(this.maxLeaning - (this.maxLeaning * factor)) * prefix;

			this.lean(leaning);
		},
		redrawWeights: function() {
			var templateWeight = document.getElementById('templateWeight');
			var plates = {
				'PRO': document.getElementById('leftPlateWeights'),
				'CONTRA': document.getElementById('rightPlateWeights')
			};
			var totalWeightCount = this.getWeightCountPerThesis();
			var weightCount = {
				'PRO': 0,
				'CONTRA': 0
			};

			// clear existing weights
			while (plates.PRO.firstChild) {
				plates.PRO.removeChild(plates.PRO.firstChild);
			}
			while (plates.CONTRA.firstChild) {
				plates.CONTRA.removeChild(plates.CONTRA.firstChild);
			}

			// attach weights
			_.each(this.attachedWeights, function(weight) {
				var clone = templateWeight.cloneNode(true);
				var plate = plates[weight.argument.thesis];

				// set argument number
				clone.getElementsByTagName('text')[0].firstChild.nodeValue = weight.argument.number;
				clone.setAttribute('id', 'weight' + weight.argument.number);

				// set proportions
				var scale = weight.value / 7;
				var translateX = weightCount[weight.argument.thesis] * 40;
				if (weight.argument.thesis === 'CONTRA') {
					translateX += 233;
				}
				var translateY = (114.5 - (114.5 * scale)) * 1.667;
				console.log(translateY);

				clone.setAttribute('transform', 'translate(' + translateX + ', ' + translateY + ') scale(' + scale + ')');

				plate.appendChild(clone);
				weightCount[weight.argument.thesis]++;
			});
		},
		getWeightCountPerThesis: function() {
			var count = {
				'PRO': 0,
				'CONTRA': 0
			};

			_.each(this.attachedWeights, function(weight) {
				count[weight.argument.thesis]++;
			});
		},
		addWeight: function(weight) {
			var existingWeight = this.findAttachedWeight(weight.argument);
			if (existingWeight) {
				existingWeight.value = weight.value;
			} else {
				this.attachedWeights.push(weight);
			}

			this.redrawWeights();
			this.recalculateLeaning();
		},
		findAttachedWeight: function(argument) {
			return _.find(this.attachedWeights, function(weight) {
				return weight.argument.number == argument.number;
			});
		},
		dragWeight: function(event) {
			event.dataTransfer.setData('weight', event.target.getAttribute('data-weight'));
			event.dataTransfer.setData('number', event.target.getAttribute('data-number'));
		},
		allowDrop: function(event) {
			event.preventDefault();
		},
		onDrop: function(event) {
			event.preventDefault();
			var weight = event.dataTransfer.getData('weight');
			var number = event.dataTransfer.getData('number');
			var argument = a.storage.findArgument(number);

			this.addWeight({
				'value': Number(weight),
				'argument': argument
			});
		}
	};

	evaluateModule.controller('EvaluateCtrl', function($scope) {
		$scope.model = a.model;
	});
})(angular, argue, _);