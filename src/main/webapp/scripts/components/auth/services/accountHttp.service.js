'use strict';

    angular.module('arterialeduApp')
.factory('AccountHttp', function ($http) {
    var Account = {
        getAccount: function() {
            return $http.get('api/account');
        }
    };
    return Account;
});

