'use strict';

angular.module('arterialeduApp')
.factory('MyLearningSectionHttp', function ($http) {
	var SectionByModule = {
		getSectionByModuleId: function(id) {
			return $http.get('api/sections/bymodule/' + id);
		}
	};
	return SectionByModule;
});

// angular.module('arterialeduApp')
//     .factory('MyLearningSection', function ($resource) {
//         return $resource('api/sections/bymodule/:moduleId', {}, {
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

