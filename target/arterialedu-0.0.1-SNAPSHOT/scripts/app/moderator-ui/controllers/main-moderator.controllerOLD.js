'use strict';

angular.module('arterialeduApp')
	.controller('ModeratorMainController', ['$scope', 'meetingModulesHttp', 'userModuleHttp', 'userResponseHttp', 'UserProgressHttp', function ($scope, meetingModulesHttp, userModuleHttp, userResponseHttp, UserProgressHttp) {
		

		$scope.pageSection = {
			main: true,
			response: false
		};
		
		/**
		 *	Input submisson for activity ID 															
		 */ 
		$scope.findActivity = function(isValid) {
		
			$scope.validation = {
				validation: false,
				noActicityId: false
			};

			$scope.pageSection = {
				main: true,
				response: false
			};			

			if (isValid) {

				if ($scope.activityId.id !== '27893'){
					
					$scope.validation = {
						validation: true,
						noExistingActicityId: true
					};
				} else {
					
					$scope.meetingModules = meetingModulesHttp.getMeetingModules();

					$scope.meetingModules.then(function (response) {
						return response.data;

					}).then(function (meetingsFound) {
						
						angular.forEach(meetingsFound, function(value){
							$scope.activityIdFormId = value.meeting.activityId;							

							if ($scope.activityIdFormId === parseInt($scope.activityId.id)) {
								$scope.module = value.module;
								$scope.moduleId = value.module.id;

							} else { 
								console.log('We are NOT correct');
							}
						});				

						return $scope.moduleId;

					}).then(function (moduleId) {
						$scope.getUserModules(moduleId);
					}, function(err) {
						console.log('ERROR: There was an error: $scope.meetingModules: ',err);
					});
				}

			} else {
				$scope.validation = {
					validation: true,
					noActicityId: true
				};
			}
			
		};

		/**	------------------------------------------------------------
		 *	Function to use the meetingId to get userModules
		 */
		$scope.getUserModules = function(moduleId) {			

			var userModuleArray = [];
			$scope.userModuleData = userModuleArray;

			if (moduleId !== null || moduleId !== '') {
				$scope.userModule = userModuleHttp.getUserModule();
				
				$scope.userModule.then(function (response) {					
					var userModuleObj = response.data
					
					if (userModuleObj <= 0) {
						console.log('we are in');
						$scope.validation = {
							validation: true,
							noAttendees: true
						}
					} else {
						angular.forEach(userModuleObj, function(value, key){											

							if (value.module.id ===  moduleId) {

								userModuleArray.push(value);

								var progress = UserProgressHttp.getProgress(value.id);

								$scope.userModuleData[key].all = true;
								
								if (value.user.accreditationType === 'RACGP') {
									$scope.userModuleData[key].racgp = true;	
								} else if (value.user.accreditationType === 'ACRRM') {
									$scope.userModuleData[key].acrrm = true;	
								}

								progress.then(function (progressResponse) {

									var endDate = _.get(progressResponse.data, 'endDate');
									console.log('endDate: ',endDate);

									var date = _(endDate).toString();
									date = date.replace(/,/g, '-');

									$scope.userModuleData[key].endDate = date;
									$scope.userModuleData[key].isComplete = true;

									console.log('$scope.userModuleData: ',$scope.userModuleData);

								}, function (err) {
									console.log('ERROR: We coulld not found this users progess: ',err);
									$scope.userModuleData[key].isComplete = false;
								});


								//console.log('$scope.userModuleData: ',$scope.userModuleData);

								$scope.pageSection = {
									main: false,
									response: true
								}
							};
						}, function (err) {
								console.log('ERROR: There were no userModules found for this Activity ID');
						});
					};
				}
			};
		}


		/**
		 *	Custom fitler for moderator data filtering
		 */

		$scope.filterAll = true;
		$scope.checkTest =  {
			// all: true,
			// complete: true
		};
		$scope.filterToolBar = [];
		$scope.reset = function () {
			if ($scope.checkTest.all) {
				$scope.checkTest.complete = false;
				$scope.checkTest.racgp = false;
				$scope.checkTest.acrrm = false;

			} 
		}

		$scope.stateChagned = function (id) {
				console.log('in the change');
				console.log('id: ',$scope.checkTest[id]);


				if (id !== 'all') {
					$scope.checkTest.all = false;
				}

				if ($scope.checkTest[id]) {
					// alert(" State Changed")
					console.log('state changed');

					angular.forEach($scope.filterToolBar, function(value, key){
						//console.log('value is: ',value);

						var obj = {
							[id]: true
						}

						console.log('obj is: ',obj);

						if (value == obj) { console.log('we are true: ') };

					});


					if ($scope.filterToolBar.length <= 0) {
						console.log('we are in');
					};

					console.log("id is: ",id);
					if (id) {
						console.log('we are pushing true');
						$scope.filterToolBar.push({[id]: true});	
					} 
					// else if (id === 'racgp') {
					// 	console.log('we are pushing RACGP');
					// 	$scope.filterToolBar.push({[id]: 'RACGP'});
					// } else if (id === 'acrrm') {
					// 	console.log('we are pushing ACRRM');
					// 	$scope.filterToolBar.push({[id]: 'ACRRM'});
					// }
					


					console.log('filterToolBar: ',$scope.filterToolBar);

				} else {
					$scope.filterToolBar.pop([id]);					
				};
		}
		// $scope.fitlerByToolBar = function (filterValue) {



		// 	console.log('filterValue: ',filterValue);
		// 	if ($scope.filterToolBar.length > 0) {
		// 		$scope.filterToolBar.pop();
		// 	};
		// 	var i = $.inArray(filterValue, $scope.filterToolBar);			
		// 	if (i > -1) {
		// 		$scope.filterToolBar.splice(i,1);
		// 	} else {
		// 		$scope.filterToolBar.push(filterValue);
		// 	}
		// }

		$scope.moderatorFilter = function (value) {
			console.log("value is: ",value);
			
			if ($scope.filterToolBar.length > 0) {

				angular.forEach(value, function(data, key){
					console.log('data is: ',data);
					if ( data.isComplete !== $scope.checkTest.complete ) {
						console.log('data.isComplete: ',data.isComplete);
						console.log('$scope.checkTest.complete: ',$scope.checkTest.complete);
						console.log('Complete');
						return
					}
					
				});

				

				if (value.all == $scope.checkTest.all ) {
					console.log('all is checked');
					$scope.filterToolBar.pop();
					return
				}
			}
			return value;
		}

				// var filteredValue = $scope.filterToolBar[0];
				//console.log('filteredValue: ',filteredValue);
				
				// if (value.isComplete == filteredValue || value.user.accreditationType == filteredValue || value.isComplete == filteredValue && value.user.accreditationType == filteredValue) {
				// if (value.isComplete == checkTest.all) {
				// 	return
				// } else

				// _.forEach($scope.filterToolBar, function(n, i) {
				// 	console.log(i,' is: ',n);
				// 	if (value.isComplete !== $scope.checkTest.complete ) {
				// 	return
				// 	}
				// })
				// console.log('value.user.accreditationType: ',value.user.accreditationType);
				// console.log('$scope.checkTest.racgp: ',$scope.checkTest.racgp);
				// console.log('$scope.checkTest.acrrm: ',$scope.checkTest.acrrm);

				// if (value.isComplete !== $scope.checkTest.complete || value.user.accreditationType !== $scope.checkTest.racgp || value.user.accreditationType !== $scope.checkTest.acrrm ) {


				// if ( value.isComplete !== $scope.checkTest.complete ) {
				// if ( value.accreditationType !== $scope.checkTest.racgp ) {
				// 	console.log('value.isComplete: ',value.isComplete);
				// 	console.log('$scope.checkTest.complete: ',$scope.checkTest.complete);
				// 	console.log('Complete');
				// 	return
				// }

				// if ( value.accreditationType !== $scope.checkTest.acrrm && value.accreditationType !== $scope.checkTest.racgp ) {
				// 	console.log('value.isComplete: ',value.isComplete);
				// 	console.log('$scope.checkTest.complete: ',$scope.checkTest.complete);
				// 	console.log('Complete');
				// 	return
				// }

				// if ( value.isComplete !== $scope.checkTest.complete ) {
					
				// 	return 

				// } else 
				// if ( value.isComplete !== $scope.checkTest.complete || value.acrrm !== $scope.checkTest.acrrm ) {
		// 		angular.forEach(value, function(data, key){
		// 			console.log('data is: ',data);
		// 			if ( data.isComplete !== $scope.checkTest.complete ) {
		// 				console.log('data.isComplete: ',data.isComplete);
		// 				console.log('$scope.checkTest.complete: ',$scope.checkTest.complete);
		// 				console.log('Complete');
		// 			}
		// 			return
		// 		});

		// 		return value;
		// 		// else {	
		// 		// 	console.log('return whats not there');
		// 		// }
		// 		// if ( value.user.accreditationType == $scope.checkTest.racgp ) {
		// 		// 	console.log('value.user.accreditationType: ',value.user.accreditationType);
		// 		// 	console.log('we are RACGP');
		// 		// // if (value.user.accreditationType !== $scope.checkTest.acrrm) {
		// 		// 	return
		// 		// } 
		// 		// if (value.user.accreditationType == $scope.checkTest.acrrm ) {
		// 		// 	console.log('we are ACRRM');
		// 		// 	return
		// 		// } 
		// 		if (value.all == $scope.checkTest.all ) {
		// 			console.log('all is checked');
		// 			$scope.filterToolBar.pop();
		// 			return
		// 		}
 	// 			// else if () {
		// 		// 	console.log('all');
		// 		// 	$scope.filterToolBar.pop();
		// 		// }
		// 	}
		// 	//$scope.filteredValue = filteredValue;
		// 	//$scope.value = value;	
			
		// }

			
		
		


		/** 
		 *  The below snippet is for creating a collection of user data for moderator filtering
		 *	http://codepen.io/thelifenadine/pen/rammwv
		 */
		// var createFilterOptions = function(userModuleObj) {
		// 	var filters = [];
		// 	_.each(userModuleObj, function (response) {
		// 		var existingFilter = _.findWhere(filters, { accreditation: response.accreditation});

		// 		if (!existingFilter) {

		// 			var filter = {};
		// 			filter.accreditation = response.user.accreditation;
		// 			filter.values = [];
		// 			// filter.accreditationType = [];
		// 			// filter.state = [];
		// 			filter.values.push({accreditationType: response.user.accreditationType});
		// 			filter.values.push({state: response.user.state});

		// 			UserProgressHttp.getProgress(response.id).then(function (progressResponse) {
		// 				console.log('progressResponse: ',progressResponse.data);
		// 				if (progressResponse.data.endDate) {						
		// 					filter.values.push({complete: true});
		// 				};
		// 			});
		// 			filters.push(filter);
		// 		};
		// 	});
		// 	console.log('filters: ',filters);
		// 	$scope.Filters = filters;	
		
	}]);
// .filter('dynamicFilter', function () {

// 		return function(userModuleObj, filterCategories, scope) {
// 			var filtered = [];
// 			var userModuleObjFilters = _.filter(filterCategories, function (fc) {

// 				return _.any(fc.values, { 'complete': true });
				
// 			});

// 			_.each(userModuleObj, function (module) {

// 				var includeComplete = true;
// 				_.each(userModuleObjFilters, function (filter) {
// 					console.log('module in each: ',filter);
// 					var props = _.filter(module.properties, { 'name': filter.accreditation });
// 					if (!_.any(props, function (prop) { return _.any(filter.values, { 'value': prop.value, 'complete': true }); })) {
// 						includeComplete = false;
// 					}
// 				});
// 				if (includeComplete) {
// 					filtered.push(module);
// 				}
// 			});
// 			return filtered;
// 		}
// 	});



































