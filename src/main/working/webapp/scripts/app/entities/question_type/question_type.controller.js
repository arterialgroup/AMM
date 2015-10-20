'use strict';

angular.module('arterialeduApp')
    .controller('Question_typeController', function ($scope, Question_type) {
        $scope.question_types = [];
        $scope.loadAll = function() {
            Question_type.query(function(result) {
               $scope.question_types = result;
            });
        };
        $scope.loadAll();

        $scope.showUpdate = function (id) {
            Question_type.get({id: id}, function(result) {
                $scope.question_type = result;
                $('#saveQuestion_typeModal').modal('show');
            });
        };

        $scope.save = function () {
            if ($scope.question_type.id != null) {
                Question_type.update($scope.question_type,
                    function () {
                        $scope.refresh();
                    });
            } else {
                Question_type.save($scope.question_type,
                    function () {
                        $scope.refresh();
                    });
            }
        };

        $scope.delete = function (id) {
            Question_type.get({id: id}, function(result) {
                $scope.question_type = result;
                $('#deleteQuestion_typeConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Question_type.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteQuestion_typeConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $('#saveQuestion_typeModal').modal('hide');
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.question_type = {name: null, description: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
