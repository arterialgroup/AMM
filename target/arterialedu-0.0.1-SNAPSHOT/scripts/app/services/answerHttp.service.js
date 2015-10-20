'use strict';

angular.module('arterialeduApp')
.factory('answerHttp', function ($http) {
    var Answer = {
        // getAnswers: function() {
        //     return $http.get('api/steps');
        // },
        getAnswerById: function(id) {
            return $http.get('/api/answers/' + id);
        },
    };
    return Answer;
});
