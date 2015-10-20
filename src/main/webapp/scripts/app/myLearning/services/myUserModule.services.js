angular.module('arterialeduApp')
.factory('userModuleHttp', function ($http) {
	var userModule = {
		userModulePost: function() {
			$http.put('api/userModules');
		},
		userByUserId: function(id) {
			return $http.get('api/userModules/byuser/' + id);
		}
	};
	return userModule;
});

