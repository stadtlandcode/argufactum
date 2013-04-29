'use strict';

(function(angular, q, _) {
	var analysisModule = angular.module('analysis', ['storage']);

	q.analysis = {
		calculate: function(questions, answers) {
			var result = {
				'positive': 0,
				'negative': 0,
				'neutral': 0
			};

			_.each(questions, _.bind(function(question) {
				var answer = this.getAnswer(question.number, answers);

				if (!answer || answer.choice === 'DRAW') {
					result.neutral++;
				} else {
					var countFor = (question.supports === answer.choice) ? 'positive' : 'negative';
					result[countFor]++;
				}
			}, this));

			return result;
		},
		getAnswer: function(questionNumber, answers) {
			return _.find(answers, function(answer) {
				return answer.question == questionNumber;
			});
		}
	};

	analysisModule.controller('AnalysisCtrl', function($scope, storage) {
		$scope.questionnaire = storage.getModel();
		$scope.analysis = q.analysis.calculate($scope.questionnaire.questions, q.answers);
	});
})(angular, questionnaire, _);