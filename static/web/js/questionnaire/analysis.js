'use strict';

(function(angular, q, _) {
	var analysisModule = angular.module('analysis', ['storage', 'AwesomeChartJS']);

	q.analysis = {
		getViewModel: function(questions, answers) {
			var numbers = {
				'positive': 0,
				'negative': 0,
				'neutral': 0
			};
			_.each(questions, _.bind(function(question) {
				var answer = this.getAnswer(question.number, answers);

				if (!answer || !answer.choice || answer.choice === 'DRAW') {
					numbers.neutral++;
				} else {
					var countFor = (question.supports === answer.choice) ? 'positive' : 'negative';
					numbers[countFor]++;
				}
			}, this));

			// winner
			var winnerChoice = numbers.positive > numbers.negative ? 'positive' : 'negative';
			if (numbers[winnerChoice] === 0) {
				winnerChoice = 'neutral';
			} else if (numbers.positive === numbers.negaitve) {
				winnerChoice = 'draw';
			} else {
				var winnerPercent = Math.round(numbers[winnerChoice] / questions.length * 100);
				var winnerAdjective = 'größtenteils';
				if (winnerPercent > 90) {
					winnerAdjective = 'absolut';
				} else if (winnerPercent < 50) {
					winnerAdjective = 'knapp';
				}
			}
			var winner = {
				choice: winnerChoice,
				adjective: winnerAdjective
			};

			var model = {
				'numbers': numbers,
				'answers': answers,
				'chart': [],
				'winner': winner
			};
			model.chart.push({
				value: numbers.positive,
				color: 'green'
			});
			model.chart.push({
				value: numbers.negative,
				color: 'red'
			});
			model.chart.push({
				value: numbers.neutral,
				color: 'grey'
			});

			return model;
		},
		getAnswer: function(questionNumber, answers) {
			return _.find(answers, function(answer) {
				return answer.question.number == questionNumber;
			});
		}
	};

	analysisModule.controller('AnalysisCtrl', function($scope, storage) {
		$scope.questionnaire = storage.getModel();
		$scope.model = q.analysis.getViewModel($scope.questionnaire.questions, q.answers);
	});
})(angular, questionnaire, _);