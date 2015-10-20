'use strict';

angular.module('arterialeduApp')
    .controller('UserModuleDetailController', function ($scope, $stateParams, UserModule, Module, User) {
        $scope.userModule = {};
        $scope.load = function (id) {
            UserModule.get({id: id}, function(result) {
              $scope.userModule = result;
            });
        };
        $scope.load($stateParams.id);
    });
