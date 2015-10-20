'use strict';

angular.module('arterialeduApp')
    .controller('SectionDetailController', function ($scope, $stateParams, Section, Module, Section_type, Step) {
        $scope.section = {};
        $scope.load = function (id) {
            Section.get({id: id}, function(result) {
              $scope.section = result;
            });
        };
        $scope.load($stateParams.id);
    });
