angular.module('arterialeduApp')
.factory('SectionHttp', function ($http) {
	var Section = {
		getSections: function() {
			return $http.get('api/sections');
		}
	};
	return Section;
});

