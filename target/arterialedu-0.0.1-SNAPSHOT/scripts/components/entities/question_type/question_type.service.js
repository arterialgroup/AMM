'use strict';

angular.module('arterialeduApp')
    .factory('Question_type', function ($resource) {
        return $resource('api/question_types/:id', {}, {
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
