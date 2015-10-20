'use strict';

angular.module('arterialeduApp')
    .controller('UserModuleController', function ($scope, UserModule, Module, User, ParseLinks) {
        $scope.userModules = [];
        $scope.modules = Module.query();
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            UserModule.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.userModules = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            UserModule.update($scope.userModule,
                function () {
                    $scope.loadAll();
                    $('#saveUserModuleModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            UserModule.get({id: id}, function(result) {
                $scope.userModule = result;
                $('#saveUserModuleModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            UserModule.get({id: id}, function(result) {
                $scope.userModule = result;
                $('#deleteUserModuleConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            UserModule.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteUserModuleConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.userModule = {id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
