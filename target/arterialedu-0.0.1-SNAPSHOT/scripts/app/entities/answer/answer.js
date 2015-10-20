'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('answer', {
                parent: 'entity',
                url: '/answer',
                data: {
                    roles: ['ROLE_ADMIN'], 
                    pageTitle: 'arterialeduApp.answer.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/answer/answers.html',
                        controller: 'AnswerController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('answer');
                        return $translate.refresh();
                    }]
                }
            })
            .state('answerDetail', {
                parent: 'entity',
                url: '/answer/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.answer.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/answer/answer-detail.html',
                        controller: 'AnswerDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('answer');
                        return $translate.refresh();
                    }]
                }
            });
    });
