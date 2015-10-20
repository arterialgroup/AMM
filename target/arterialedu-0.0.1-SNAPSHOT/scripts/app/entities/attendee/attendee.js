'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('attendee', {
                parent: 'entity',
                url: '/attendee',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.attendee.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/attendee/attendees.html',
                        controller: 'AttendeeController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('attendee');
                        return $translate.refresh();
                    }]
                }
            })
            .state('attendeeDetail', {
                parent: 'entity',
                url: '/attendee/:id',
                data: {
                    roles: ['ROLE_ADMIN'],
                    pageTitle: 'arterialeduApp.attendee.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/attendee/attendee-detail.html',
                        controller: 'AttendeeDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('attendee');
                        return $translate.refresh();
                    }]
                }
            });
    });
