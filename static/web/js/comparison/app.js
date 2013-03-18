'use strict';
var comparison = {};

(function(angular) {
	var appModule = angular.module('ct', ['table']);

	appModule.config(function($routeProvider) {
		$routeProvider.when('/table', {
			templateUrl: 'partials/comparison/table.html',
			controller: 'TableCtrl'
		});
		$routeProvider.otherwise({
			redirectTo: '/table'
		});
	});

	appModule.run(['storage', function(storage) {
		storage.load();
	}]);
})(angular);
