'use strict';

describe('reject one value of an array', function() {
	it('should remove the value on the same array reference', function() {
		var exampleArray = [1, 2, 3, 4, 5];
		var iterator = function(value) {
			return 4 === value;
		};
		comparison.array.rejectOne(exampleArray, iterator);

		expect(exampleArray.length).toEqual(4);
	});
});

describe('reject all matching values of an array', function() {
	it('should remove the values on the same array reference', function() {
		var exampleArray = [1, 2, 3, 4, 5];
		var iterator = function(value) {
			return value >= 4;
		};
		comparison.array.rejectAll(exampleArray, iterator);

		expect(exampleArray.length).toEqual(3);
	});
});