'use strict';

angular.module('arterialeduApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('myModules', {
                parent: 'myLearning',
                url: '/my-modules',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'global.menu.myLearning.main'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/myLearning/myModules/partials/myModules.html',
                        controller: 'MyModulesController'
                    }
                },
                resolve: {                
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            }).state('myModulesDetail', {
                parent: 'myLearning',
                url: '/my-modules/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'global.menu.myLearning.myModulesDetail'
                },
                views: {
                    'content@': {
                        templateUrl:'scripts/app/myLearning/myModules/partials/myModulesDetail.html',
                        controller: 'MyModulesDetailController'
                    }
                },
                resolve: {
                    moduleId: function($stateParams) {
                        return $stateParams.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('myModulesDetail');
                        return $translate.refresh();
                    }]
                }
            }).state('moduleSectionDetail', {
                parent: 'myLearning',
                url: 'my-modules/section-detail/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'global.menu.myLearning.moduleSectionDetail'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/myLearning/myModules/partials/moduleSectionDetail.html',
                        controller: 'ModuleSectionDetailController'
                    }
                },
                resolve: {
                    sectionId: function($stateParams) {
                        return $stateParams.id;
                    },
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('moduleSectionDetail');
                        return $translate.refresh();
                    }]
                }
            }).state('sectionStepDetail', {
                parent: 'myLearning',
                url: 'section/steps/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'global.menu.myLearning.sectionStepDetail'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/myLearning/myModules/partials/sectionStepDetail.html',
                        controller: 'SectionStepsDetailController'
                    }
                },
                resolve: {
                        stepId: function($stateParams) {
                            return $stateParams.id;
                        },
                        translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                            $translatePartialLoader.addPart('sectionStepDetail');
                            return $translate.refresh();
                        }]
                    }
            });
});


