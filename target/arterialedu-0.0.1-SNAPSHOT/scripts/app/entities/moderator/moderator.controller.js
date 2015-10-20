'use strict';

angular.module('arterialeduApp')
    .controller('ModeratorController', function ($scope, Moderator, User, Meeting) {
        $scope.moderators = [];
        $scope.users = User.query();
        $scope.meetings = Meeting.query();
        $scope.loadAll = function() {
            Moderator.query(function(result) {
               $scope.moderators = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            Moderator.update($scope.moderator,
                function () {
                    $scope.loadAll();
                    $('#saveModeratorModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Moderator.get({id: id}, function(result) {
                $scope.moderator = result;
                $('#saveModeratorModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Moderator.get({id: id}, function(result) {
                $scope.moderator = result;
                $('#deleteModeratorConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Moderator.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteModeratorConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.moderator = {id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
