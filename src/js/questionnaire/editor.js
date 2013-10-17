'use strict';

(function(angular, q, _) {
	var editorModule = angular.module('editor', ['storage', 'ui.bootstrap', 'template/tooltip/tooltip-popup.html']);

	q.editor = {
		getEmptyModel: function() {
			var model = {
				qId: null,
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
			var fact = {
				number: nextNumber,
				references: [],
				text: ''
			};
			this.addReference(fact.references);
			facts.push(fact);
			return fact;
		},
		addReference: function(references) {
			var nextNumber = this.getNextNumber(references);
			var reference = {
				number: nextNumber,
				text: ''
			};
			references.push(reference);
			return reference;
		},
		removeQuestion: function(question, questions) {
			q.array.remove(questions, question);
		},
		removeFact: function(fact, facts) {
			q.array.remove(facts, fact);
		},
		renumerate: function(questions) {
			var number = 1;
			_.each(questions, function(question) {
				question.number = number;
				number++;
			});
		},
		getSomeQuestion: function(questions) {
			if (questions.length > 0) {
				return _.first(questions);
			}
			return this.addQuestion(questions);
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
		openFactDialog: function($dialog, fact, facts) {
			var d = $dialog.dialog({
				templateUrl: 'partials/questionnaire/factDialog.html',
				controller: 'FactDialogCtrl',
				resolve: {
					'fact': function() {
						return fact;
					},
					'facts': function() {
						return facts;
					}
				}
			});

			d.open();
		},
		assureReferenceInput: function(fact) {
			var emptyReference = _.find(fact.references, function(reference) {
				return reference.text === '';
			});
			if (!emptyReference) {
				this.addReference(fact.references);
			}
		},
		save: function($http, model, successCallback, errorCallback) {
			var jsonString = angular.toJson(model);
			var postData = 'json=' + jsonString;
			var jqXHR = $.post(HostUtils.v1Url('/storage'), postData);
			jqXHR.done(function(data) {
				successCallback(data);
			});
			jqXHR.fail(errorCallback);
		}
	};

	editorModule.controller('EditorCtrl', function($scope, storage, $location, $dialog, $http, $routeParams) {
		$scope.model = ($routeParams.qId) ? storage.getModel() : q.editor.getEmptyModel();
		$scope.question = q.editor.getSomeQuestion($scope.model.questions);
		$scope.loading = false;

		$scope.addQuestion = function() {
			$scope.question = q.editor.addQuestion($scope.model.questions);
			document.getElementById('questionInput').focus();
		};
		$scope.editQuestion = function(question) {
			$scope.question = question;
			document.getElementById('questionInput').focus();
		};
		$scope.removeQuestion = function(question) {
			q.editor.removeQuestion(question, $scope.model.questions);
			q.editor.renumerate($scope.model.questions);
			if (question === $scope.question) {
				$scope.question = q.editor.getSomeQuestion($scope.model.questions);
			}
		};
		$scope.getCssClassForQuestion = function(question) {
			return question.supports === 'YES' ? 'text-success' : 'text-error';
		};
		$scope.addFact = function(question) {
			q.editor.openFactDialog($dialog, q.editor.addFact(question.facts), question.facts);
		};
		$scope.editFact = function(fact, question) {
			q.editor.openFactDialog($dialog, fact, question.facts);
		};
		$scope.removeFact = function(fact, question) {
			q.editor.removeFact(fact, question.facts);
		};

		$scope.save = function() {
			$scope.loading = true;
			q.editor.save($http, $scope.model, $scope.saveSuccess, $scope.saveError);
		};
		$scope.saveSuccess = function(id) {
			$scope.loading = false;
			$scope.model.qId = id;
			storage.saveModel($scope.model);

			$location.path('/create/success/' + id);
			$scope.$apply();
		};
		$scope.saveError = function() {
			$scope.loading = false;
		};
	});

	editorModule.controller('FactDialogCtrl', function($scope, dialog, fact, facts) {
		$scope.fact = fact;

		$scope.assureReferenceInput = function(fact) {
			q.editor.assureReferenceInput(fact);
		};
		$scope.close = function() {
			dialog.close(false);
		};
		$scope.remove = function() {
			q.editor.removeFact(fact, facts);
			dialog.close();
		};
	});

	editorModule.controller('SuccessCtrl', function($scope, storage) {
		$scope.questionnaire = storage.getModel();
	});
})(angular, questionnaire, _);