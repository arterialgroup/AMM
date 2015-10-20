'use strict';

angular.module('arterialeduApp')
.factory('userModuleByUserId', function ($http) {
    var Account = {
        getUserModule: function(id) {
            return $http.get('api/userModules/byuser/' + id);
        }   
    };
    return Account;
});

