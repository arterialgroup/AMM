'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('questionGroup', {
                parent: 'entity',
                url: '/questionGroup',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.questionGroup.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionGroup/questionGroups.html',
                        controller: 'QuestionGroupController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questionGroup');
                        return $translate.refresh();
                    }]
                }
            })
            .state('questionGroupDetail', {
                parent: 'entity',
                url: '/questionGroup/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.questionGroup.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/questionGroup/questionGroup-detail.html',
                        controller: 'QuestionGroupDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('questionGroup');
                        return $translate.refresh();
                    }]
                }
            });
    });
