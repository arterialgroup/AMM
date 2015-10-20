'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('section_type', {
                parent: 'entity',
                url: '/section_type',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.section_type.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/section_type/section_types.html',
                        controller: 'Section_typeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('section_type');
                        return $translate.refresh();
                    }]
                }
            })
            .state('section_typeDetail', {
                parent: 'entity',
                url: '/section_type/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.section_type.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/section_type/section_type-detail.html',
                        controller: 'Section_typeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('section_type');
                        return $translate.refresh();
                    }]
                }
            });
    });
