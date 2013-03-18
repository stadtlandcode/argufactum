'use strict';

(function(angular, c, _) {
	var tableModule = angular.module('table', []);

	c.table = {
		loadData: function() {
			if (localStorage.getItem('ct.data')) {
				return JSON.parse(localStorage.getItem('ct.data'));
			}
			if (typeof comparisonData !== 'undefined') {
				return comparisonData;
			}
		},
		resetData: function() {
			localStorage.removeItem('ct.data');
		},
		mergeData: function(currentData, newData) {
			_.extend(currentData, newData);
		},
		saveData: function(data) {
			localStorage.setItem('ct.data', JSON.stringify(data));
		},
		getNextPosition: function(objects) {
			var lastObject = _.max(objects, function(object) {
				return object.position;
			});
			return lastObject ? lastObject.position + 1 : 1;
		},
		addGeneric: function(xAxis, xAxisIdField, yAxis, yAxisIdField, cells) {
			var xAxisObject = {
				id: new Date().getTime(),
				label: '',
				position: this.getNextPosition(xAxis)
			};
			xAxis.push(xAxisObject);

			var newCells = [];
			_.each(yAxis, function(yAxisObject) {
				var cell = {
					type: 'boolean',
					value: true
				};
				cell[xAxisIdField] = xAxisObject.id;
				cell[yAxisIdField] = yAxisObject.id;
				newCells.push(cell);
			});
			cells.push.apply(cells, newCells);
		},
		addCriterium: function(data) {
			this.addGeneric(data.criteria, 'criteriumId', data.options, 'optionId', data.cells);
		},
		addOption: function(data) {
			this.addGeneric(data.options, 'optionId', data.criteria, 'criteriumId', data.cells);
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

		$scope.addOption = function() {
			c.table.addOption(storage.getData());
			$scope.onChange();
		};
		$scope.addCriterium = function() {
			c.table.addCriterium(storage.getData());
			$scope.onChange();
		};
		$scope.reset = function() {
			storage.resetData();
		};

		$scope.onChange = function() {
			storage.updateCache();
			storage.saveData();
		};
		$scope.$watch(function() {
			return storage.getData();
		}, $scope.onChange);
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
		this.saveData = function() {
			c.table.saveData(data);
		};
		this.resetData = function() {
			c.table.resetData();
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
