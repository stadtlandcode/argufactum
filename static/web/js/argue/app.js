'use strict';
var argue = {};

(function(angular) {
	var appModule = angular.module('argue', ['evaluate', 'weight']);

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
		$routeProvider.otherwise({
			redirectTo: '/evaluate'
		});
	});
})(angular);
