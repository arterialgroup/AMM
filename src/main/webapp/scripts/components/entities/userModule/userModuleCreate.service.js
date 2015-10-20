'use strict';

angular.module('arterialeduApp')
    .factory('UserModuleCreate', function ($resource) {
        return $resource('api/userModules', {}, {
            // 'query': { method: 'GET', isArray: true},
            // 'get': {
            //     method: 'GET',
            //     transformResponse: function (data) {
            //         data = angular.fromJson(data);
            //         return data;
            //     }
            // },
            'create': { method:'PUT' }
        });
    });
