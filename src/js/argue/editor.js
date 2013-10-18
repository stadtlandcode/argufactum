'use strict';

(function(angular, a, _) {
	var editorModule = angular.module('editor', ['storage', 'ui.bootstrap', 'template/tooltip/tooltip-popup.html']);

	a.editor = {
		getEmptyModel: function() {
			var model = {
				aId: null,
				title: '',
				isPublic: true,
				arguments: []
			};
			return model;
		},
		addQuestion: function(argumentList) {
			var nextNumber = this.getNextNumber(argumentList);
			var argument = {
				number: nextNumber,
				supports: 'YES',
				facts: []
			};
			argumentList.push(argument);
			return argument;
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
		removeQuestion: function(argument, argumentList) {
			a.array.remove(argumentList, argument);
		},
		removeFact: function(fact, facts) {
			a.array.remove(facts, fact);
		},
		renumerate: function(argumentList) {
			var number = 1;
			_.each(argumentList, function(argument) {
				argument.number = number;
				number++;
			});
		},
		getSomeQuestion: function(argumentList) {
			if (argumentList.length > 0) {
				return _.first(argumentList);
			}
			return this.addQuestion(argumentList);
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
				templateUrl: 'partials/argue/factDialog.html',
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
			var jqXHR = $.ajax(HostUtils.v1Url('/topic'), {
				data: angular.toJson(model),
				contentType: 'application/json',
				type: 'POST'
			});
			jqXHR.done(function(data) {
				successCallback(data);
			});
			jqXHR.fail(errorCallback);
		}
	};

	editorModule.controller('EditorCtrl', function($scope, storage, $location, $dialog, $http, $routeParams) {
		$scope.model = ($routeParams.aId) ? storage.getModel() : a.editor.getEmptyModel();
		$scope.argument = a.editor.getSomeQuestion($scope.model.arguments);
		$scope.loading = false;

		$scope.addQuestion = function() {
			$scope.argument = a.editor.addQuestion($scope.model.arguments);
			document.getElementById('argumentInput').focus();
		};
		$scope.editQuestion = function(argument) {
			$scope.argument = argument;
			document.getElementById('argumentInput').focus();
		};
		$scope.removeQuestion = function(argument) {
			a.editor.removeQuestion(argument, $scope.model.arguments);
			a.editor.renumerate($scope.model.arguments);
			if (argument === $scope.argument) {
				$scope.argument = a.editor.getSomeQuestion($scope.model.arguments);
			}
		};
		$scope.getCssClassForQuestion = function(argument) {
			return argument.supports === 'YES' ? 'text-success' : 'text-error';
		};
		$scope.addFact = function(argument) {
			a.editor.openFactDialog($dialog, a.editor.addFact(argument.facts), argument.facts);
		};
		$scope.editFact = function(fact, argument) {
			a.editor.openFactDialog($dialog, fact, argument.facts);
		};
		$scope.removeFact = function(fact, argument) {
			a.editor.removeFact(fact, argument.facts);
		};

		$scope.save = function() {
			$scope.loading = true;
			a.editor.save($http, $scope.model, $scope.saveSuccess, $scope.saveError);
		};
		$scope.saveSuccess = function(data) {
			$scope.loading = false;
			$scope.model.aId = data.id;
			storage.saveModel($scope.model);

			$location.path('/create/success/' + data.id);
			$scope.$apply();
		};
		$scope.saveError = function() {
			$scope.loading = false;
		};
	});

	editorModule.controller('FactDialogCtrl', function($scope, dialog, fact, facts) {
		$scope.fact = fact;

		$scope.assureReferenceInput = function(fact) {
			a.editor.assureReferenceInput(fact);
		};
		$scope.close = function() {
			dialog.close(false);
		};
		$scope.remove = function() {
			a.editor.removeFact(fact, facts);
			dialog.close();
		};
	});

	editorModule.controller('SuccessCtrl', function($scope, storage) {
		$scope.argue = storage.getModel();
	});

	editorModule.controller('LoadCtrl', function($scope, storage, $location, $routeParams) {
		var aId = $routeParams.aId;

		$.getJSON(HostUtils.v1Url('/topic/' + aId), function(data) {
			var model = data;
			model.aId = aId;
			storage.saveModel(model);
			$location.path('/answer').replace();
			$scope.$apply();
		});
	});
})(angular, argue, _);