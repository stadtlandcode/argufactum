'use strict';

(function(angular, q, _) {
	var analysisModule = angular.module('analysis', ['storage', 'AwesomeChartJS']);

	q.analysis = {
		getViewModel: function(questions, answers) {
			var numbers = {
				'positive': 0,
				'negative': 0,
				'neutral': 0,
				'notAnswered': 0
			};
			_.each(questions, _.bind(function(question) {
				var answer = this.getAnswer(question.number, answers);

				if (!answer || !answer.choice) {
					numbers.notAnswered++;
				} else if (answer.choice === 'DRAW') {
					numbers.neutral++;
				} else {
					var countFor = (question.supports === answer.choice) ? 'positive' : 'negative';
					numbers[countFor]++;
				}
			}, this));

			var model = {
				'numbers': numbers,
				'answers': answers,
				'chart': [],
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
				value: numbers.neutral + numbers.notAnswered,
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
		console.log(q.answers);
		$scope.model = q.analysis.getViewModel($scope.questionnaire.questions, q.answers);
		$scope.cssClassForAnswer = function(answer) {
			if (answer.meaning == 'positive') {
				return 'text-success';
			} else if (answer.meaning == 'negative') {
				return 'text-error';
			} else {
				return 'muted';
			}
		};
	});
})(angular, questionnaire, _);