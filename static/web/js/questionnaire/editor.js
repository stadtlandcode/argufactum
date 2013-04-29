'use strict';

(function(angular, q, _) {
	var editorModule = angular.module('editor', ['storage']);

	q.editor = {
		getEmptyModel: function() {
			var model = {
				title: '',
				isPublic: true,
				questions: []
			};
			this.addQuestion(model.questions);
			return model;
		},
		addQuestion: function(questions) {
			var nextNumber = this.getNextNumber(questions);
			questions.push({
				number: nextNumber,
				supports: 'YES',
				facts: []
			});
		},
		addFact: function(facts) {
			var nextNumber = this.getNextNumber(facts);
			facts.push({
				number: nextNumber,
				text: ''
			});
		},
		getNextNumber: function(objects) {
			if (_.isEmpty(objects)) {
				return 1;
			}
			var maxObject = _.max(objects, function(object) {
				return object.number;
			});
			return maxObject.number + 1;
		}
	};

	editorModule.controller('EditorCtrl', function($scope, storage, $location) {
		$scope.model = q.editor.getEmptyModel();

		$scope.addQuestion = function() {
			q.editor.addQuestion($scope.model.questions);
		};
		$scope.addFact = function(question) {
			q.editor.addFact(question.facts);
		};

		$scope.save = function() {
			console.log($scope.model);
			storage.saveModel($scope.model);
			$location.path('/create/success');
		};
	});

	editorModule.controller('SuccessCtrl', function($scope, storage) {
		$scope.questionnaire = storage.getModel();
	});
})(angular, questionnaire, _);