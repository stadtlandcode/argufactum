(function(c) {
	c.data = {
		options: [{
			id: 1,
			label: 'MIT',
			position: 1
		}, {
			id: 2,
			label: 'LGPL',
			position: 2
		}],
		criteria: [{
			id: 1,
			label: 'Copyleft',
			position: 1,
			dataTypeId: 'boolean'
		}, {
			id: 2,
			label: 'Modifikationen',
			position: 2,
			dataTypeId: 'boolean'
		}],
		cells: [{
			optionId: 1,
			criteriumId: 1,
			value: true
		}, {
			optionId: 1,
			criteriumId: 2,
			value: true
		}, {
			optionId: 2,
			criteriumId: 1,
			value: true
		}, {
			optionId: 2,
			criteriumId: 2,
			value: false
		}]
	};
})(comparison);