'use strict';

angular.module('arterialeduApp')
.factory('registerUser', ['$http', '$q', 'Auth', function ($http, $q, Auth) {
	var user = {};
	return {
		register: function(credentials, callback) {

			var deferred = $q.defer();
			
			user = credentials;
			console.log('credentials in service: ',credentials);
			
			$http.post('/api/attendees/register/' + credentials.meetingId, credentials).success(function (){

				deferred.resolve(credentials);
				
				return credentials;
			});                      
			return deferred.promise;
		}      
	}
}]);
