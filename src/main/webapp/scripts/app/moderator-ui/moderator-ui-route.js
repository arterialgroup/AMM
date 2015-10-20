'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('moderatorDashboard', {
                parent: 'moderatorMain',
                url: '/moderator-dashboard',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'global.menu.moderatorDashboard.main'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/moderator-ui/partials/moderator-dashboard.html',
                        controller: 'ModeratorDashboardMainController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            });
    });
