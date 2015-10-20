'use strict';

angular.module('arterialeduApp')
    .controller('LogoutController', function (Auth) {
        Auth.logout();
    });
