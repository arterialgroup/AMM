'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('question_type', {
                parent: 'entity',
                url: '/question_type',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.question_type.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/question_type/question_types.html',
                        controller: 'Question_typeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('question_type');
                        return $translate.refresh();
                    }]
                }
            })
            .state('question_typeDetail', {
                parent: 'entity',
                url: '/question_type/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.question_type.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/question_type/question_type-detail.html',
                        controller: 'Question_typeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('question_type');
                        return $translate.refresh();
                    }]
                }
            });
    });
