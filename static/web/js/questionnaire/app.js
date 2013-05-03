'use strict';
var questionnaire = {};

(function(angular) {
	var appModule = angular.module('q', ['list', 'editor', 'answer', 'analysis']);

	appModule.config(function($routeProvider) {
		$routeProvider.when('/start', {
			templateUrl: 'partials/questionnaire/start.html',
			controller: 'StartCtrl'
		});
		$routeProvider.when('/create', {
			templateUrl: 'partials/questionnaire/editor.html',
			controller: 'EditorCtrl'
		});
		$routeProvider.when('/create/success', {
			templateUrl: 'partials/questionnaire/createdSuccessfuly.html',
			controller: 'SuccessCtrl'
		});
		$routeProvider.when('/answer', {
			templateUrl: 'partials/questionnaire/answer.html',
			controller: 'AnswerCtrl'
		});
		$routeProvider.when('/answer/:questionId', {
			templateUrl: 'partials/questionnaire/answer.html',
			controller: 'AnswerCtrl'
		});
		$routeProvider.when('/analysis', {
			templateUrl: 'partials/questionnaire/analysis.html',
			controller: 'AnalysisCtrl'
		});
		$routeProvider.otherwise({
			redirectTo: '/start'
		});
	});
})(angular);
