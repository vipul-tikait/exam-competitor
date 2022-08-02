/*$("#buttonCancel").on("click", function() {
	url = contextPath + "users";
	window.location = url;
});

*/

function checkEmailUnique(form) {

	url = contextPath + "users/check_email";

//	url = "[[@{/users/check_email}]]";
	userEmail = $("#email").val();
	userId = $("#id").val();
	csrf = $("input[name='_csrf']").val();
	params = { email: userEmail, _csrf: csrf, id: userId };

	$.post(url, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "Duplicated") {
			showDialogBoxforEmail("Warning", "There is anouther user having email " + userEmail);
		} else {
			showDialogBoxforEmail("Error", "There is Unknown response from server");
		}

	}).fail(function() {
		showDialogBoxforEmail("Error", "Could not connect to server");
	})

	return false;
}

