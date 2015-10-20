'use strict';

angular.module('arterialeduApp')
    .controller('MeetingDetailController', function ($scope, $stateParams, Meeting, MeetingType) {
        $scope.meeting = {};
        $scope.load = function (id) {
            Meeting.get({id: id}, function(result) {
              $scope.meeting = result;
            });
        };
        $scope.load($stateParams.id);
    });
