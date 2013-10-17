'use strict';

var comparisonTable = {};
(function($) {
	var recalculate = function() {
		var results = {};
		$('.alternative').each(function() {
			var indexOfAlternative = $(this).index();
			var sum = 0;
			
			$('.table-comparison tbody tr:not(.result)').each(function() {
				var factor = $(this).find('.slider').slider('value') / 10;
				var desired = $(this).find('select').val();
				var textValue = $(this).children().eq(indexOfAlternative).text();
				
				if(desired == textValue) {
					sum += 1 * factor;
				}
			});
			
			results[indexOfAlternative] = sum;
		});
		
		var highestResult = 0;
		$.each(results, function(index, result) {
			if(result > highestResult) {
				highestResult = result;
			}
			$('tr.result').children().eq(index).text(result);
		});

		$('.comparison-winner').removeClass('comparison-winner');
		$.each(results, function(index, result) {
			if(result >= highestResult) {
				$('.table-comparison tr').each(function() {
					$(this).children().eq(index).addClass('comparison-winner');
				});
			}
		});
	};
	
	comparisonTable.init = function() {
		var slider = $('<div class="slider" data-toggle="tooltip" title="Gewichtung (1-10)"></div>').insertAfter('.criteria-choose').slider({
			min : 1,
			max : 10,
			range : "min",
			value : 5,
			change : recalculate
		});
		$('.criteria-choose').change(recalculate);
		$('[data-toggle="tooltip"]').tooltip();
	};
})(jQuery);