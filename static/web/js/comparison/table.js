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
		updateRating: function(ratingData, criteriumId, rating) {
			var foundRating = this.findRatingEntry(ratingData, criteriumId);

			if (foundRating) {
				foundRating.rating = rating;
			} else {
				ratingData.push({
					'criteriumId': criteriumId,
					'rating': rating
				});
			}
		},
		getRating: function(ratingData, criterium) {
			var ratingEntry = this.findRatingEntry(ratingData, criterium.id);
			return ratingEntry ? ratingEntry.rating : 5;
		},
		findRatingEntry: function(ratingData, criteriumId) {
			return _.find(ratingData, function(ratingEntry) {
				return ratingEntry.criteriumId == criteriumId;
			});
		},
		updateResultCache: function(resultCache, data, ratingData) {
			resultCache.length = 0;

			var sortedOptions = _.sortBy(data.options, function(option) {
				return option.position;
			});

			_.each(sortedOptions, _.bind(function(option) {
				var sum = 0;
				_.each(data.criteria, _.bind(function(criterium) {
					var rating = this.getRating(ratingData, criterium);
					var factor = rating / 10;
					var desired = true;

					var cell = this.cells.find(criterium.id, option.id, data);
					if (cell.value === desired) {
						sum += 1 * factor;
					}

				}, this));

				resultCache.push({
					'optionId': option.id,
					'sum': sum
				});
			}, this));
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
		isWinner: function(resultCache, option) {
			var highestRating = _.max(resultCache, function(resultEntry) {
				return resultEntry.sum;
			});
			var ratingOfOption = _.find(resultCache, function(resultEntry) {
				return resultEntry.optionId == option.id;
			});
			return ratingOfOption.sum === highestRating.sum;
		},
		cells: {
			findByCriterium: function(criteriumId, cache) {
				var found = _.find(cache, function(cacheEntry) {
					return cacheEntry.criteriumId === criteriumId;
				});
				return found ? found.cells : [];
			},
			find: function(criteriumId, optionId, data) {
				return _.find(data.cells, function(cell) {
					return cell.criteriumId === criteriumId && cell.optionId === optionId;
				});
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
		$scope.editMode = false;
		$scope.results = storage.getResultCache();
		$scope.cellsOfCriterium = function(criterium) {
			return c.table.cells.findByCriterium(criterium.id, storage.getCache());
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
			storage.reset();
		};
		$scope.toggleMode = function() {
			$scope.editMode = !$scope.editMode;
		};
		$scope.setRating = function(criteriumId, rating) {
			c.table.updateRating(storage.getRating(), criteriumId, rating);
			$scope.onChange();
			$scope.$apply();
		};
		$scope.ratingOfCriterium = function(criterium) {
			return c.table.getRating(storage.getRating(), criterium);
		};
		$scope.getOptionClasses = function(option) {
			return c.table.isWinner(storage.getResultCache(), option) ? 'option comparison-winner' : 'option';
		};

		$scope.onChange = function() {
			storage.updateCache();
			storage.updateResultCache();
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
		var resultCache = [];

		this.getData = function() {
			return data;
		};
		this.getRating = function() {
			return rating;
		};

		this.updateResultCache = function() {
			c.table.updateResultCache(resultCache, data, rating);
		};
		this.getResultCache = function() {
			if (resultCache.length <= 0) {
				this.updateResultCache();
			}
			return resultCache;
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
