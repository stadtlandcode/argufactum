'use strict';

(function(angular, q, _) {
	var storageModule = angular.module('storage', []);

	q.model = {};

	storageModule.service('storage', function() {
		this.saveModel = function(model) {
			q.model = model;
		};

		this.getModel = function(id) {
			return q.model;
		};
	});
})(angular, questionnaire, _);