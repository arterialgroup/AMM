'use strict';

angular.module('arterialeduApp')
    .controller('AnswerController', function ($scope, Answer, Question, ParseLinks) {
        $scope.answers = [];

        $scope.questions = Question.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Answer.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.answers = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Answer.update($scope.answer,
                function () {
                    $scope.loadAll();
                    $('#saveAnswerModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Answer.get({id: id}, function(result) {
                $scope.answer = result;
                $('#saveAnswerModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Answer.get({id: id}, function(result) {
                $scope.answer = result;
                $('#deleteAnswerConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Answer.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteAnswerConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.answer = {text: null, correct: false, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
