'use strict';

angular.module('arterialeduApp')
    .controller('QuestionGroupDetailController', function ($scope, $stateParams, QuestionGroup) {
        $scope.questionGroup = {};
        $scope.load = function (id) {
            QuestionGroup.get({id: id}, function(result) {
              $scope.questionGroup = result;
            });
        };
        $scope.load($stateParams.id);
    });
