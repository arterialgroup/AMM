'use strict';

angular.module('arterialeduApp')
    .controller('StepDetailController', function ($scope, $stateParams, Step, Section, Question) {
        $scope.step = {};
        $scope.load = function (id) {
            Step.get({id: id}, function(result) {
              $scope.step = result;
            });
        };
        $scope.load($stateParams.id);
    });
