/*moduleURL = "[[@{/exams}]]";
	
	$(document).ready(function(){
		dropdownTopics = $("#topics");
		divChosenTopics= $("#chosenTopics");
		
		dropdownTopics.change(function(){
			divChosenTopics.empty();
			showChosenTopics();
		});
		showChosenTopics();
	});
	
	function showChosenTopics(){
		dropdownTopics.children("option:selected").each(function(){
			selectedTopic = $(this);
			topicId = selectedTopic.val();
			topicName = selectedTopic.text().replaceAll('--',"");
			
			divChosenTopics.append("<span class='badge badge-secondary m-1'>"+topicName+"</span>");
		});
	}
		
	
	$("#buttonCancel").on("click",function(){
		 window.location = "[[@{/exams}]]";
	 });
	
	function checkUnique(form){
		examId= $("#id").val();
		examName= $("#name").val();
		csrf = $("input[name='_csrf']").val();
		
		url = "[[@{/exams/check_unique}]]";
		 
		params = {id: examId, name:examName, _csrf:csrf};
		 
		$.post(url, params,function(response){
			 if(response == "OK"){
				form.submit();
			 }else if(response == "DuplicateName"){
				 showDialogBoxforExamDuplicate("Warning","There is anouther exam having same name "+examName);
			 }else {
				 showDialogBoxforExamDuplicate("Error","There is Unknown response from server");
			 }
			 
		 }).fail(function(){
			 showDialogBoxforExamDuplicate("Error","Could not connect to server");
		 })
		 
		 return false;
	
	}
	
	function showDialogBoxforExamDuplicate(title, message){
		 
		 $("#modalTitle").text(title);
		 $("#modalBody").text(message);
		 $("#modalDialog").modal();
	 }*/