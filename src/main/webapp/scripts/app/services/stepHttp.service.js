'use strict';

angular.module('arterialeduApp')
.factory('StepHttp', function ($http) {
    var Step = {
        getSteps: function() {
            return $http.get('api/steps');
        },
        getStep: function(id) {
            return $http.get('api/steps/' + id);
        },
        getStepBySection: function(id) {
            return $http.get('api/steps/bysection/' + id);
        },   
    };
    return Step;
});


// 'use strict';

// angular.module('arterialeduApp')
//     .factory('Step', function ($resource) {
//         return $resource('api/steps/:id', {}, {
//             'query': { method: 'GET', isArray: true},
//             'get': {
//                 method: 'GET',
//                 transformResponse: function (data) {
//                     data = angular.fromJson(data);
//                     return data;
//                 }
//             },
//             'update': { method:'PUT' }
//         });
//     });
