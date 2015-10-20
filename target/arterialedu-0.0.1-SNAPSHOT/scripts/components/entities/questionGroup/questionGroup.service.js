'use strict';

angular.module('arterialeduApp')
    .factory('QuestionGroup', function ($resource) {
        return $resource('api/questionGroups/:id', {}, {
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
