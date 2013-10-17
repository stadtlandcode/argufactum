'use strict';
var comparison = {};

(function(angular) {
	var appModule = angular.module('ct', ['table']);

	appModule.config(function($routeProvider) {
		$routeProvider.when('/viewTable', {
			templateUrl: 'partials/comparison/viewTable.html',
			controller: 'ViewTableCtrl'
		});
		$routeProvider.when('/editTable', {
			templateUrl: 'partials/comparison/editTable.html',
			controller: 'EditTableCtrl'
		});
		$routeProvider.otherwise({
			redirectTo: '/viewTable'
		});
	});

	appModule.run(['storage', function(storage) {
		storage.load();
	}]);
})(angular);
