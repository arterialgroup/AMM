'use strict';

angular.module('arterialeduApp')
    .factory('MyLearningSteps', function ($resource) {
            return $resource('api/steps/bysection/:sectionId', {}, {
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







// var Section = {
//     getSections: function() {
//         return $http.get('api/sections');
//     },
//     getSection: function(id) {
//         return $http.get('api/sections/'+ id);
//     },
//     getModules: function() {
//         return $http.get('api/modules');
//     },
//     createChapter: function(data) {
//         return $http.post('api/sections', data);
//     }
// };







// angular.module('arterialeduApp')
// .factory('Section', function ($http) {

//    var Section = {
//        getSections: function() {
//            return $http.get('api/sections');
//        }
//    };

//    return Section;
// });


