'use strict';

(function(angular, q, _) {
	var answerModule = angular.module('answer', ['storage']);

	q.answers = [];

	q.answer = {
		forQuestion: function(question) {
			var existingAnswer = this.findAnswer(question);
			if (existingAnswer) {
				return existingAnswer;
			}

			return {
				'question': question,
				'choice': null,
				'relevance': 'AVERAGE',
				'meaning': null,
				'meaningCssClass': '',
				'choiceLabel': '',
				'choiceCssClass': ''
			};
		},
		findAnswer: function(question) {
			return _.find(q.answers, function(answer) {
				return answer.question.number === question.number;
			});
		},
		save: function(answer) {
			this.completeAnswer(answer);
			if (!this.findAnswer(answer.question)) {
				q.answers.push(answer);
			}
		},
		completeAnswer: function(answer) {
			if (!answer.choice || answer.choice === 'DRAW') {
				answer.choiceLabel = (answer.choice === 'DRAW') ? 'Dazu habe ich keine Meinung' : 'Nicht beantwortet';
				answer.choiceCssClass = 'muted';
				answer.meaning = 'neutral';
				answer.meaningCssClass = answer.choiceCssClass;
			} else {
				answer.choiceLabel = (answer.choice === 'YES') ? 'Ja' : 'Nein';
				answer.choiceCssClass = (answer.choice === 'YES') ? 'text-success' : 'text-error';
				answer.meaning = (answer.question.supports === answer.choice) ? 'positive' : 'negative';
				answer.meaningCssClass = (answer.meaning === 'positive') ? 'text-success' : 'text-error';
			}
		},
		getNextQuestion: function(answer, questions) {
			var numberOfNextQuestion = answer.question.number + 1;
			return this.findQuestion(numberOfNextQuestion, questions);
		},
		getPreviousQuestion: function(answer, questions) {
			var numberOfPreviousQuestion = answer.question.number - 1;
			return this.findQuestion(numberOfPreviousQuestion, questions);
		},
		findQuestion: function(number, questions) {
			return _.find(questions, function(question) {
				return question.number == number;
			});
		},
		getProgress: function(question, numberOfQuestions) {
			return question ? Math.round(question.number / numberOfQuestions * 100) : 100;
		}
	};

	answerModule.controller('AnswerCtrl', function($scope, storage, $location) {
		$scope.questionnaire = storage.getModel();
		$scope.question = $scope.questionnaire.questions[0];
		$scope.answer = q.answer.forQuestion($scope.question);
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
				$scope.answer = q.answer.forQuestion($scope.question);
			} else {
				$location.path('/analysis');
			}
		};
		$scope.back = function() {
			$scope.gotoQuestion(q.answer.getPreviousQuestion($scope.answer, $scope.questionnaire.questions));
		};
		$scope.gotoQuestion = function(question) {
			$scope.question = question;
			$scope.answer = q.answer.forQuestion($scope.question);
		};
	});
})(angular, questionnaire, _);