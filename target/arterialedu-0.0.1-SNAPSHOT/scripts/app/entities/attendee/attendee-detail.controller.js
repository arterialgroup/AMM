'use strict';

angular.module('arterialeduApp')
    .controller('AttendeeDetailController', function ($scope, $stateParams, Attendee, Meeting, User) {
        $scope.attendee = {};
        $scope.load = function (id) {
            Attendee.get({id: id}, function(result) {
              $scope.attendee = result;
            });
        };
        $scope.load($stateParams.id);
    });
