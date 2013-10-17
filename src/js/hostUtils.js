var HostUtils = {};

(function(HostUtils) {
	var isLive = function() {
		return window.location.host.indexOf('argufactum.de') >= 0;
	};

	HostUtils.v1Url = function(path) {
		var host = isLive() ? 'api.argufactum.de' : (window.location.host.replace('8445', '8444'));
		return 'http://' + host + path;
	};
	HostUtils.v2Url = function(path) {
		return 'http://' + window.location.host + path;
	};
})(HostUtils);