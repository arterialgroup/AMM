'use strict';

angular.module('arterialeduApp')
    .controller('ModuleDetailController', function ($scope, $stateParams, Module, Section) {
        $scope.module = {};
        $scope.load = function (id) {
            Module.get({id: id}, function(result) {
              $scope.module = result;
            });
        };
        $scope.load($stateParams.id);
    });
