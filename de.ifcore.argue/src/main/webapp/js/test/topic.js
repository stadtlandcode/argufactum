test('history dialog', function() {
	var deferred = mockGet();
	var restoreFunction = mockFunction();
	$.template('historyTable', '<table><tr><td><abbr class="timeago" title="2012-04-24T22:24:44+02:00">time</abbr><button>t</button></td></tr></table>');
	
	argue.history.load('/test', restoreFunction);
	deferred.resolve({1:1});
	equal($('#history table.mock').length, 0, 'remove previous table in history dialog');
	equal($('#history table').length, 1, 'add new table to history dialog');
	notEqual($('#history abbr').text(), 'time', 'call timeago');
	ok($('#history').hasClass('in'), 'open dialog');
	
	$('#history button').click();
	equal(restoreFunction.counter, 1, 'bind restoreFunction to click event for all buttons');
});

module('edit');
var prepareEdit = function() {
	argue.templates.loaded = true;
	argue.topic.opts.editable = true;
	$.template('topicTools', '<button class="edit-topic-button" />');
	$(argue).triggerHandler('topic.loaded.tools');
	$(argue).triggerHandler('templates.loaded.dialogs');
	$(argue).triggerHandler('templates.loaded.editDialog');
};

test('open dialog', function() {
	prepareEdit();
	$('#edit-topic').append('<span class="alert"></span>');
	
	$('.edit-topic-button').click();
	ok($('#edit-topic').is(':visible'), 'show edit dialog');
	ok($('#edit-topic .alert').length === 0, 'remove alert');
	equal($('#edit-id').val(), 88, 'set topicId in edit dialog');
	equal($('#edit-term').val(), 'Testthema', 'set term in edit dialog');
	equal($('#edit-definition').val(), 'def1', 'set definition in edit dialog');
	equal($('#edit-category').val(), 81, 'set category in edit dialog');
});

test('submit form when something has changed only', function() {
	prepareEdit();
	var deferred = mockPost();
	
	$('.edit-topic-button').click();
	$('#edit-topic form').submit();
	equal(jQuery.post.counter, 0, 'nothing has changed - do not submit');
	ok(!$('#edit-topic').is(':visible'), "close edit dialog (if form doesn't get submitted)");

	$('.edit-topic-button').click();
	$('#edit-term').val('abc');
	$('#edit-topic form').submit();
	equal(jQuery.post.counter, 1, 'term has changed - do submit');
	deferred.resolve({1:1});
	ok(!$('#edit-topic').is(':visible'), "close edit dialog (if form gets submitted and ajax request finished successfully)");
	
	$('.edit-topic-button').click();
	$('#edit-term').val('');
	$('#edit-topic form').submit();
	equal(jQuery.post.counter, 1, 'term is empty - do not submit');
	
	$('.edit-topic-button').click();
	$('#edit-definition').val('abc');
	$('#edit-topic form').submit();
	equal(jQuery.post.counter, 2, 'definition has changed - do submit');

	$('.edit-topic-button').click();
	$('#edit-category').val(74);
	$('#edit-topic form').submit();
	equal(jQuery.post.counter, 3, 'category has changed - do submit');
});

test('perform edit', function() {
	prepareEdit();
	$('.edit-topic-button').click();
	$.template('modificationLog', '<span class="editor-note">{{= modificationLog.name}}</span>');
	$.template('topicEditedByAnotherUser', '<span class="alert">{{= term}}</span>');
	
	$(argue).triggerHandler('topic.editTerm', { termId: 84, text: 'abc', definition: '' });
	ok($('.topic-page-header h1 small').length === 0, 'no category in data - remove category');
	ok($('#edit-topic .alert').length === 0, "don't send an alert to edit form if the term hasn't changed");
	
	var data = {
		termId: 900,
		text: 'def',
		definition: 'xyz',
		modificationLog: {
			name: 'user1',
			userId: 10
		},
		category: {
			id: 910,
			name: 'cat1'
		}
	};
	$(argue).triggerHandler('topic.editTerm', data);
	equal($('.topic-page-header').data('term-id'), data.termId, 'set term id');
	equal($('.topic-page-header h1 span').text(), data.text, 'set term');
	equal($('.topic-page-header .definition').text(), data.definition, 'set definition');
	equal($('.topic-page-header .editor-note').text(), data.modificationLog.name, 'add modificationLog');
	equal($('.topic-page-header h1 small').text(), data.category.name, 'set category text');
	equal($('.topic-page-header h1 small').data('category-id'), data.category.id, 'set category id');
	ok($('#edit-topic .alert').length === 0, "don't send an alert to edit form if the given userId is the id of the current user");
	
	data.termId = 901;
	data.text = 'def2';
	data.modificationLog.userId = 11;
	$(argue).triggerHandler('topic.editTerm', data);
	equal($('#edit-topic .alert').text(), data.text, "send an alert to edit form");
});

module('share');
var prepareShare = function() {
	argue.templates.loaded = true;
	argue.topic.opts.editable = true;
	$.template('topicTools', '<button class="share-topic-button" />');
	$(argue).triggerHandler('topic.loaded.tools');
	$(argue).triggerHandler('templates.loaded.dialogs');
	$(argue).triggerHandler('templates.loaded.shareDialog');
	mockGet();
};

test('open dialog', function() {
	prepareShare();
	$('#add-access-right-form').append('<span class="alert"></span>');
	$('#access-right-contact').val('abcdef');
	
	$('.share-topic-button').click();
	ok($('#share-topic').is(':visible'), 'show share dialog');
	equal($('#visibility-topic-id').val(), 88, 'set topicId in visibility form');
	ok($('#visibility-public').is(':checked'), 'check visibility checkbox');
	
	ok($('#add-access-right-form .alert').length === 0, 'remove alert in access right form');
	equal($('#access-right-topic-id').val(), 88, 'set topicId in visibility form');
	equal($('#access-right-contact').val(), '', 'clear contact input in access right form');
});

test('load access rights', function() {
	prepareShare();
	var deferredGet = mockGet();
	mockPost();
	$.template('shareTopicLoader', '<tr><td>abc</td></tr>');
	$.template('accessRight', '<tr><td>{{= id}}<form action="#" method="post"><i></i></form></td></tr>');
	
	$('.share-topic-button').click();
	equal($('.access-rights-list tr').length, 1, 'add ajax-indicator to access rights list')
	deferredGet.resolve({ accessRights: [{id: 10}, {id:11}]});
	equal($('.access-rights-list tr').length, 2, 'remove ajax-indicator, add a row per access-right');
	
	$('.access-rights-list tr:first i').click();
	equal(jQuery.post.counter, 1, 'set clickhandler on i element');
});

test('submit visibility form when something has changed only', function() {
	prepareShare();
	
	mockPost();
	$('.share-topic-button').click();
	$('#set-visibility-form').submit();
	equal(jQuery.post.counter, 0, 'nothing has changed - do not submit');

	var deferred = mockPost();
	$('#visibility-public').prop('checked', false);
	$('#set-visibility-form').submit();
	equal(jQuery.post.counter, 1, 'visibility has changed - do submit');
	deferred.reject();
	ok($('#visibility-public').is(':checked'), 'reset checkbox on post-request error');
});

test('submit access right form when something has changed only', function() {
	prepareShare();
	
	mockPost();
	$('.share-topic-button').click();
	$('#add-access-right-form').submit();
	equal(jQuery.post.counter, 0, 'nothing has changed - do not submit');
	
	var deferred = mockPost();
	$('#access-right-contact').val('abc');
	$('#add-access-right-form').submit();
	equal(jQuery.post.counter, 1, 'contact input has changed - do submit');
	deferred.resolve({1:1});
	equal($('#access-right-contact').val(), '', 'clear contact input when submit has successfully completed');
});

test('change visibility', function() {
	$.template('copyright', '<p class="copyright"></p>');
	
	$(argue).triggerHandler('topic.editVisibility', { 'visibility': 'PRIVATE' });
	equal($('.topic-page-header').data('visibility'), 'PRIVATE', '[public to private] set visibility data attribute');
	equal($('.copyright').length, 0, '[public to private] remove copyright notice');
	
	$(argue).triggerHandler('topic.editVisibility', { 'visibility': 'PUBLIC' });
	equal($('.topic-page-header').data('visibility'), 'PUBLIC', '[private to public] set visibility data attribute');
	equal($('.copyright').length, 1, '[private to public] add copyright notice');
});

test('add access right', function() {
	prepareShare();
	$.template('accessRight', '<tr><td></td></tr>');
	var deferred = mockGet();
	var sharedTopicHandler = mockFunction();
	
	$(argue).triggerHandler('topic.addAccessRight', { 'writeAccess': 0 });
	$(argue).triggerHandler('topic.addAccessRight', { 'writeAccess': 1 });
	$('.access-rights-list tbody').append('<tr><td></td></tr>');
	$(argue).triggerHandler('topic.addAccessRight', { 'writeAccess': 0 });
	equal($('.access-rights-list tr').length, 2, 'add access rights to list when access rights are loaded');
});

test('remove access right', function() {
	prepareShare();
	$('.access-rights-list tbody').append('<tr data-access-right-id="101"></tr><tr data-access-right-id="102"></tr>');

	$(argue).triggerHandler('topic.removeAccessRight', { id: 102, userId: 11 });
	equal($('.access-rights-list tr').length, 1, 'remove row');
	equal($('.access-rights-list tr').data('access-right-id'), '101', 'removed correct row?');
	ok(!$('#access-denied').is(':visible'), "don't show access denied dialog");

	$(argue).triggerHandler('topic.removeAccessRight', { id: 101, userId: 10 });
	ok($('#access-denied').is(':visible'), 'show access denied dialog');
	ok(!$('.access-denied-absolutely').is(':visible'), 'hide absolutely denied message');
	ok($('.access-denied-readOnly').is(':visible'), 'show readOnly message');
});