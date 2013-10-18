'use strict';
var argue = {};

(function(angular) {
	var appModule = angular.module('argue', ['evaluate', 'weight', 'editor']);

	appModule.config(function($routeProvider) {
		$routeProvider.when('/evaluate', {
			templateUrl: 'partials/argue/evaluate.html',
			controller: 'EvaluateCtrl'
		});
		$routeProvider.when('/weight', {
			templateUrl: 'partials/argue/weight.html',
			controller: 'WeightCtrl'
		});
		$routeProvider.when('/weight/:argumentId', {
			templateUrl: 'partials/argue/weight.html',
			controller: 'WeightCtrl'
		});
		$routeProvider.when('/create', {
			templateUrl: 'partials/argue/editor.html',
			controller: 'EditorCtrl'
		});
		$routeProvider.when('/edit/:aId', {
			templateUrl: 'partials/argue/editor.html',
			controller: 'EditorCtrl'
		});
		$routeProvider.when('/create/success/:aId', {
			templateUrl: 'partials/argue/createdSuccessfuly.html',
			controller: 'SuccessCtrl'
		});
		$routeProvider.when('/a/:aId', {
			templateUrl: 'partials/argue/loading.html',
			controller: 'LoadCtrl'
		});
		$routeProvider.otherwise({
			redirectTo: '/evaluate'
		});
	});
})(angular);
