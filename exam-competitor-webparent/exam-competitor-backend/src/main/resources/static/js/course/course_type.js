
$(document).ready(function() {

	$(".link-curses-type-delete").on("click", function(e) {
		e.preventDefault();
		link = $(this);
		courseTypeId = link.attr("courseTypeId");
		$("#yesButton").attr("href", link.attr("href"));
		$("#confirmText").text("Are you sure to delete course type with ID : " + courseTypeId);
		$("#confirmModal").modal();
	});

	$("#buttonCancel").on("click", function() {
		url = contextPath + "couses/type";
		window.location = url;
	});
});
function checkCourseTypeUnique(form) {

	url = contextPath + "courses/check_courses_type";

	//	url = "[[@{/users/check_email}]]";
	coursesTypeName = $("#name").val();
	couseTypeId = $("#id").val();
	csrf = $("input[name='_csrf']").val();
	params = { name: coursesTypeName, _csrf: csrf, id: couseTypeId };

	$.post(url, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "Duplicated") {
			showDialogBoxforCorseType("Warning", "There is anouther curseType having name " + coursesTypeName);
		} else {
			showDialogBoxforCorseType("Error", "There is Unknown response from server");
		}

	}).fail(function() {
		showDialogBoxforCorseType("Error", "Could not connect to server");
	})

	return false;
}

