'use strict';

angular.module('arterialeduApp')
    .controller('MeetingController', function ($scope, Meeting, MeetingType, ParseLinks) {
        $scope.meetings = [];
        $scope.meetingtypes = MeetingType.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Meeting.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.meetings = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Meeting.update($scope.meeting,
                function () {
                    $scope.loadAll();
                    $('#saveMeetingModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Meeting.get({id: id}, function(result) {
                $scope.meeting = result;
                $('#saveMeetingModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Meeting.get({id: id}, function(result) {
                $scope.meeting = result;
                $('#deleteMeetingConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Meeting.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMeetingConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.meeting = {name: null, startDate: null, days: null, activityId: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
