'use strict';

angular.module('arterialeduApp')
    .controller('MeetingTypeController', function ($scope, MeetingType) {
        $scope.meetingTypes = [];
        $scope.loadAll = function() {
            MeetingType.query(function(result) {
               $scope.meetingTypes = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            MeetingType.update($scope.meetingType,
                function () {
                    $scope.loadAll();
                    $('#saveMeetingTypeModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            MeetingType.get({id: id}, function(result) {
                $scope.meetingType = result;
                $('#saveMeetingTypeModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            MeetingType.get({id: id}, function(result) {
                $scope.meetingType = result;
                $('#deleteMeetingTypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            MeetingType.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMeetingTypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.meetingType = {name: null, description: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
