'use strict';

angular.module('arterialeduApp')
    .controller('MeetingModuleController', function ($scope, MeetingModule, Meeting, Module, ParseLinks) {
        $scope.meetingModules = [];
        $scope.meetings = Meeting.query();
        $scope.modules = Module.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            MeetingModule.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.meetingModules = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            MeetingModule.update($scope.meetingModule,
                function () {
                    $scope.loadAll();
                    $('#saveMeetingModuleModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            MeetingModule.get({id: id}, function(result) {
                $scope.meetingModule = result;
                $('#saveMeetingModuleModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            MeetingModule.get({id: id}, function(result) {
                $scope.meetingModule = result;
                $('#deleteMeetingModuleConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            MeetingModule.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteMeetingModuleConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.meetingModule = {dateAvailable: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
