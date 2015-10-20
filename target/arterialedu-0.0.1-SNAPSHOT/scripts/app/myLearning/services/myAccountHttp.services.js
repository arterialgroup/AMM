'use strict';

angular.module('arterialeduApp')
.factory('AccountHttp', function ($http) {
	var Step = {
		getSteps: function() {
			return $http.get('api/steps');
		},
		getStep: function() {
			return $http.get('api/steps/:id');			
		},	
		getStepsById: function() {
			return $http.get('api/steps/bysection/:id');			
		}	
	};
	return Step;
});



angular.module('arterialeduApp')
    .factory('Account', function Account($resource) {
        return $resource('api/account', {}, {
            'get': { method: 'GET', params: {}, isArray: false,
                interceptor: {
                    response: function(response) {
                        // expose response
                        return response;
                    }
                }
            }
        });
    });
