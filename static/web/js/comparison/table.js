'use strict';

(function(angular, c, _) {
	var tableModule = angular.module('table', []);

	c.table = {
		loadData: function() {
			if (localStorage.getItem('ct.data')) {
				return localStorage.getitem('ct.data');
			}
			if (typeof comparisonData !== 'undefined') {
				return comparisonData;
			}
		},
		mergeData: function(currentData, newData) {
			_.extend(currentData, newData);
		},
		cells: {
			findInCache: function(criteriumId, cache) {
				var found = _.find(cache, function(cacheEntry) {
					return cacheEntry.criteriumId === criteriumId;
				});
				return found ? found.cells : [];
			},
			updateCache: function(cache, data) {
				cache.length = 0;

				_.each(data.criteria, function(criterium) {
					var allCells = _.filter(data.cells, function(cell) {
						return cell.criteriumId === criterium.id;
					});
					var sortedCells = _.sortBy(allCells, function(cell) {
						var optionOfCell = _.find(data.options, function(option) {
							return option.id === cell.optionId;
						});
						return optionOfCell.position;
					});

					cache.push({
						'criteriumId': criterium.id,
						'cells': sortedCells
					});
				});
			}
		}
	};

	tableModule.controller('TableCtrl', ['$scope', 'storage', function($scope, storage) {
		$scope.criteria = storage.getData().criteria;
		$scope.options = storage.getData().options;

		$scope.cellsOfCriterium = function(criterium) {
			return c.table.cells.findInCache(criterium.id, storage.getCache());
		};

		$scope.$watch(storage.getData(), storage.updateCache);
	}]);

	tableModule.service('storage', function() {
		var data = {
			options: [],
			criteria: [],
			cells: []
		};
		var cache = [];

		this.getData = function() {
			return data;
		};
		this.loadData = function() {
			var loaded = c.table.loadData();
			c.table.mergeData(data, loaded);
		};

		this.updateCache = function() {
			c.table.cells.updateCache(cache, data);
		};
		this.getCache = function() {
			if (cache.length <= 0) {
				this.updateCache();
			}
			return cache;
		};
	});
})(angular, comparison, _);
