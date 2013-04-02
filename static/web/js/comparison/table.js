'use strict';

(function(angular, c, _) {
	var tableModule = angular.module('table', ['ui.bootstrap']);

	c.dataTypes = [{
		id: 'boolean',
		label: 'Ja / Nein',
		getNewCell: function() {
			return {
				value: false
			};
		},
		configureExistingCell: function(cell) {
			cell.value = false;
			cell.label = '';
		},
		labelForCell: function(cell) {
			return cell.value ? 'Ja' : 'Nein';
		}
	}, {
		id: 'rating',
		label: 'Bewertung',
		table: {
			5: 'sehr gut',
			4: 'gut',
			3: 'durchschnittlich',
			2: 'schlecht',
			1: 'sehr schlecht'
		},
		getNewCell: function() {
			return {
				value: 3
			};
		},
		configureExistingCell: function(cell) {
			cell.value = 3;
			cell.label = '';
		},
		labelForCell: function(cell) {
			return this.table[cell.value];
		}
	}];

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
		getNextPosition: function(positionedObjects) {
			var lastObject = _.max(positionedObjects, function(object) {
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
		addCellsGeneric: function(cells, dataTypeFactory, xAxisObject, xAxisIdField, yAxis, yAxisIdField) {
			var newCells = [];
			_.each(yAxis, function(yAxisObject) {
				var dataType = dataTypeFactory(yAxisObject);
				var cell = dataType.getNewCell();
				cell[xAxisIdField] = xAxisObject.id;
				cell[yAxisIdField] = yAxisObject.id;
				newCells.push(cell);
			});
			cells.push.apply(cells, newCells);
		},
		removeCellsGeneric: function(cells, idField, id) {
			c.array.rejectAll(cells, function(cell) {
				return cell[idField] === id;
			});
		},
		defaultCriterium: function() {
			return {
				label: '',
				dataTypeId: 'boolean'
			};
		},
		addCriterium: function(criterium, data) {
			criterium = _.extend(criterium, {
				id: new Date().getTime(),
				position: this.getNextPosition(data.criteria)
			});
			data.criteria.push(criterium);

			var dataTypeFactory = _.bind(function() {
				return this.findDataType(criterium.dataTypeId);
			}, this);
			this.addCellsGeneric(data.cells, dataTypeFactory, criterium, 'criteriumId', data.options, 'optionId');
		},
		removeCriterium: function(id, data) {
			c.array.rejectOne(data.criteria, function(criterium) {
				return criterium.id === id;
			});
			this.removeCellsGeneric(data.cells, 'criteriumId', id);
		},
		openCriteriumDialog: function($dialog, criterium, handleResult) {
			var d = $dialog.dialog({
				templateUrl: 'partials/comparison/criteriumDialog.html',
				controller: 'CriteriumDialogCtrl',
				resolve: {
					'criterium': function() {
						return criterium;
					}
				}
			});

			d.open().then(function(result) {
				if (!result) {
					return false;
				}
				handleResult(result);
			});
		},
		addOption: function(data) {
			var option = {
				id: new Date().getTime(),
				label: '',
				position: this.getNextPosition(data.options)
			};
			data.options.push(option);

			var dataTypeFactory = _.bind(function(criterium) {
				return this.findDataType(criterium.dataTypeId);
			}, this);
			this.addCellsGeneric(data.cells, dataTypeFactory, option, 'optionId', data.criteria, 'criteriumId');
		},
		removeOption: function(id, data) {
			c.array.rejectOne(data.options, function(option) {
				return option.id === id;
			});
			this.removeCellsGeneric(data.cells, 'optionId', id);
		},
		findDataType: function(dataTypeId) {
			return _.find(c.dataTypes, function(dataType) {
				return dataType.id === dataTypeId;
			});
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
		},
		setupScope: function($scope, storage) {
			$scope.criteria = storage.getData().criteria;
			$scope.options = storage.getData().options;
			$scope.cellsOfCriterium = function(criterium) {
				return c.table.cells.findByCriterium(criterium.id, storage.getCache());
			};
		}
	};

	tableModule.controller('ViewTableCtrl', ['$scope', 'storage', function($scope, storage) {
		// data
		c.table.setupScope($scope, storage);
		storage.updateResultCache();
		$scope.results = storage.getResultCache();

		// functions
		$scope.setRating = function(criteriumId, rating) {
			c.table.updateRating(storage.getRating(), criteriumId, rating);
			storage.updateResultCache();
			storage.save();
			$scope.$apply();
		};
		$scope.ratingOfCriterium = function(criterium) {
			return c.table.getRating(storage.getRating(), criterium);
		};
		$scope.getOptionClasses = function(option) {
			return c.table.isWinner(storage.getResultCache(), option) ? 'option comparison-winner' : 'option';
		};
		$scope.templateUrl = function(criterium) {
			return 'partials/comparison/dataType/' + criterium.dataTypeId + '.html';
		};
		$scope.labelOfCell = function(criterium, cell) {
			var dataType = c.table.findDataType(criterium.dataTypeId);
			return dataType.labelForCell(cell);
		};
	}]);

	tableModule.controller('EditTableCtrl', ['$scope', '$dialog', 'storage', function($scope, $dialog, storage) {
		// data
		c.table.setupScope($scope, storage);

		$scope.addOption = function() {
			c.table.addOption(storage.getData());
			$scope.onChange();
		};
		$scope.removeOption = function(option) {
			c.table.removeOption(option.id, storage.getData());
			$scope.onChange();
		};

		$scope.addCriterium = function() {
			var criterium = c.table.defaultCriterium();
			var handleResult = function(result) {
				c.table.addCriterium(result, storage.getData());
				$scope.onChange();
			};
			c.table.openCriteriumDialog($dialog, criterium, handleResult);
		};
		$scope.editCriterium = function(criterium) {
			c.table.openCriteriumDialog($dialog, criterium, $scope.onChange);
		};
		$scope.removeCriterium = function(criterium) {
			c.table.removeCriterium(criterium.id, storage.getData());
			$scope.onChange();
		};
		$scope.templateUrl = function(criterium) {
			return 'partials/comparison/dataType/' + criterium.dataTypeId + 'Edit.html';
		};

		$scope.reset = function() {
			storage.reset();
		};
		$scope.onChange = function() {
			storage.updateCache();
			storage.save();
		};
	}]);

	tableModule.controller('CriteriumDialogCtrl', ['$scope', 'dialog', 'criterium', function($scope, dialog, criterium) {
		$scope.criterium = criterium;
		$scope.edit = criterium.id ? true : false;
		$scope.dataTypes = c.dataTypes;

		$scope.save = function() {
			dialog.close($scope.criterium);
		};
		$scope.close = function() {
			dialog.close(false);
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
