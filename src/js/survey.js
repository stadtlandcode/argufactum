'use strict';

var survey = {};
(function($) {
	var toNextQuestion = function() {
		var index = $('.question:visible').hide().index();
		var nextQuestion = $('.question:eq(' + (index + 1) + ')');
		if(nextQuestion.length >= 0) {
			nextQuestion.show();
		}
		else {
			console.log(nextQuestion);
		}
	};
	
	survey.init = function() {
		$('.question-container').on('click', '.answer-question, .skip-question', toNextQuestion);
	};
})(jQuery);