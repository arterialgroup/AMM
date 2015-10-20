'use strict';

angular.module('arterialeduApp')
    .controller('MeetingTypeDetailController', function ($scope, $stateParams, MeetingType) {
        $scope.meetingType = {};
        $scope.load = function (id) {
            MeetingType.get({id: id}, function(result) {
              $scope.meetingType = result;
            });
        };
        $scope.load($stateParams.id);
    });
