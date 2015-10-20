'use strict';

angular.module('arterialeduApp')
    .factory('User', function ($resource) {
        return $resource('api/users/:login', {}, {
                'query': {method: 'GET', isArray: true},
                'get': {
                    method: 'GET',
                    transformResponse: function (data) {
                        data = angular.fromJson(data);
                        return data;
                    }
                }
                // 'get': { method: 'GET', params: {}, isArray: false,
                //     interceptor: {
                //         response: function(response) {
                //             // expose response
                //             return response;
                //         }
                //     }
                // }
            });
        });
