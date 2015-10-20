angular.module('arterialeduApp')
.factory('userModuleProgressHttp', function ($http) {
	var start = {
		userModuleStartPut: function() {
			$http.put('api/user/progress/start');
		},
		getUserModule: function() {
			$http.get('api/user/progress/33' )
		}
	};
	return start;
});

