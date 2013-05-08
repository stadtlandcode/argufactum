var HostUtils = {};

(function(HostUtils) {
	var isLive = function() {
		return window.location.host.indexOf('argufactum.de') >= 0;
	};

	HostUtils.v1Url = function(path) {
		var host = isLive() ? 'argufactum.de/v1' : (window.location.host.replace('8083', '8080') + '/v1');
		return 'http://' + host + path;
	};
	HostUtils.v2Url = function(path) {
		return 'http://' + window.location.host + path;
	};
})(HostUtils);