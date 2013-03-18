'use strict';
var ct = {};

(function(angular, ct) {
	var appModule = angular.module('ct', []);

	appModule.config(function($routeProvider) {
		$routeProvider.when('/table', {
			templateUrl: 'partials/comparison/table.html',
			controller: 'TableCtrl'
		});
		$routeProvider.otherwise({
			redirectTo: '/table'
		});
	});

	appModule.controller('TableCtrl', function() {
	});
})(angular, ct);
