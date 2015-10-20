'use strict';

angular.module('arterialeduApp')
    .controller('MeetingModuleDetailController', function ($scope, $stateParams, MeetingModule, Meeting, Module) {
        $scope.meetingModule = {};
        $scope.load = function (id) {
            MeetingModule.get({id: id}, function(result) {
              $scope.meetingModule = result;
            });
        };
        $scope.load($stateParams.id);
    });
