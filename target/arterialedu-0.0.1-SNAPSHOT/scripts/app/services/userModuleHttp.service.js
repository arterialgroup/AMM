'use strict';

angular.module('arterialeduApp')
.factory('userModule', function ($http) {
    var userModule = {
        getUserModule: function(pageNum) {
            return $http.get('api/userModules?page=' + pageNum);
        }   
    };
    return userModule;
});

