'use strict';

(function(angular, q, _) {
	var editorModule = angular.module('editor', ['storage', 'ui.bootstrap', 'template/tooltip/tooltip-popup.html']);

	q.editor = {
		getEmptyModel: function() {
			var model = {
				title: '',
				isPublic: true,
				questions: []
			};
			return model;
		},
		addQuestion: function(questions) {
			var nextNumber = this.getNextNumber(questions);
			var question = {
				number: nextNumber,
				supports: 'YES',
				facts: []
			};
			questions.push(question);
			return question;
		},
		addFact: function(facts) {
			var nextNumber = this.getNextNumber(facts);
			facts.push({
				number: nextNumber,
				text: ''
			});
		},
		removeQuestion: function(question, questions) {
			q.array.remove(questions, question);
		},
		removeFact: function(fact, facts) {
			q.array.remove(facts, fact);
		},
		getNextNumber: function(objects) {
			if (_.isEmpty(objects)) {
				return 1;
			}
			var maxObject = _.max(objects, function(object) {
				return object.number;
			});
			return maxObject.number + 1;
		},
		openQuestionDialog: function($dialog, question, questions) {
			var d = $dialog.dialog({
				templateUrl: 'partials/questionnaire/questionDialog.html',
				controller: 'QuestionDialogCtrl',
				resolve: {
					'question': function() {
						return question;
					},
					'questions': function() {
						return questions;
					}
				}
			});

			d.open();
		}
	};

	editorModule.controller('EditorCtrl', function($scope, storage, $location, $dialog) {
		$scope.model = q.editor.getEmptyModel();

		$scope.addQuestion = function() {
			var question = q.editor.addQuestion($scope.model.questions);
			$scope.editQuestion(question);
		};
		$scope.editQuestion = function(question) {
			q.editor.openQuestionDialog($dialog, question, $scope.model.questions);
		};
		$scope.removeQuestion = function(question) {
			q.editor.removeQuestion(question, $scope.model.questions);
		};

		$scope.save = function() {
			storage.saveModel($scope.model);
			$location.path('/create/success');
		};
		$scope.exportJson = function() {
			var text = angular.toJson($scope.model);
			window.prompt("Copy to clipboard: Ctrl+C, Enter", text);
		};
	});

	editorModule.controller('QuestionDialogCtrl', function($scope, dialog, question, questions) {
		$scope.question = question;

		$scope.addFact = function(question) {
			q.editor.addFact(question.facts);
		};
		$scope.removeFact = function(fact) {
			q.editor.removeFact(fact, question.facts);
		};
		$scope.close = function() {
			dialog.close(false);
		};
		$scope.remove = function() {
			q.editor.removeQuestion(question, questions);
			dialog.close();
		};
	});

	editorModule.controller('SuccessCtrl', function($scope, storage) {
		$scope.questionnaire = storage.getModel();
	});
})(angular, questionnaire, _);