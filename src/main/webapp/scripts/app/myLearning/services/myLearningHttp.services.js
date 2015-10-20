angular.module('arterialeduApp')
.factory('StepsHttp', function ($http) {
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

