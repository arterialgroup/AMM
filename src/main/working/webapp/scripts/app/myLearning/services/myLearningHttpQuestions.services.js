angular.module('arterialeduApp')
.factory('QuestionsHttp', function ($http) {
	var Question = {
		getQuestions: function() {
			return $http.get('api/questions');
		},
		getQuestion: function() {
			return $http.get('api/questions/:id');			
		},
		getQuestionByStep: function(id) {
			return $http.get('api/questions/bystep/' + id );			
		}	
	};
	return Question;
});

