'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('step', {
                parent: 'entity',
                url: '/step',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.step.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/step/steps.html',
                        controller: 'StepController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('step');
                        return $translate.refresh();
                    }]
                }
            })
            .state('stepDetail', {
                parent: 'entity',
                url: '/step/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.step.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/step/step-detail.html',
                        controller: 'StepDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('step');
                        return $translate.refresh();
                    }]
                }
            });
    });
