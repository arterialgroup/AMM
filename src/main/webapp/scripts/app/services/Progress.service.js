'use strict';

angular.module('arterialeduApp')
.factory('ProgressService', ['$rootScope', 'MyLearningSection', 'moduleId', function ($rootScope, MyLearningSection, moduleId){


	return {

		sections: MyLearningSection.query({moduleId: moduleId})
		

		

	}


}]);
