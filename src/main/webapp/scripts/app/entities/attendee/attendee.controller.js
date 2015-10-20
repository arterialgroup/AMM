'use strict';

angular.module('arterialeduApp')
    .controller('AttendeeController', function ($scope, Attendee, Meeting, User, ParseLinks) {
        $scope.attendees = [];
        $scope.meetings = Meeting.query();
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Attendee.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.attendees = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Attendee.update($scope.attendee,
                function () {
                    $scope.loadAll();
                    $('#saveAttendeeModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Attendee.get({id: id}, function(result) {
                $scope.attendee = result;
                $('#saveAttendeeModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Attendee.get({id: id}, function(result) {
                $scope.attendee = result;
                $('#deleteAttendeeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Attendee.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAttendeeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.attendee = {attended: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
