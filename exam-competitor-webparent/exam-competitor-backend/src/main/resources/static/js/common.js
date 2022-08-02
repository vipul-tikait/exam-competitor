
$(document).ready(function(){
		 $("#logoutLink").on("click",function(e){
			 e.preventDefault();
			 document.logoutForm.submit();
		 });
		 
		 $("#fileImage").change(function(){
			 
			 fileSize = this.files[0].size;
			 if(fileSize > 1048576){
				 this.setCustomValidity("You must choose image of size less than 1MB.");
				 this.reportValidity();
			 }else{
				 showImageThumbnail(this);	 
			 }
		 });
		 
		  $(".link-user-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 userId = link.attr("userId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete user with ID : "+userId);
			 $("#confirmModal").modal("show");
		 });
		 $(".link-category-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 entityId = link.attr("categoryId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete entity with ID : "+entityId);
			 $("#confirmModal").modal("show");
		 });
		 $(".link-brand-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 entityId = link.attr("brandId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete entity with ID : "+entityId);
			 $("#confirmModal").modal();
		 });
		$(".link-product-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 entityId = link.attr("productId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete entity with ID : "+entityId);
			 $("#confirmModal").modal("show");
		 });
		$(".link-customer-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 entityId = link.attr("customerId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete entity with ID : "+entityId);
			 $("#confirmModal").modal("show");
		 });
		 $(".link-course-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 entityId = link.attr("courseId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete entity with ID : "+entityId);
			 $("#confirmModal").modal("show");
		 });
		 $(".link-mainExamType-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 entityId = link.attr("mainExamTypeId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete entity with ID : "+entityId);
			 $("#confirmModal").modal("show");
		 });
		 
		$(".link-mainExam-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 entityId = link.attr("mainExamId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete entity with ID : "+entityId);
			 $("#confirmModal").modal("show");
		 });
		$(".link-exam-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 entityId = link.attr("examId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete entity with ID : "+entityId);
			 $("#confirmModal").modal("show");
		 });
		$(".link-exam-level-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 entityId = link.attr("examLevelId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete entity with ID : "+entityId);
			 $("#confirmModal").modal("show");
		 });
		$(".link-question-set-delete").on("click",function(e){
			 e.preventDefault();
			 link = $(this);
			 entityId = link.attr("questionSetId");
			 $("#yesButton").attr("href", link.attr("href"));
			 $("#confirmText").text("Are you sure to delete entity with ID : "+entityId);
			 $("#confirmModal").modal("show");
		 });
		 
		 
		 customizeDropDownMenu();
		 customizeTabs();
	
		
		
		

	});
	
	function customizeDropDownMenu(){
		
		$(".navbar .dropdown").hover(
			function(){
				$(this).find('.dropdown-menu').first().stop(true,true).delay(250).slideDown();
			},
			function(){
				$(this).find('.dropdown-menu').first().stop(true,true).delay(100).slideUp();
			}
			
		)
		
		$(".dropdown > a").click(function(){
			location.href = this.href;
		})
	}
	
	 function showImageThumbnail(fileInput){
		 
		 var file = fileInput.files[0];
		 var reader = new FileReader();
		 reader.onload = function(e){
			 $("#thumbnail").attr("src", e.target.result);
		 };
		 reader.readAsDataURL(file);
		 
	 }
	 
	 function showDialogBoxforEmail(title, message){
		 
		 $("#modalTitle").text(title);
		 $("#modalBody").text(message);
		 $("#modalDialog").modal();
	 }
	 
	 function checkPasswordMatch(confirmedPassword){
		if(confirmedPassword.value != $("#password").val()){
			confirmedPassword.setCustomValidity("Password do not mached !");
		}else{
			confirmedPassword.setCustomValidity("");
		}
	 }
	 
function customizeTabs() {
	// Javascript to enable link to tab
	var url = document.location.toString();
	if (url.match('#')) {
	    $('.nav-tabs a[href="#' + url.split('#')[1] + '"]').tab('show');
	} 

	// Change hash for page-reload
	$('.nav-tabs a').on('shown.bs.tab', function (e) {
	    window.location.hash = e.target.hash;
	})	
}	 
	 