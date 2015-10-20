'use strict';

angular.module('arterialeduApp')
    .controller('StepController', function ($scope, Step, Section, Question, ParseLinks) {
        $scope.steps = [];
        $scope.sections = Section.query();
        $scope.questions = Question.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            Step.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.steps = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            Step.update($scope.step,
                function () {
                    $scope.loadAll();
                    $('#saveStepModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            Step.get({id: id}, function(result) {
                $scope.step = result;
                $('#saveStepModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            Step.get({id: id}, function(result) {
                $scope.step = result;
                $('#deleteStepConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Step.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteStepConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.step = {name: null, description: null, content: null, series: null, id: null};
            $scope.editForm.$setPristine();
            $scope.editForm.$setUntouched();
        };
    });
