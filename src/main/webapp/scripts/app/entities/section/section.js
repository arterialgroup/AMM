'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('section', {
                parent: 'entity',
                url: '/section',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.section.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/section/sections.html',
                        controller: 'SectionController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('section');
                        return $translate.refresh();
                    }]
                }
            })
            .state('sectionDetail', {
                parent: 'entity',
                url: '/section/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.section.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/section/section-detail.html',
                        controller: 'SectionDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('section');
                        return $translate.refresh();
                    }]
                }
            });
    });
