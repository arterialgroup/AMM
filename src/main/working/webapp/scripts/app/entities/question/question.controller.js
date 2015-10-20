'use strict';

angular.module('arterialeduApp')
    .controller('QuestionController', function ($scope, Question, Question_type, Step, Answer, ParseLinks) {
        $scope.questions = [];
        $scope.question_types = Question_type.query();
        $scope.steps = Step.query();
        $scope.answers = Answer.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Question.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.questions = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Question.update($scope.question,
                function () {
                    $scope.loadAll();
                    $('#saveQuestionModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Question.get({id: id}, function(result) {
                $scope.question = result;
                $('#saveQuestionModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Question.get({id: id}, function(result) {
                $scope.question = result;
                $('#deleteQuestionConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Question.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteQuestionConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.question = {text: null, series: null, showResults: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
