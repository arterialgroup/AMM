'use strict';

angular.module('arterialeduApp')
    .controller('QuestionDetailController', function ($scope, $stateParams, Question, Question_type, Step, Answer) {
        $scope.question = {};
        $scope.load = function (id) {
            Question.get({id: id}, function(result) {
              $scope.question = result;
            });
        };
        $scope.load($stateParams.id);
    });
