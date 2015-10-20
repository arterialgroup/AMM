'use strict';

    angular.module('arterialeduApp')
.factory('UserProgressHttp', function ($http) {
    var userProgress = {
   //      getProgress: function(data) {
			// return $http.get('api/user/progress/' + data);			
			    	
   // //      	$http({
			// //     "url": 'api/user/progress/start/54',
			// //     "dataType": 'application/json',
			// //     "method": 'GET'
			 
			// //   //   {
			// // 		// userModuleId: 1,
			// // 		// stepId: 2,
			// // 		// startDate: 0,
			// // 		// endDate: 0
			// //   //   },
			   
			// //    	// "Content-Type": "application/json"
			    
			// // })
   //      },
        getProgress: function(id) {
			return $http.get('api/user/progress/' + id);
		},
        startProgress: function(data) {
     
     
        	$http({
			    "url": 'api/user/progress/start',
			    "dataType": 'application/json',
			    "method": 'POST',
			    "data": data 
			  //   {
					// userModuleId: 1,
					// stepId: 2,
					// startDate: 0,
					// endDate: 0
			  //   },
			   
			   	// "Content-Type": "application/json"
			    
			}).success(function(data, response){
			
			
			}).error(function(data, error){
			
			});

        	// $http.post('api/user/progress/start');
        	// $http.get('api/user/progress/start').success(function(data) );
        	// $http(req).success(function(data){
        	
        	// });
        },
        updateProgress: function(data) {
     
     
        	$http({
			    "url": 'api/user/progress/update',
			    "dataType": 'application/json',
			    "method": 'POST',
			    "data": data 
			  //   {
					// userModuleId: 1,
					// stepId: 2,
					// startDate: 0,
					// endDate: 0
			  //   },
			   
			   	// "Content-Type": "application/json"
			    
			}).success(function(data, response){
			
			
			}).error(function(data, error){
			
			});

        	// $http.post('api/user/progress/start');
        	// $http.get('api/user/progress/start').success(function(data) );
        	// $http(req).success(function(data){
        	
        	// });
        },
        endProgress: function(data) {
     
        	$http({
			    "url": 'api/user/progress/end',
			    "dataType": 'application/json',
			    "method": 'POST',
			    "data": data 
			  //   {
					// userModuleId: 1,
					// stepId: 2,
					// startDate: 0,
					// endDate: 0
			  //   },
			   
			   	// "Content-Type": "application/json"
			    
			}).success(function(data, response){
			
			
			}).error(function(data, error){
			
			});

        	// $http.post('api/user/progress/start');
        	// $http.get('api/user/progress/start').success(function(data) );
        	// $http(req).success(function(data){
        	
        	// });
        }
    };
    return userProgress;
});



