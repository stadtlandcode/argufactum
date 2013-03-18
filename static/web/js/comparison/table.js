'use strict';

(function(angular, c, _) {
	var tableModule = angular.module('table', []);

	c.table = {
		load: function(key) {
			if (localStorage.getItem('ct.' + key)) {
				return JSON.parse(localStorage.getItem('ct.' + key));
			}
			if (c[key]) {
				return c[key];
			}
		},
		reset: function(key) {
			localStorage.removeItem('ct.' + key);
		},
		merge: function(currentData, newData) {
			_.extend(currentData, newData);
		},
		save: function(data, key) {
			localStorage.setItem('ct.' + key, JSON.stringify(data));
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
		$scope.editMode = false;

		$scope.addOption = function() {
			c.table.addOption(storage.getData());
			$scope.onChange();
		};
		$scope.addCriterium = function() {
			c.table.addCriterium(storage.getData());
			$scope.onChange();
		};
		$scope.reset = function() {
			storage.reset();
		};
		$scope.toggleMode = function() {
			$scope.editMode = !$scope.editMode;
		};
		$scope.setRating = function(criteriaId, rating) {
			this.getRating().push({
				criteriaId: criteriaId,
				rating: rating
			});
		};

		$scope.onChange = function() {
			storage.updateCache();
			storage.save();
		};
	}]);

	tableModule.service('storage', function() {
		var data = {
			options: [],
			criteria: [],
			cells: []
		};
		var rating = [];
		var cache = [];

		this.getData = function() {
			return data;
		};
		this.getRating = function() {
			return rating;
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

		this.load = function() {
			c.table.merge(data, c.table.load('data'));
			c.table.merge(rating, c.table.load('rating'));
		};
		this.save = function() {
			c.table.save(data, 'data');
			c.table.save(rating, 'rating');
		};
		this.reset = function() {
			c.table.reset('data');
			c.table.reset('rating');
		};
	});
})(angular, comparison, _);
