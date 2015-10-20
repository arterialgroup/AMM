'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('moderator', {
                parent: 'entity',
                url: '/moderator',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.moderator.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/moderator/moderators.html',
                        controller: 'ModeratorController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('moderator');
                        return $translate.refresh();
                    }]
                }
            })
            .state('moderatorDetail', {
                parent: 'entity',
                url: '/moderator/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.moderator.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/moderator/moderator-detail.html',
                        controller: 'ModeratorDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('moderator');
                        return $translate.refresh();
                    }]
                }
            });
    });
