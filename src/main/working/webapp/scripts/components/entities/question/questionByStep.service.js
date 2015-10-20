'use strict';

angular.module('arterialeduApp')
    .factory('QuestionByStep', function ($resource) {
        return $resource('api/questions/bystep/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
