'use strict';

angular.module('arterialeduApp')
.factory('ServiceIdService', ['$rootScope', '$stateParams', '$state', '$location', function ($rootScope, $stateParams, $state, $location) {
    var sectionId = null;
    var sectionArray = [];

    function setId(data) {
    	sectionId = data;
    }

    function setSectionsArray(data) {
    	sectionArray = data
    }

    return {
    	setSectionId: sectionId,
    	setSectionArray: sectionArray
    }

}]);
