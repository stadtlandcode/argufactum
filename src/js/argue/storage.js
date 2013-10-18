'use strict';

(function(angular, a, _) {
	var storageModule = angular.module('storage', []);

	a.model = {};

	storageModule.service('storage', function() {
		this.saveModel = function(model) {
			a.model = model;
		};

		this.getModel = function(id) {
			return a.model;
		};
	});
})(angular, argue, _);