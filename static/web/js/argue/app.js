'use strict';
var argue = {};

(function(angular) {
	var appModule = angular.module('argue', ['evaluate']);

	appModule.config(function($routeProvider) {
		$routeProvider.when('/evaluate', {
			templateUrl: 'partials/argue/evaluate.html',
			controller: 'EvaluateCtrl'
		});
		$routeProvider.otherwise({
			redirectTo: '/evaluate'
		});
	});
})(angular);
