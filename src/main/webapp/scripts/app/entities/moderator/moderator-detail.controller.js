'use strict';

angular.module('arterialeduApp')
    .controller('ModeratorDetailController', function ($scope, $stateParams, Moderator, User, Meeting) {
        $scope.moderator = {};
        $scope.load = function (id) {
            Moderator.get({id: id}, function(result) {
              $scope.moderator = result;
            });
        };
        $scope.load($stateParams.id);
    });
