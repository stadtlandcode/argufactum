function mockGet() {
	var deferred = jQuery.Deferred();
	jQuery.get = function() { jQuery.get.counter++; return deferred.promise(); };
	jQuery.get.counter = 0;
	return deferred;
}

function mockPost() {
	var deferred = jQuery.Deferred();
	jQuery.post = function() { jQuery.post.counter++; return deferred.promise(); };
	jQuery.post.counter = 0;
	return deferred;
}

function mockFunction(toReturn) {
	var mock = function() {
		mock.counter++;
		mock.argsPerCall[mock.counter] = arguments;
		mock.args = mock.argsPerCall[mock.counter];
		if(toReturn === 'this') {
			return this;
		} else {
			return toReturn;
		}
	};
	mock.counter = 0;
	mock.args = [];
	mock.argsPerCall = [];
	return mock;
};

argue.redirect = mockFunction();
argue.reload = mockFunction();
argue.confirm = mockFunction(true);
$(document).ready(function () {
	$('#qunit-fixture').on('submit', 'form', function(event) { event.preventDefault(); });
});
