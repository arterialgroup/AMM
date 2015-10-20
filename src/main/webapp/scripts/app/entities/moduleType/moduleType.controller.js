'use strict';

angular.module('arterialeduApp')
    .controller('ModuleTypeController', function ($scope, ModuleType) {
        $scope.moduleTypes = [];
        $scope.loadAll = function() {
            ModuleType.query(function(result) {
               $scope.moduleTypes = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            ModuleType.update($scope.moduleType,
                function () {
                    $scope.loadAll();
                    $('#saveModuleTypeModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            ModuleType.get({id: id}, function(result) {
                $scope.moduleType = result;
                $('#saveModuleTypeModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            ModuleType.get({id: id}, function(result) {
                $scope.moduleType = result;
                $('#deleteModuleTypeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            ModuleType.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteModuleTypeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.moduleType = {name: null, description: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
