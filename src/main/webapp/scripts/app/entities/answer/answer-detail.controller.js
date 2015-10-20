'use strict';

angular.module('arterialeduApp')
    .controller('AnswerDetailController', function ($scope, $stateParams, Answer, Question) {
        $scope.answer = {};
        $scope.load = function (id) {
            Answer.get({id: id}, function(result) {
              $scope.answer = result;
            });
        };
        $scope.load($stateParams.id);
    });
