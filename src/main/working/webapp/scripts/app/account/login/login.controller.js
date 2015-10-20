'use strict';

angular.module('arterialeduApp')
    .controller('LoginController', function ($rootScope, $scope, $state, $timeout, Auth) {
        $scope.user = {};
        $scope.errors = {};

        $scope.rememberMe = true;
        $timeout(function (){angular.element('[ng-model="username"]').focus();});
        $scope.login = function () {
            console.log('working here');
            Auth.login({
                username: $scope.username,
                password: $scope.password,
                rememberMe: $scope.rememberMe

            }).then(function () {
                $scope.authenticationError = false;
                console.log('working here 2nd');
                console.log('here: ',$rootScope.previousStateName);
                
                if ($rootScope.previousStateName === 'register') {
                    $state.go('home');
                } else {
                    // $rootScope.back();
                    $state.go('home');
                }
            }).catch(function () {
                $scope.authenticationError = true;
            });
        };
    });
