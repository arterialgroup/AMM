'use strict';

angular.module('arterialeduApp')
    .controller('Question_typeDetailController', function ($scope, $stateParams, Question_type) {
        $scope.question_type = {};
        $scope.load = function (id) {
            Question_type.get({id: id}, function(result) {
              $scope.question_type = result;
            });
        };
        $scope.load($stateParams.id);
    });
