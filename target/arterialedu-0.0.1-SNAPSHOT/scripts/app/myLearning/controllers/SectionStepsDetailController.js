// 'use strict';

angular.module('arterialeduApp')
.controller('SectionStepsDetailController', ['$rootScope', '$scope', '$state', 'userResponseHttp', '$controller', 'answerHttp', 'Step', 'StepHttp', 'QuestionByStep', 'QuestionsHttp', 'UserProgressHttp', 'stepId', 'UserHttp', 'AccountHttp', 'userModuleHttp', '$stateParams', 'ServiceIdService', function ($rootScope, $scope, $state, userResponseHttp, $controller, answerHttp, Step, StepHttp, QuestionByStep, QuestionsHttp, UserProgressHttp, stepId, UserHttp, AccountHttp, userModuleHttp, $stateParams, ServiceIdService) {


	/*	----------------------------------------------------------------------------------------------------
	 *	getting step by step id */

	$scope.steps = Step.get({id: stepId});
	var step = $scope.steps;

	StepHttp.getStep(stepId).then(function (data) {
		
		$scope.sectionId = data.data.section.id;
	});


	var isCheckBox = false;
	var isRadio = false;
	var isFreeText = false;
	var isDropDown = false; 
	var isGroup = false;
	var userModelIdGlobal = false

	// My checks 


	// questions by setep
	$scope.questionsReturned = QuestionByStep.query({id: stepId});

	// question and answers - using http to use promise functions
	$scope.qanda = QuestionsHttp.getQuestionByStep(stepId);
	

	$scope.acountHttp = AccountHttp.getAccount();
	var accounthttp = $scope.acountHttp;

	function getUserModule() {
		accounthttp.then(function(data) {
			// var accountUserLogin = data.data.id
			getUserModuleById(data.data.id);
			// var userObject = UserHttp.getCurrentUser(accountUserLogin);
			//return userObject;

		});
		// .then(function(data) {

		// 	return data.data
		// }).then(function(data) {

		// 	getUserModuleById(data.id);
		// });

		function getUserModuleById(data) {
			var returnUserModuleById = userModuleHttp.userByUserId(data);
			returnUserModuleById.then(function(data) {

				return data.data;
			}).then(function(data) {
				
				var userModuleIdSeperateArray = [];
				var userModuleIdSeperate = userModuleIdSeperateArray;
				
				for(var prop in data) {

					var moduleID = $stateParams.sectionId;

					angular.forEach(data, function(value, key){
						
					
						var valueModuleId = value.module.id;
						var stateModuleId = parseInt($stateParams.sectionId);




						if (valueModuleId == stateModuleId) {
							
							userModuleIdSeperate.push({
								id: value.id	
							});

						};




					});


					angular.forEach(userModuleIdSeperate, function(value, key){

						var id = value.id;
						getUpdateUserProgress(id);
						$scope.testResponse = id;
					});

					

					var getSavedResponse = function(userModule) {
						
						
						$scope.savedResponse = userResponseHttp.getSavedResponse(userModule, stepId);
						var savedResponse = $scope.savedResponse;
						
						savedResponse.then( function (response) {
							var answerFound = response.data.answers;
							angular.forEach(answerFound, function(value, key){
								var answerObj = value;
								
								


								$scope.currentAnswer[answerObj.questionId] = answerObj.answerId;	
								if (answerObj.answerId === null) {
									$scope.currentAnswer[answerObj.questionId] = answerObj.answerText;		
								} 
								//else {
								// 	$scope.currentAnswer[answerObj.questionId] = answerObj.answerText;	
								// }
								
								
								// angular.forEach(answerObj, function(value, key){
								// 	
								// 	

								// 	if (key === 'answerId') {
								// 		

								// 		$scope.currentAnswer[0].usersAnswer = value;
								// 		// $scope.currentAnswer = {
								// 		// 	usersAnswer: value						
								// 		// }

								// 		
								// 	}
									
								// });
							});

						}, function (err) {
							
						});

					};
					getSavedResponse($scope.testResponse);

					return data[prop].id;


				}
			});
		}

		function getUpdateUserProgress(id) {

			
			var UserProgressReturned = UserProgressHttp.getProgress(id);


			UserProgressReturned.then(function(data) {
				userModelIdGlobal = data.data.userModuleId;
				userModelIdGlobal = userModelIdGlobal;
				
				//
			});
		}
	}
	getUserModule();



	
	// savedResponse.then(function (response) {
	// 	
	// });

	/*	----------------------------------------------------------------------------------------------------
	 *	Step navigation */

	// steps array for step navigation
	var stepsNavArray = [];
	$scope.stepsNavArray = stepsNavArray;


	// getting section by section id and pushing stepsNavArray to var
	var sectionId = ServiceIdService.setId;
	var stepsNavArrayBySectionId = StepHttp.getStepBySection(sectionId);
	
	stepsNavArrayBySectionId.then(function(data) {
		for(var prop in data.data) {
			stepsNavArray.push(data.data[prop].id);
		}
		//
		var lastArrayItem = stepsNavArray[stepsNavArray.length - 1];
		// 

		// 
		// 

		// 
		
		

		var currentPosition = $scope.stepsNavArray.indexOf(parseInt(stepId));
		var totalPercentage = $scope.stepsNavArray.length;
	
		// (Completed Task/Total Task ) * 100
		var calculateComplete = function(postion,total) {
			var result = (postion/total) * 100;
			
			$scope.percentageComplete = Math.round(result) + '%';
			
		}

		calculateComplete(currentPosition,totalPercentage);

		// if last
		if($stateParams.id == lastArrayItem) {
			//
			$scope.showMe = {
				'submit': false,
				'next': false, 
				'complete': true,
			};
		} else {
			//
			$scope.showMe = {
				'submit': false,
				'next': true, 
				'complete': false, 
			};
		}
		//
		//
		if($stateParams.id == stepsNavArray[0]) {
			//
			$scope.prevShow = false;
		} else {
			$scope.prevShow = true;
		}
	});  



	var lastIndex = function(data) {
		//
		//
	}
	lastIndex(stepsNavArray);
	
	// step navigation 
	function getPageName() {
		var currentStep = $stateParams.id;
		var currentStepId = parseInt(currentStep);
		return currentStepId;
	}

	


	// $scope.next = function() {	
	// 	
	// 	//
	// 	// if (!$scope.questionForm.$pristine) {
	// 		var currentStep = getPageName();	  
	// 		var idx = stepsNavArray.indexOf(currentStep);

	// 			if(idx != -1 && idx < (stepsNavArray.length-1)) {
	// 				var newId = stepsNavArray[idx+1];
	// 				$state.go('sectionStepDetail', { id : newId });
	// 			}	
	// 		} 
			// else {
			// 	
			// 	$scope.questionForm.submitted = true;
			// }
	

	// 
	// $scope.next = function() {	
	// 	//
	// 	//	
	// 		var currentStep = getPageName();	  
	// 		var idx = stepsNavArray.indexOf(currentStep);

	// 		if(idx != -1 && idx < (stepsNavArray.length-1)) {
	// 			var newId = stepsNavArray[idx+1];
	// 			$state.go('sectionStepDetail', { id : newId });
	// 		}

	// }
	


	

	
	/*	----------------------------------------------------------------------------------------------------
	 *	Progress tracking  */

	var createDate = new Date();
	var formatDate = createDate.getFullYear() + "-" + (createDate.getMonth() + 1) + "-" + createDate.getDate();

	// $scope.activityModule = {

	// 	updateProgress: function() {

	// 		// var getUserId = function() {

	// 		// 	/* Get account object */
	// 		// 	accounthttp.then(function(data) {
	// 		// 		var accountUserLogin = data.data.login

	// 		// 		/* get and return user resource */
	// 		// 		var userObject = UserHttp.getCurrentUser(accountUserLogin);
	// 		// 		return userObject;

	// 		// 	}).then(function(data) {
	// 		// 		/* get user obj */
	// 		// 		return data.data
	// 		// 	}).then(function(data) {
	// 		// 		/* call getUserById with user id */
	// 		// 		getUserModuleById(data.id);
	// 		// 	});
	// 		// };


	// 		// var getUserModuleById = function(data) {

	// 		// 	/* returned userModule */
	// 		// 	var returnUserModuleById = userModuleHttp.userByUserId(data);

	// 		// 	returnUserModuleById.then(function(data) {
	// 		// 		 userModule obj 
	// 		// 		return data.data;
	// 		// 	}).then(function(data) {
	// 		// 		/* return id from obj array */
	// 		// 		for(var prop in data) {
	// 		// 			getUpdateUserProgress(data[prop].id);
	// 		// 			return data[prop].id;
	// 		// 		}
	// 		// 	});
	// 		// };

	// 		var getUpdateUserProgress = function(id) {
	// 			var lastArrayItem = stepsNavArray[stepsNavArray.length - 1];
	// 			
	// 			/* get current userModule progress */
	// 			var UserProgressReturned = UserProgressHttp.getProgress(id);


	// 			UserProgressReturned.then(function(data) {

	// 				/* update */
	// 				function update() {

	// 					UserProgressHttp.updateProgress(
	// 					{
	// 						"userProgressId": data.data.userProgressId,
	// 						"userModuleId": data.data.userModuleId,
	// 						"stepId": stepId,
	// 						"startDate": "",
	// 						"endDate": ""
	// 					}
	// 					);
	// 				}
	// 				update();

	// 				if(lastArrayItem == $stateParams.id) {
	// 					
	// 					function end() {
	// 						UserProgressHttp.endProgress(
	// 						{
	// 							"userProgressId": data.data.userProgressId,
	// 							"userModuleId": data.data.userModuleId,
	// 							"stepId": stepId,
	// 							"startDate": "",
	// 							"endDate": formatDate
	// 						}
	// 						);
	// 					} 
	// 					return end();	
	// 				}



	// 			});
	// 		};

	// 		getUpdateUserProgress(userModelIdGlobal);

	// 	}

	// };


	/*	----------------------------------------------------------------------------------------------------
	 *	question and answer */

	 // getting question objects
	var qanda = $scope.qanda;

	// arrays to push new collections 
	var answerCollection = [];
	var correctAnswerCollection = [];
	var questionCollection = [];
	var groupQuestionCollection = [];
	var groupAnswerCollection = [];

	$scope.questionCollection = questionCollection;
	$scope.groupQuestionCollection = groupQuestionCollection;
	$scope.answerCollection = answerCollection;
	$scope.correctAnswerCollection = correctAnswerCollection;
	$scope.groupAnswerCollection = groupAnswerCollection;

	// getting and making new question collection
	qanda.then(function(data) {
		//

		
		

		
		isGroup = data.data[0].isGroup;

		

		var questionObj = data.data;


		angular.forEach(questionObj[0].questions, function(value){
			
			if( value.questionType === 'CHECKBOX' ) {
				
				$scope.isCheckBox = true;
				isCheckBox = true;
			} else if( value.questionType === 'RADIO' ) {
				
				$scope.isRadio = true;
				isRadio = true;
			} else if( value.questionType === "FREETEXT" ) {
				
				$scope.isFreeText = true;
				isFreeText = true
			} else if( value.questionType === 'DROPDOWN' ) {
				
				$scope.isDropDown = true;
				isDropDown = true;
			}

		});

		// single question
		if(!isGroup ) {
			
			$scope.isGroup = isGroup;
			

			// questions
			angular.forEach(questionObj, function(value){
				var question = value.questions[0];
				questionCollection.push(question);
			});

			// answer 
			angular.forEach(questionObj, function(value){
				var answerObj = value.questions[0].answers;
				
				angular.forEach(answerObj, function(value){
					answerCollection.push(value);

					if(value.correct === true) {
						correctAnswerCollection.push(answerObj);					
					}

					$scope.getTotal = function(){
						var total = 0;
						var totalArray = [];
						$scope.totalArray = totalArray;
						// var colAmount = [];
						// $scope.colAmount = colAmount;

						for(var i = 0; i < $scope.answerCollection.length; i++){
							var item = $scope.answerCollection[i];
							// total += item;
							totalArray.push(item);
						}
						//
						if($scope.totalArray.length == 4) {
							var colAmount = 3;
							$scope.colAmount = colAmount;
							//
							$scope.colValue = 3;
							return '3';
						} else if ($scope.totalArray.length == 5) {
							$scope.colValue = 5;
							return '5';
						}

						//
						//return $scope.colAmount;
					};
					
				});

			});

		// group question
		
		} else if( $scope.isFreeText ) {
			
			angular.forEach(questionObj, function(value) {
				
				$scope.groupQuestionTitle = value.groupText;
				//
			});

			// getting group questions question text
			angular.forEach(questionObj, function(value) {
				$scope.groupQuestionTitle = value.groupText;
			});

			angular.forEach(questionObj, function(value){
				var groupQuestions = value.questions;

				angular.forEach(groupQuestions, function(value){
					var groupQuestionObj = value;

					groupQuestionCollection.push(groupQuestionObj);
				}); 
			});

			// getting group answer text
			angular.forEach(questionObj, function(value){
				var groupAnswers = value.questions[0].answers;
				// 

				angular.forEach(groupAnswers, function(value){
					// 
					var groupAnswersObj = value;
					groupAnswerCollection.push(groupAnswersObj);
					
				}); 
			});

		}  else if( isGroup ) {

			$scope.isGroup = isGroup;

			// getting group questions question text
			angular.forEach(questionObj, function(value) {
				$scope.groupQuestionTitle = value.groupText;
			});

			angular.forEach(questionObj, function(value){
				var groupQuestions = value.questions;

				angular.forEach(groupQuestions, function(value){
					var groupQuestionObj = value;

					angular.forEach(groupQuestionObj, function(value, key){
						//
					});
					
					groupQuestionCollection.push(groupQuestionObj);
				}); 
			});

			// getting group answer text
			angular.forEach(questionObj, function(value){
				var groupAnswers = value.questions[0].answers;
				// 

				angular.forEach(groupAnswers, function(value){
					// 
					var groupAnswersObj = value;
					groupAnswerCollection.push(groupAnswersObj);
					
					$scope.sortName = 'answerId';
				});
			});
		}



	});
	

	/*	----------------------------------------------------------------------------------------------------
	 *	submit answer */

	//$scope.currentAnswer = {};
	$scope.answer = {};

	$scope.answerBtn = {
		'result': 'rectangle-radio-btn'
	}

	// submitAnswer function

	$scope.submitted = false;


	$scope.formData = {};
	$scope.currentAnswer = {};
	
	$scope.showMe = false;




	$scope.prevPage = function() {
		// var prevPage = function() {
			var lastArrayItem = stepsNavArray[stepsNavArray.length - 1];
			
			var currentStep = getPageName();	  
			var idx = stepsNavArray.indexOf(currentStep);

				if(idx != -1) {
					var newId = stepsNavArray[idx-1];
					
					$state.go('sectionStepDetail', { sectionId : $scope.sectionId, id : newId });
				} 

	}

	$scope.submitAnswer = function(answer) {
		
		// answerId user is submitting
		$scope.currentAnswer = answer;


		/*	---------------------------------------------------------------------- * 
		 *	Saving the users response - this could just be chained on to the above *
		 *	---------------------------------------------------------------------  */
		var getUpdateUserProgress = function(id) {

			// Last item in array
			var lastArrayItem = stepsNavArray[stepsNavArray.length - 1];

			// Get users progress using there id
			var UserProgressReturned = UserProgressHttp.getProgress(id);
			UserProgressReturned.then(function(data) {

				// Save progress
				function update() {
					UserProgressHttp.updateProgress(
						{
							"userProgressId": data.data.userProgressId,
							"userModuleId": data.data.userModuleId,
							"stepId": stepId,
							"startDate": "",
							"endDate": ""
						}
					);
				}
				update();
			});
			// End userProgress update
		};
		// End getUpdateUserProgress


		/*	---------------------------------------------------------------------- * 
		 *	Next page function 													   *
		 *	---------------------------------------------------------------------  */
		var nextPage = function() {
		var currentStep = getPageName();	  
		var idx = stepsNavArray.indexOf(currentStep);

			if(idx != -1 && idx < (stepsNavArray.length-1)) {
				var newId = stepsNavArray[idx+1];
				$state.go('sectionStepDetail', { sectionId : $scope.sectionId, id : newId });
			} else if (idx != -1 && idx >= (stepsNavArray.length-1)) {
				$state.go('confirm', { id : $scope.sectionId });
			}
		};
			

		/*	---------------------------------------------------------------------- * 
		 *	Pushing answer to array and saving that response 					   *
		 *	---------------------------------------------------------------------  */			
		
		// If the answer is valid
		if($scope.answers.$valid) {

			$scope.showMe = false;
			
			// Variables			
			var answerData = $scope.currentAnswer;

			var answerArray = [];
			$scope.answerArray = answerArray;

			// Record user repsonse for radio question that is a singular question
			if(isRadio && !isGroup) {

				

				angular.forEach(answerData, function(value, key){
					
					var usersAnswerArray = [];
					
					// Creating object to store answerId
					// key
					var key = "answer";
					// Obj
					var usersAnswerArrayObj = {};
					usersAnswerArrayObj[key] = value;
					usersAnswerArray.push(usersAnswerArrayObj);

					angular.forEach(usersAnswerArray, function(value, key){

						var getAnswerById = answerHttp.getAnswerById(value.answer);
						getAnswerById.then(function (response) {
							
							var answerObject = response.data;

							answerArray.push(
								{
									"questionId": answerObject.question.id,
									"answerId": answerObject.id,
									"answerText": answerObject.text,
									"correct": false	
								}
							);

							userResponseHttp.saveResponse(
								{
									"userModuleId": userModelIdGlobal,
									"stepId": stepId,
									"answers": answerArray
								}
							);


						});
					});
					
				});

			getUpdateUserProgress(userModelIdGlobal);
			nextPage();

		// If radio and a group question
		} else if (isRadio && isGroup) {

			// answerData - This returns a object containing a key and value for each answer selected
			angular.forEach(answerData, function(value, key){
				
				var usersAnswerArray = [];

				// Creating object and key to store answerId
				var key = "answer";				
				var usersAnswerArrayObj = {};

				// Storing the value to a new key in the obj for each item in answerData
				usersAnswerArrayObj[key] = value;
								
				var groupAnswerArray = [];

				// Taking usersAnswerArrayObj, finding the answerObj and saving the response
				angular.forEach(usersAnswerArrayObj, function(value, key){
										
					var getAnswerById = answerHttp.getAnswerById(value);
					getAnswerById.then(function (response) {
						
						var answerObject = response.data;			
						groupAnswerArray.push(
							{
								"questionId": answerObject.question.id,
								"answerId": answerObject.id,
								"answerText": answerObject.text,
								"correct": false
							}
						);

					}).then(function (data) {
						// Saving the users progress
						userResponseHttp.saveResponse(
							{
								"userModuleId": userModelIdGlobal,
								"stepId": stepId,
								"answers": groupAnswerArray
							}
						);	
					});
				});
			});	

			
			getUpdateUserProgress(userModelIdGlobal);
			nextPage();



			} else if (isFreeText) {

				
				angular.forEach(answerData, function(value, key){

					answerArray.push(
						{
							"questionId": key,
							"answerId": "",
							"answerText": value,
							"correct": false
						}
					);
				});	

				userResponseHttp.saveResponse(
					{
						"userModuleId": userModelIdGlobal,
						"stepId": stepId,
						"answers": answerArray
					}
				);	

				getUpdateUserProgress(userModelIdGlobal);
				nextPage();
			}; 


			/* FOR ANSWER FEEDBACK */
			$scope.disableInput = true;

			// setting the users answer
			var usersResult = answer.correct;
			var alert = answer.alert;

			if(usersResult) {
				$scope.answerBtn = {
					'result': 'rectangle-radio-btn'
				}
				$scope.answer = { 
					'answer': 'Correct',
					'show': false
				}
				$scope.alert = { 
					'alert': 'alerts alerts-success',
					'info': 'alerts alerts-info' 
				}
		} else {



			$scope.answerBtn = {
				'result': 'rectangle-radio-btn-incorrect'
			}
			$scope.answer = { 
				'answer': 'Incorrect',
				'show': false 
			}
			$scope.alert = { 
				'alert': 'alerts alerts-danger',
				'info': 'alerts alerts-info'
			}
		}
		} else {
			$scope.testScope = true;
			$scope.answers.submitted = true;
		}
		

	}

	
}]).directive('subScript', function () {
	return {
		restrict: 'A',
		require: 'ngModel',
		replace: true,
		scope: {
			props: '=subScript',
			ngModel: '=ngModel'
		},
		link: function compile(scope, element, attrs, controller) {
			scope.$watch('ngModel', function (value) {

				if (value == 'CHADS2') {
					var html = '<strong>CHADS<sub>2</sub></strong>';
					element.html(html);
				};
				if (value == 'CHA2DS2VASc') {
					var html = '<strong>CHA<sub>2</sub>DS<sub>2</sub>VASc</strong>';
					element.html(html);
				};
			});
		}
	};	
});













