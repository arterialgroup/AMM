'use strict';

angular.module('arterialeduApp')
    .controller('ModuleTypeDetailController', function ($scope, $stateParams, ModuleType) {
        $scope.moduleType = {};
        $scope.load = function (id) {
            ModuleType.get({id: id}, function(result) {
              $scope.moduleType = result;
            });
        };
        $scope.load($stateParams.id);
    });
