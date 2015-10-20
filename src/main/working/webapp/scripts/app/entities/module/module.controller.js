'use strict';

angular.module('arterialeduApp')
    .controller('ModuleController', function ($scope, Module, Section, ParseLinks) {
        $scope.modules = [];
        $scope.sections = Section.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Module.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.modules = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Module.update($scope.module,
                function () {
                    $scope.loadAll();
                    $('#saveModuleModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Module.get({id: id}, function(result) {
                $scope.module = result;
                $('#saveModuleModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Module.get({id: id}, function(result) {
                $scope.module = result;
                $('#deleteModuleConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Module.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteModuleConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.module = {name: null, description: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
