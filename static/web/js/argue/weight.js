'use strict';

(function(angular, a, _) {
	var weightModule = angular.module('weight', []);

	a.weights = [];

	a.weight = {
		forArgument: function(argument) {
			var existingWeight = this.findWeight(argument);
			if (existingWeight) {
				return existingWeight;
			}

			return {
				'argument': argument,
				'value': null,
				'height': 0
			};
		},
		getPossibleChoices: function() {
			var choices = [];
			for ( var i = 1; i <= 4; i++) {
				choices.push({
					value: i
				});
			}
			return choices;
		},
		findWeight: function(argument) {
			return _.find(a.weights, function(weight) {
				return weight.argument.number === argument.number;
			});
		},
		save: function(weight) {
			this.completeWeight(weight);
			if (!this.findWeight(weight.argument)) {
				a.weights.push(weight);
			}
		},
		completeWeight: function(weight) {
			weight.height = weight.value * 10;
		},
		getNextArgument: function(weight, topicArguments) {
			var numberOfNextArgument = weight.argument.number + 1;
			return this.findArgument(numberOfNextArgument, topicArguments);
		},
		getPreviousArgument: function(weight, topicArguments) {
			var numberOfPreviousArgument = weight.argument.number - 1;
			return this.findArgument(numberOfPreviousArgument, topicArguments);
		},
		findArgument: function(number, topicArguments) {
			return _.find(topicArguments, function(argument) {
				return argument.number == number;
			});
		},
		getProgress: function(argument, numberOfArguments) {
			return argument ? Math.round(argument.number / numberOfArguments * 100) : 100;
		}
	};

	weightModule.controller('WeightCtrl', function($scope, $location, $routeParams) {
		$scope.gotoArgument = function(argument) {
			$scope.argument = argument;
			$scope.weight = a.weight.forArgument($scope.argument);
			$scope.possibleChoices = a.weight.getPossibleChoices();
		};

		$scope.topic = a.model;

		var initialArgumentNumber = $routeParams.argumentId ? $routeParams.argumentId : 1;
		$scope.gotoArgument(a.weight.findArgument(initialArgumentNumber, $scope.topic.arguments));

		$scope.weights = a.weights;
		$scope.progress = function() {
			return a.weight.getProgress($scope.argument, $scope.topic.arguments.length);
		};

		$scope.saveWeight = function() {
			a.weight.save($scope.weight);
			$scope.forward();
		};
		$scope.skip = function() {
			$scope.weight.value = null;
			$scope.saveWeight();
		};
		$scope.forward = function() {
			$scope.argument = a.weight.getNextArgument($scope.weight, $scope.topic.arguments);
			if ($scope.argument) {
				$scope.weight = a.weight.forArgument($scope.argument);
			} else {
				$location.path('/analysis');
			}
		};
		$scope.back = function() {
			$scope.gotoArgument(a.weight.getPreviousArgument($scope.weight, $scope.topic.arguments));
		};

		$scope.choice = function(possibleChoice) {
			$scope.weight.value = possibleChoice.value;
		};
		$scope.getSelectedClass = function(possibleChoice) {
			return ($scope.weight.value === possibleChoice.value) ? 'weight-selected' : '';
		};
	});
})(angular, argue, _);