'use strict';
var ct = {};

(function(angular, ct) {
	var appModule = angular.module('ct', []);

	appModule.config(['$routeProvider', function($routeProvider) {
		$routeProvider.when('/', {
			templateUrl: 'partials/comparison/table.html',
			controller: TableCtrl
		});
	}]);
})(angular, ct);
