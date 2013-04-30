'use strict';

(function(angular, q, _) {
	var answerModule = angular.module('answer', ['storage']);

	q.answers = [];

	q.answer = {
		getEmpty: function(question) {
			return {
				'question': question.number,
				'choice': null,
				'relevance': 'AVERAGE'
			};
		},
		save: function(answer) {
			q.answers.push(answer);
		},
		getNextQuestion: function(answer, questions) {
			var numberOfNextQuestion = answer.question + 1;
			return _.find(questions, function(question) {
				return question.number == numberOfNextQuestion;
			});
		},
		getProgress: function(numberOfQuestions) {
			return Math.round((q.answers.length + 1) / numberOfQuestions * 100);
		}
	};

	answerModule.controller('AnswerCtrl', function($scope, storage, $location) {
		$scope.questionnaire = storage.getModel();
		$scope.question = $scope.questionnaire.questions[0];
		$scope.answer = q.answer.getEmpty($scope.question);
		$scope.progress = function() {
			return q.answer.getProgress($scope.questionnaire.questions.length);
		};

		$scope.saveAnswer = function() {
			q.answer.save($scope.answer);
			$scope.forward();
		};
		$scope.skip = function() {
			$scope.answer.choice = null;
			$scope.saveAnswer();
		};
		$scope.forward = function() {
			$scope.question = q.answer.getNextQuestion($scope.answer, $scope.questionnaire.questions);
			if ($scope.question) {
				$scope.answer = q.answer.getEmpty($scope.question);
			} else {
				$location.path('/analysis');
			}
		};
	});
})(angular, questionnaire, _);