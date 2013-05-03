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
			return this.findQuestion(numberOfNextQuestion, questions);
		},
		getPreviousQuestion: function(answer, questions) {
			var numberOfPreviousQuestion = answer.question - 1;
			return this.findQuestion(numberOfPreviousQuestion, questions);
		},
		findQuestion: function(number, questions) {
			return _.find(questions, function(question) {
				return question.number == number;
			});
		},
		getProgress: function(question, numberOfQuestions) {
			return Math.round(question.number / numberOfQuestions * 100);
		}
	};

	answerModule.controller('AnswerCtrl', function($scope, storage, $location) {
		$scope.questionnaire = storage.getModel();
		$scope.question = $scope.questionnaire.questions[0];
		$scope.answer = q.answer.getEmpty($scope.question);
		$scope.answers = q.answers;
		$scope.progress = function() {
			return q.answer.getProgress($scope.question, $scope.questionnaire.questions.length);
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
		$scope.back = function() {
			$scope.question = q.answer.getPreviousQuestion($scope.answer, $scope.questionnaire.questions);
		};
	});
})(angular, questionnaire, _);