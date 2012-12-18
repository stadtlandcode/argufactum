module('common');
test("reordering", function() {
	argue.proContra.reorder.generic($('#reorder-fixture li'), argue.proContra.reorder.relevances, 'relevance');
	$('#reorder-fixture li').each(function() {
		equal($(this).index()+1, $(this).attr('data-correct-position'), 'correct position?');
	});
	expect(5);
});

test("restore", function() {
	var deferredGet = mockGet();
	var deferredPost = mockPost();
	$.template('restoreTable', '<table><tr data-id="2"><td><form><abbr class="timeago" title="2012-04-24T22:24:44+02:00">time</abbr></form></td></tr></table>');
	
	argue.proContra.restoreDialog.load('/test', '<h3>Servus!</h3>', 'argument');
	equal(jQuery.get.counter, 1, 'get url');

	equal($('#restore-mock-table').length, 1, 'wait for get');
	deferredGet.resolve({id: 10});
	equal($('#restore-mock-table').length, 0, 'remove existing table');
	equal($('#restore h3').text(), 'Servus!', 'replace title');
	equal($('#restore table tr[data-id="2"]').length, 1, 'add table template');
	equal($('#restore abbr').attr('title'), 'time', 'convert time');
	
	$('#restore form').submit();
	equal(jQuery.post.counter, 1, 'post url');
	deferredPost.resolve({id: 2});
	equal($('#restore table tr[data-id="2"]').length, 0, 'remove row');
});

test("edit Topic Theses (Dialog)", function() {
	$(argue).triggerHandler('templates.loaded.editTopicThesesDialog');
	argue.topic.getId = function() { return 10; };
	
	argue.proContra.editTopicThesesDialog.open();
	equal($('#edit-proContraTheses input').val(), 10, 'set topic id');
	equal($('#edit-proContraTheses select').val(), "PRO_CONTRA", 'select current message code');
	equal(argue.proContra.editTopicThesesDialog.getCurrentMessageCodeAppendix(), "PRO_CONTRA", 'getCurrentMessageCodeAppendix()');
	
	mockPost();
	argue.stopEvent = mockFunction();
	$('#edit-proContraTheses form').submit();
	equal(argue.stopEvent.counter, 1, 'prevent submit - nothing has changed');
	$('#edit-proContraTheses select').val('YES_NO');
	$('#edit-proContraTheses form').submit();
	equal(argue.stopEvent.counter, 1, 'do submit when something has changed');
});

test("edit Topic Theses", function() {
	var data = {
		'thesesMessageCodeAppendix': 'YES_NO',
		'proThesis': 'Yes',
		'contraThesis': 'No'
	};
	$(argue).triggerHandler('proContraTheses.edit', data);
	
	equal($('.box-list-container').data('message-code-appendix'), data.thesesMessageCodeAppendix, 'set message code appendix data attribute');
	equal($('.PRO h2').text(), data.proThesis, 'set proThesis in topic header h2');
	equal($('h2.PRO span:first').text(), data.proThesis, 'set proThesis in first span of h2 tags with class PRO');
	equal($('.CONTRA h2').text(), data.contraThesis, 'set contraThesis in topic header h2');
	equal($('h2.CONTRA span:first').text(), data.contraThesis, 'set contraThesis in first span of h2 tags with class CONTRA');
});

module('argueBox');
test('create', function() {
	var box = new argue.proContra.argueBox('argument');
	box.addTools = mockFunction();
	box.relevance.updateStars = mockFunction();
	box.userRelevance.restore = mockFunction();
	$.fn.updateModificationLog = mockFunction();
	argue.url.setContextPath('/abc');
	
	$.template('argument', '<div class="argument"><span>{{= id}}</span><span>{{= url}}</span><abbr class="timeago" title="2012-04-24T22:24:44+02:00">time</abbr></div>');
	var fragment = box.create({id: 10});
	equal($(fragment).find('span:first').text(), 10, 'replace id tmpl variable');
	equal($(fragment).find('abbr').attr('title'), 'time', 'call timeago');
	equal(box.addTools.counter, 1, 'call addTools');
	equal(box.relevance.updateStars.counter, 1, 'call relevance.update');
	equal(box.userRelevance.restore.counter, 1, 'call userRelevance.restore');
	equal($.fn.updateModificationLog.counter, 1, 'call updateModificationLog');

	var fragment2 = box.create({id: 10, url: '/test'});
	equal($(fragment2).find('span:last').text(), '/abc/test', 'replace url tmpl variable');
});

test('addTools (without rights)', function() {
	var boxHandler = new argue.proContra.argueBox('argument');
	argue.topic.opts.editable = false;
	argue.topic.opts.voteable = false;
	ok(!boxHandler.addTools($('#argument-box')), 'return false when topic is not editable or voteable');
	
});
	
test('addTools (voteable)', function() {
	var box = $('#argument-box');
	var boxHandler = new argue.proContra.argueBox('argument');
	var deferred = mockPost();
	$.fn.stars = mockFunction('this');
	boxHandler.userRelevance.getCurrent = function(box) { if(box) { return 'AVERAGE'; } }
	boxHandler.userRelevance.onChange = mockFunction();
	boxHandler.userRelevance.onSubmit = mockFunction();
	boxHandler.userRelevance.onSuccess = mockFunction();
	$.template('argumentTools', '<form class="stars"></form><div class="tool-buttons"></div>')
	argue.topic.opts.editable = false;
	argue.topic.opts.voteable = true;
	
	boxHandler.addTools(box);
	ok($(box).find('.box-tools').hasClass('box-vote'), 'add class box-vote to box-tools');
	equal($(box).find('.stars').length, 1, 'add stars form');
	equal($(box).find('.tool-buttons').length, 0, 'remove div with class tool-buttons');
	equal($.fn.stars.counter, 2, 'call stars');
	equal($.fn.stars.args[0], 2, 'activate 2 stars');
	
	$(box).find('.stars').trigger('rating.change');
	equal(boxHandler.userRelevance.onChange.counter, 1, 'call userRelevance.onChange on rating.change event');
	$(box).find('.stars').submit();
	equal(boxHandler.userRelevance.onSubmit.counter, 1, 'call userRelevance.onSubmit on stars form submit');
	deferred.resolve({1:1});
	equal(boxHandler.userRelevance.onSuccess.counter, 1, 'call userRelevance.onSuccess on successful stars form submit');
});

test('addTools (voteable and editable)', function() {
	var box = $('#argument-box');
	var boxHandler = new argue.proContra.argueBox('argument', { restoreBox: 'restoreBox' });
	mockPost();
	$.fn.stars = mockFunction('this');
	boxHandler.editForm.open = mockFunction();
	argue.history.load = mockFunction();
	$.template('argumentTools', '<form class="stars"></form><div class="tool-buttons"><button class="edit-box-button"></button><form class="delete-box-form" method="post"><button type="button"></button></form><button class="box-history-button" data-history-url="/history"></button></div>')
	argue.topic.opts.editable = true;
	argue.topic.opts.voteable = true;
	
	boxHandler.addTools(box);
	equal($(box).find('.stars').length, 1, 'add stars form');
	equal($(box).find('.tool-buttons').length, 1, 'add tool buttons');
	
	$(box).find('.edit-box-button').click();
	equal(boxHandler.editForm.open.counter, 1, 'call editForm.open on edit button click');
	
	$(box).find('.delete-box-form button').click();
	equal(jQuery.post.counter, 1, 'submit delete form on delete button click');
	equal(argue.confirm.args[0], 'confirm?', 'require confirmation of delete');
	
	$(box).find('.box-history-button').click();
	equal(argue.history.load.counter, 1, 'call history.load on history button click');
	equal(argue.history.load.args[0], '/history', 'call history.load function with correct url attribute');
	equal(argue.history.load.args[1], 'restoreBox', 'call history.load function with correct restoreFunction attribute');
});

test('edit', function() {
	var box = $('#argument-box');
	var boxHandler = new argue.proContra.argueBox('argument');
	$.fn.updateModificationLog = mockFunction();
	$.template('argumentEditedByAnotherUser', '<span>{{= text}}</span>');
	
	data = {
		id: 99,
		textId: 1001,
		text: 'xyz123'
	};
	boxHandler.edit(null, data, box);
	equal($(box).data('text-id'), data.textId, 'update textId');
	equal($(box).find('.box-text').text(), data.text, 'update text');
	equal($('#child-page-title').text(), data.text, 'update text in childPage');
	equal($.fn.updateModificationLog.counter, 1, 'call updateModificationLog');
	equal($('h2.PRO span').text(), 'PRO', "don't update text of h2 outside page header");
	
	boxHandler.editForm.alert = mockFunction();
	boxHandler.edit(null, {id: 88, textId: 1002, text: 'def123'}, box);
	equal(boxHandler.editForm.alert.counter, 1, 'add alert to visible edit form');
	equal(boxHandler.editForm.alert.args[1].text(), 'def123', 'pass alert tmpl as argument');
});

test('editForm (alert)', function() {
	var box = $('#argument-box-edit');
	var boxHandler = new argue.proContra.argueBox('argument');
	$('.edit-form').append('<span class="alert prev-alert"></span>');
	boxHandler.editForm.alert(box, '<span class="alert"></span>');
	equal($(box).find('.edit-form .prev-alert').length, 0, 'remove previous alert');
	equal($(box).find('.edit-form .alert').length, 1, 'add alert');
});

test('editForm (isOpen)', function() {
	var box = $('#argument-box-edit');
	var boxHandler = new argue.proContra.argueBox('argument');
	ok(boxHandler.editForm.isOpen(box), "editForm is visible");
	$(box).find('.edit-form').hide();
	ok(!boxHandler.editForm.isOpen(box), "editForm isn't visible");
});
test('editForm (onSubmit)', function() {
	var box = $('#argument-box-edit');
	var boxHandler = new argue.proContra.argueBox('argument');
	boxHandler.editForm.closeAll = mockFunction();
	argue.stopEvent = mockFunction();
	
	boxHandler.editForm.onSubmit.call($(box).find('.edit-form'));
	equal(argue.stopEvent.counter, 1, "stop submit when textarea is empty");
	
	$(box).find('textarea').val('changed');
	boxHandler.editForm.onSubmit.call($(box).find('.edit-form'));
	equal(argue.stopEvent.counter, 1, "pass submit when textarea has changed");
	
	$(box).find('textarea').val('abc');
	boxHandler.editForm.onSubmit.call($(box).find('.edit-form'));
	equal(argue.stopEvent.counter, 2, "stop submit when textarea hasn't changed");
});

test('editForm (open)', function() {
	var box = $('#argument-box-edit');
	var boxHandler = new argue.proContra.argueBox('argument');
	$.template('editArgueBox', '<span>{{= text}}</span>');
	
	boxHandler.editForm.closeAll = mockFunction();
	boxHandler.editForm.open.call($(box).find('.edit-form .box-tools'));
	equal(boxHandler.editForm.closeAll.counter, 1, 'call close function');
});

test('editForm (close)', function() {
	var box = $('#argument-box-edit');
	var boxHandler = new argue.proContra.argueBox('argument');
	boxHandler.editForm.closeAll();
	equal($('.fixture-edit-form').length, 0, 'remove all previous edit forms');
	equal($(box).find('.box-content, .box-tools').filter(':visible').length, 2, 'show box-content and box-tools on close');
});

test('remove', function() {
	var box = $('#argument-box');
	var boxEdit = $('#argument-box-edit');
	var boxHandler = new argue.proContra.argueBox('argument');
	var onDelete = mockFunction();
	$(argue).on('argument.delete', onDelete);
	var onPostDelete = mockFunction();
	$(argue).on('argument.postDelete', onPostDelete);
	boxHandler.editForm.alert = mockFunction();
	argue.stopEvent = mockFunction();
	
	boxHandler.remove(null, null, box);
	equal($('#argument-box').length, 0, 'remove box');
	equal(onPostDelete.counter, 1, 'trigger postDelete event');
	ok($(onPostDelete.args[1]).is('#argument-list'), 'trigger postDelete event with list argument');

	boxHandler.remove(null, null, boxEdit);
	equal($('#argument-box-edit').length, 1, "don't remove box when edit form is visible");
	equal(boxHandler.editForm.alert.counter, 1, 'alert to edit form');
	equal(argue.stopEvent.counter, 1, 'stop event');
	$(boxEdit).trigger('editForm.close');
	equal(onDelete.counter, 1, 'delete on editForm.close event');
	
	$(argue).off('argument.delete');
	$(argue).off('argument.postDelete');
});

test('userRelevance (onChange)', function() {
	var form = $('#argument-box-stars .stars');
	var boxHandler = new argue.proContra.argueBox('argument');
	var onSubmit = mockFunction();
	$(form).submit(onSubmit);
	
	boxHandler.userRelevance.onChange.call(form, null, 3);
	equal($(form).find('input').val(), 'HIGH', 'set given number of stars as relevance');
	equal(onSubmit.counter, 1, 'submit form');
});

test('userRelevance (onSubmit)', function() {
	var form = $('#argument-box-stars .stars');
	var boxHandler = new argue.proContra.argueBox('argument');
	argue.stopEvent = mockFunction();
	boxHandler.userRelevance.onError = mockFunction();
	
	boxHandler.userRelevance.onSubmit.call(form);
	equal(argue.stopEvent.counter, 1, 'stop event (relevance unchanged)');
	equal(boxHandler.userRelevance.onError.counter, 1, 'call onError (relevance unchanged)');
	
	$(form).find('input').val('');
	boxHandler.userRelevance.onSubmit.call(form);
	equal(argue.stopEvent.counter, 1, 'pass event (relevance changed)');

	$(form).find('input').val('HIGH');
	boxHandler.userRelevance.onSubmit.call(form);
	equal(argue.stopEvent.counter, 1, 'pass event (relevance changed)');
});

test('userRelevance (onError)', function() {
	var form = $('#argument-box-stars .stars');
	var boxHandler = new argue.proContra.argueBox('argument');
	$.fn.stars = mockFunction('this');
	var onUnlock = mockFunction();
	$(form).on('rating.unlock', onUnlock);
	
	boxHandler.userRelevance.onError(form);
	equal($.fn.stars.counter, 1, 'call .stars function');
	equal($.fn.stars.args[0], 2, 'set 2 stars active');
	equal(onUnlock.counter, 1, 'trigger unlock event');
	
	$(form).off('rating.unlock');
});

test('userRelevance (onSuccess)', function() {
	var form = $('#argument-box-stars .stars');
	var boxHandler = new argue.proContra.argueBox('argument');
	var onUnlock = mockFunction();
	$(form).on('rating.unlock', onUnlock);
	
	$(form).find('input').val('LOW');
	boxHandler.userRelevance.onSuccess(form);
	equal($('#argument-box-stars').data('user-relevance'), 'LOW', 'set user-relevance data of box to value of form');
	equal(onUnlock.counter, 1, 'trigger unlock event');
});

test('userRelevance (restore)', function() {
	var box = $('#argument-box-stars');
	var boxHandler = new argue.proContra.argueBox('argument');
	argue.getUserId = function() { return 41; }
	
	boxHandler.userRelevance.restore(box, [{'userId': 9, 'relevance': 'AVERAGE'}, {'userId': 41, 'relevance': 'LOW'}]);
	equal($(box).data('user-relevance'), 'LOW', 'set user relevance to LOW');
});

test('edit relevance', function() {
	var box = $('#argument-box-stars');
	var boxHandler = new argue.proContra.argueBox('argument');
	boxHandler.relevance.updateStars = mockFunction();
	
	var data = {
		relevance: {
			value: 'LOW',
			label: 'low relevance'
		}
	};
	boxHandler.relevance.edit(null, data, box);
	equal($(box).data('relevance'), data.relevance.value, 'update data of box');
	equal($(box).find('.stats-box-relevance').attr('title'), data.relevance.label, 'update title of stats box');
	equal(boxHandler.relevance.updateStars.counter, 1, 'call relevance.update');
	
	data.relevance.value = null;
	boxHandler.relevance.edit(null, data, box);
	equal($(box).data('relevance'), '', 'set empty relevance when relevance is null');
	equal(boxHandler.relevance.updateStars.counter, 2, 'call relevance.update');
});

test('update relevance', function() {
	var box = $('#argument-box-stars');
	var boxHandler = new argue.proContra.argueBox('argument');
	$.fn.stars = mockFunction('this');
	
	$(box).data('relevance', 'LOW');
	boxHandler.relevance.updateStars(box);
	equal($.fn.stars.counter, 2, 'call stars function two times');
	equal($.fn.stars.argsPerCall[1][0]['order'], 'asc', 'set asc order for stars');
	equal($.fn.stars.argsPerCall[2][0], 1, 'activate 1 star');
	
	$(box).data('relevance', '');
	boxHandler.relevance.updateStars(box);
	equal($.fn.stars.argsPerCall[4][0], 0, 'deactivate all stars when relevance is null');
});

module('argueBoxGroup');
test('getEntity', function() {
	var group = new argue.proContra.argueBoxGroup('.topic-content', 'argument');
	equal(group.getEntity(71).length, 1, 'find an argument with id 71');
	equal(group.getEntity(62).length, 0, 'find an argument with id 62 (non existent)');
});

test('getContent', function() {
	var group = new argue.proContra.argueBoxGroup('.argument-content', 'fact', {
		parentList: new argue.proContra.argueBoxGroup('.topic-content', 'argument'),
		parentEntityName: 'argument'
	});
	equal(group.getContent().length, 2, 'find all content elements');
	equal(group.getContent(99).length, 1, 'find content element with given parentId');
});

test('getParentId', function() {
	var group = new argue.proContra.argueBoxGroup('.argument-content', 'fact', {
		parentEntityName: 'argument'
	});
	equal(group.getParentId($('#argument-content-99 .CONFIRMATIVE')), 99, 'id of closest content');
});

test('getTypeContainer', function() {
	var group = new argue.proContra.argueBoxGroup('.argument-content', 'fact', {
		parentList: new argue.proContra.argueBoxGroup('.topic-content', 'argument'),
		parentEntityName: 'argument'
	});
	equal(group.getTypeContainer('CONFIRMATIVE', 99).length, 1, 'get confirmative container');
});

test('getList', function() {
	var group = new argue.proContra.argueBoxGroup('.argument-content', 'fact', {
		parentList: new argue.proContra.argueBoxGroup('.topic-content', 'argument'),
		parentEntityName: 'argument'
	});
	equal(group.getList('CONFIRMATIVE', 99).length, 1, 'get confirmative fact list');
});

test('add', function() {
	var group = new argue.proContra.argueBoxGroup('.topic-content', 'argument');
	group.getList = mockFunction($('#topic-content .PRO ul'));
	group.boxHandler.create = mockFunction('<span id="new-mock-argument"></span>');
	group.updateParentStats = mockFunction();
	
	data = {
		id: 74,
		type: 'PRO',
		parentId: 114
	};
	group.add(null, data);
	equal(group.boxHandler.create.args[1], 3, 'pass correct number');
	equal(group.updateParentStats.counter, 1, 'call updateParentStats');
	equal($('#new-mock-argument').length, 1, 'insert new element');
});

test('update stats of argument', function() {
	var box = $('#argument-with-stats');
	$.template("argumentFactStat", '<span id="statConfirmative">{{= numberOfConfirmativeFacts}}</span><span id="statDebilitative">{{= numberOfDebilitativeFacts}}</span><span id="statUrl">{{= url}}</span>');
	argue.proContra.argumentList.boxHandler.updateStats(box, $('#argument-content-99'));
	
	equal($(box).find('#statConfirmative').text(), 2, '2 confirmative facts');
	equal($(box).find('#statDebilitative').text(), 1, '1 debilitative fact');
	equal($(box).find('#statUrl').text(), '/argument/490', 'set url');
});