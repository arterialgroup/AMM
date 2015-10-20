'use strict';

angular.module('arterialeduApp')
    .controller('Section_typeController', function ($scope, Section_type) {
        $scope.section_types = [];
        $scope.loadAll = function() {
            Section_type.query(function(result) {
               $scope.section_types = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Section_type.get({id: id}, function(result) {
                $scope.section_type = result;
                $('#saveSection_typeModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.section_type.id != null) {
                Section_type.update($scope.section_type,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Section_type.save($scope.section_type,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Section_type.get({id: id}, function(result) {
                $scope.section_type = result;
                $('#deleteSection_typeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Section_type.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSection_typeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveSection_typeModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.section_type = {name: null, description: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
