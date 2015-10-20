'use strict';

angular.module('arterialeduApp')
    .controller('Section_typeDetailController', function ($scope, $stateParams, Section_type) {
        $scope.section_type = {};
        $scope.load = function (id) {
            Section_type.get({id: id}, function(result) {
              $scope.section_type = result;
            });
        };
        $scope.load($stateParams.id);
    });
