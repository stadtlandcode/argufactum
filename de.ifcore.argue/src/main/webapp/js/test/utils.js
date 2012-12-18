test('mock function', function() {
	var mock1 = mockFunction();
	var mock2 = mockFunction();
	
	mock1();
	mock1();
	mock2();
	equal(mock1.counter, 2, 'called 2 times');
	equal(mock2.counter, 1, 'called 1 time');
});

asyncTest('block doubleClick', function() {
	var clickCount = 0;
	var delay = 50;
	var period = 250;
	var click = function() { $('#doubleClickForm button').click(); };
	$('#doubleClickForm').submit(function() { clickCount++; return false; });
	$('#doubleClickForm button').blockDoubleClick({ 'delay': delay, 'period': period });
	
	setTimeout(click, 1);
	setTimeout(click, 2);
	setTimeout(click, delay+60);
	setTimeout(click, period+120);
	
	setTimeout(function() {
		equal(clickCount, 3, 'the click at delay+60 should get blocked');
		start();
	}, period+180);
});

test('wait for templates loaded', function() {
	var mock1 = mockFunction();
	$(argue).on('abc', argue.waitForTmpl(mock1));
	equal(argue.waitForTmpl.queue.length, 0, "don't fill the queue immediately");
	
	$(argue).triggerHandler('abc');
	equal(mock1.counter, 0, "queue call of function");
	equal(argue.waitForTmpl.queue.length, 1, 'add event to queue');
	
	$(argue).triggerHandler('templates.loaded.executeDependentEvents');
	equal(mock1.counter, 1, "call events in queue");
	equal(argue.waitForTmpl.queue.length, 0, 'clear queue');
})

test('filter by data', function() {
	equal($('#filter-by-data span').filterByData('filterId', 1).length, 2, 'find 2 elements with id 1');
	equal($('#filter-by-data span').filterByData('filterId', 2).length, 1, 'find 1 elements with id 2');
});

test('select siblings by value (ascending / descending)', function() {
	equal($('#stars-desc .star-rating:first-child').siblingsByValue('.star-rating', 'greater', 'desc').length, 0, 'greatest element in desc order (first-child)');
	equal($('#stars-desc .star-rating:first-child').siblingsByValue('.star-rating', 'greater', 'asc').length, 4, 'greatest element in asc order (last-child)');
	equal($('#stars-desc .star-rating:first-child').siblingsByValue('.star-rating', 'lower', 'desc').length, 4, 'lowest element in desc order (last-child)');
	equal($('#stars-desc .star-rating:first-child').siblingsByValue('.star-rating', 'lower', 'asc').length, 0, 'lowest element in asc order (first-child)');
});

test('select nth child in direction (ascending / descending)', function() {
	equal($('#stars-desc').nthChildInDirection('.star-rating', 5, 'desc').index('#stars-desc .star-rating'), 0, 'select greatest child in desc order (first-child)');
	equal($('#stars-desc').nthChildInDirection('.star-rating', 4, 'desc').index('#stars-desc .star-rating'), 1, 'select second-greatest child in desc order');
	equal($('#stars-desc').nthChildInDirection('.star-rating', 1, 'desc').index('#stars-desc .star-rating'), 4, 'select lowest child in desc order');
	equal($('#stars-asc').nthChildInDirection('.star-rating', 4, 'asc').index('#stars-asc .star-rating'), 3, 'select second-greatest child in asc order');
});

test('complete url (add context path)', function() {
	argue.url.contextPath = null;
	equal(argue.url('/login'), '/login', 'returns unmodified url when context path is not set');
	
	argue.url.setContextPath('/argue');
	equal(argue.url('/login'), '/argue/login', 'context path without trailing slash');
	
	argue.url.setContextPath('/argue2/');
	equal(argue.url('/login'), '/argue2/login', 'context path with trailing slash');
	equal(argue.url('/argue2/login'), '/argue2/login', 'do not add context path twice');
});

test('load templates', function() {
    ok(!argue.templates.handleData('<script id="xxx">{{= id}}</script>'), 'return false when data does not contain templates')
    ok(argue.templates.handleData('<script type="text/x-jquery-tmpl" id="js-tmpl-test">{{= id}}</script><div id="tmpl-dialog" class="modal-tmpl"></div>'), 'return true when data contains templates');
    equal($('#js-tmpl-test').length, 1, 'template appended to body');
    equal('5', $.tmpl('test', { 'id' : 5 }).text(), 'template compiled');
});

module('stars');
test('hover', function() {
	$('#stars-desc').stars();
	$('#stars-asc').stars({ 'order': 'asc' });
	
	$('#stars-desc .star-rating').eq(1).mouseenter();
	ok(!$('#stars-desc').hasClass('star-rating-read'), 'hoverIn removes class star-rating-read');
	equal($('#stars-desc .star-rating-hover').length, 4, '4 stars in descendant order are active when second star is hovered');

	$('#stars-asc .star-rating').eq(1).mouseenter();
	equal($('#stars-asc .star-rating-hover').length, 2, '2 stars in ascendant order are active when second star is hovered');
});

test('click', function() {
	var changeTriggerCount = 0;
	var changeTriggerCountStars = 0;
	$('#stars-asc').on('rating.change.byClick', function(event, stars) { changeTriggerCount++; changeTriggerCountStars += stars; });
	
	$('#stars-asc').stars({ 'order': 'asc' });
	$('#stars-asc .star-rating').eq(1).mouseenter().click();
	equal($('#stars-asc .star-rating-on').length, 2, '2 stars in ascendant order are on when second star gets clicked');
	equal(changeTriggerCount, 1, 'trigger rating.change gets called once');
	equal(changeTriggerCountStars, 2, 'trigger rating.change gets called once with the number of stars (2) in an optional argument');
	ok($('#stars-asc').hasClass('star-rating-read'), 'stars are locked after click');
	
	$('#stars-asc').trigger('rating.unlock');
	ok(!$('#stars-asc').hasClass('star-rating-read'), 'stars are unlocked after persist');
});

test('activate manually', function() {
	$('#stars-desc').stars({ 'order': 'desc' });
	$('#stars-desc').stars(4);
	equal($('#stars-desc .star-rating-on').length, 4, 'four stars are on (desc)');
	ok(!$('#stars-desc .star-rating').eq(0).hasClass('star-rating-on'), 'greatest star is not on (desc)');
	
	$('#stars-asc').stars({ 'order': 'asc' });
	$('#stars-asc').stars(4);
	equal($('#stars-asc .star-rating-on').length, 4, 'four stars are on (asc)');
	ok(!$('#stars-asc .star-rating').eq(4).hasClass('star-rating-on'), 'greatest star is not on (asc)');
});

module('ajax');
test('handle broadcast', function() {
	var testResult = {
		status: 200,
		responseBody: '{"broadcastSubject": "testHandleBroadcast", "id": "85"}'
	};
	
	var subjectHandlerCalled = false;
	var recievedId = null;
	var subjectHandler = function(event, data) {
		subjectHandlerCalled = true;
		recievedId = data['id'];
	}
	$(argue).on('testHandleBroadcast', subjectHandler);
	
	argue.comet.handleMessage(testResult);
	ok(subjectHandlerCalled, 'call handler of event testHandleBroadcast');
	equal(recievedId, 85, 'handler has access to properties in responseBody');
});

test('post', function() {
	jQuery.post = function() {};
	var data = [];
	
	argue.comet.isSubscribed = function() { return true; };
	argue.post('/test', data);
	equal(data[0]['value'], argue.comet.resourceId, 'set cometResourceId');

	argue.comet.isSubscribed = function() { return false; };
	argue.post('/test', data);
	equal(data['cometResourceId'], undefined, 'do not set cometResourceId when comet is not subscribed');
});

test('submit', function() {
	var successHandler = mockFunction();
	var mockCometHandler = mockFunction();
	$(argue).on('abcdef', mockCometHandler);
	
	var deferred = mockPost();	
	$('#ajax-form').ajaxSubmit({ 'onSuccess': successHandler });
	$('#ajax-form').submit();
	equal($('#ajax-form .ajax-indicator').length, 1, 'added ajax indicator');
	deferred.resolve({ 'id': 300, 'broadcastSubject': 'abcdef' });
	equal($('#ajax-form .ajax-indicator').length, 0, 'remove ajax indicator after ajax request is completed');
	equal(successHandler.counter, 1, 'call onSuccess function when ajax request was successful');
	equal(mockCometHandler.counter, 1, 'call comet handler');

	var deferred = mockPost();	
	$('#ajax-form2').ajaxSubmit();
	$('#ajax-form2').submit();
	deferred.resolve({ 'id': 300, 'broadcastSubject': 'abcdef' });
	equal(mockCometHandler.counter, 2, 'call comet handler when no handler for onSuccess is provided');
});

test('indicator', function() {
	$('#ajax-form').addAjaxIndicator();
	$('#ajax').addAjaxIndicator();
	equal($('#ajax-form .ajax-indicator').length, 1, 'add ajax indicator to form with button');
	equal($('#ajax .ajax-indicator').length, 1, 'add ajax indicator to normal element');

	$('#ajax-form').addAjaxIndicator();
	equal($('#ajax-form .ajax-indicator').length, 1, 'add ajax indicator only once');
	
	$('#ajax-form').removeAjaxIndicator();
	equal($('#ajax-form .ajax-indicator').length, 0, 'remove ajax indicator');
});

test('listErrors', function() {
	$.template('error', '<span class="alert">{{html errorMsg}}</span>');
	
	argue.listAjaxErrors($('#ajax-errors'), { responseText: '{"globalErrors": [], "fieldErrors": {"name": "is empty"}}' });
	equal($('#ajax-errors span').html(), 'is empty', 'single fieldError');
	argue.listAjaxErrors($('#ajax-errors'), { responseText: '{"globalErrors": ["xx", "yy"], "fieldErrors": {"name": "is empty", "amount": "must be set"}}' });
	equal($('#ajax-errors span').html(), 'xx<br>yy<br>is empty<br>must be set', 'multiple global- and fieldErrors');
	argue.listAjaxErrors($('#ajax-errors'), { responseText: '{"globalErrors": ["already exists"], "fieldErrors": {}}' });
	equal($('#ajax-errors span').html(), 'already exists', 'single globalError');
});

test('get', function() {
	var deferred1 = mockGet();
	argue.get('/test');
	equal($('#content2 .global-ajax-indicator-container .ajax-indicator').length, 1, 'added ajax-indicator');

	var deferred2 = mockGet();
	argue.get('/test');
	equal($('#content2 .global-ajax-indicator-container .ajax-indicator').length, 1, 'max one ajax-indicator per content');
	
	deferred1.resolve();
	equal($('#content2 .global-ajax-indicator-container .ajax-indicator').length, 1, 'remain ajax-indicator until all requests are done');
	deferred2.resolve();
	equal($('#content2 .global-ajax-indicator-container .ajax-indicator').length, 0, 'remove ajax-indicator after all requests are done');
});

test('links (address addon)', function() {
	equal(argue.ajaxLinks.firstDocumentTitle, 'Utils Test Suite', 'first document title is set');
	argue.ajaxLinks.handler = [];
	
	var handler = new argue.ajaxLinkHandler('topic', 0, 'topic', function() { return $('.topic-content')});
	handler.handles = function(event) { return event === "topic"; };
	handler.handle = mockFunction();
	equal(argue.ajaxLinks.handler.length, 1, "new linkHandler is registered");
	
	argue.ajaxLinks.onAddressChange("abc");
	equal(handler.handle.counter, 0, "don't call handler");
	argue.ajaxLinks.onAddressChange("topic");
	equal(handler.handle.counter, 1, "call handler");
});

test('link handler (address addon)', function() {
	var handler = new argue.ajaxLinkHandler('topic', 0, 'topic4', function() { return $('.topic-content')});
	
	ok(handler.handles({'pathNames': ['topic', '21']}), 'handle correct path');
	ok(!handler.handles({'pathNames': ['21', 'topic']}), "don't handle incorrect path");
	
	handler.show($('#content1'));
	ok($('#content1').is(':visible'), 'show content1');
	ok(!$('#content2').is(':visible'), 'hide other content elements');
	equal(document.title, 'test', 'set title');
	handler.show($('#content2'));
	equal(document.title, 'Utils Test Suite', 'reset title');
	
	handler.show = mockFunction();
	$(argue).on('topic4.loaded', function(event, content) { $(content).text('1234'); });
	handler.appendContent('<div class="content" id="loaded-content"></div>');
	equal($('#loaded-content').length, 1, 'appended content');
	equal($('#loaded-content').text(), '1234', 'call topic.loaded handler');
	handler.appendContent('<div class="content" id="loaded-content"></div>');
	equal($('#loaded-content').length, 1, 'prevent double appended content');
	handler.appendContent('<div class="nocontent" id="loaded-content2"></div>');
	equal($('#loaded-content2').length, 1, 'appended content');
	equal($('#loaded-content2').text(), '', "don't call topic.loaded handler");
	
	mockGet();
	handler.show = mockFunction();
	handler.handle({'path': '/123'});
	equal(jQuery.get.counter, 1, 'start get request');
	$('.content:last').append('<div class="topic-content"></div>');
	handler.handle({'path': '/123'});
	equal(jQuery.get.counter, 1, 'start get request only once');
	equal(handler.show.counter, 1, 'show content when already loaded');
});
