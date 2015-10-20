'use strict';

angular.module('arterialeduApp')
    .controller('SectionController', function ($scope, Section, Module, Section_type, Step, ParseLinks) {
        $scope.sections = [];
        $scope.modules = Module.query();
        $scope.section_types = Section_type.query();
        $scope.steps = Step.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Section.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.sections = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Section.update($scope.section,
                function () {
                    $scope.loadAll();
                    $('#saveSectionModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Section.get({id: id}, function(result) {
                $scope.section = result;
                $('#saveSectionModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Section.get({id: id}, function(result) {
                $scope.section = result;
                $('#deleteSectionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Section.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSectionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.section = {name: null, description: null, series: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
