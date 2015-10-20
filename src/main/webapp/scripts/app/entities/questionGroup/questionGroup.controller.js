'use strict';

angular.module('arterialeduApp')
    .controller('QuestionGroupController', function ($scope, QuestionGroup) {
        $scope.questionGroups = [];
        $scope.loadAll = function() {
            QuestionGroup.query(function(result) {
               $scope.questionGroups = result;
            });
        };
        $scope.loadAll();

        $scope.create = function () {
            QuestionGroup.update($scope.questionGroup,
                function () {
                    $scope.loadAll();
                    $('#saveQuestionGroupModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            QuestionGroup.get({id: id}, function(result) {
                $scope.questionGroup = result;
                $('#saveQuestionGroupModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            QuestionGroup.get({id: id}, function(result) {
                $scope.questionGroup = result;
                $('#deleteQuestionGroupConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            QuestionGroup.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteQuestionGroupConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.questionGroup = {text: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
