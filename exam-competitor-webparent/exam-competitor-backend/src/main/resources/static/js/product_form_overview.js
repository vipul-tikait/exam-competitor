dropdownBrands = $("#brand");
dropdownCategories = $("#category");
divChosenCategories = $("#chosenCategories");

$(document).ready(function() {
	$("#shortDescription").richText();
	$("#fullDescription").richText();
	
	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});	
	
	getCategoriesForNewForm();

});

function getCategoriesForNewForm() {
	catIdField = $("#categoryId");
	editMode = false;
	
	if (catIdField.length) {
		editMode = true;
	}
	
	if (!editMode) getCategories();
}

function getCategories() {
	brandId = dropdownBrands.val(); 
	url = brandModuleURL + "/" + brandId + "/categories";
	
	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});			
	});
}
/*$("#buttonCancel").on("click", function() {
	window.location = "[[@{/products}]]";
});*/

function checkUnique(form) {
	productId = $("#id").val();
	productName = $("#name").val();
	csrf = $("input[name='_csrf']").val();
	params = { id: productId, name: productName, _csrf: csrf };

	$.post(checkUniqueUrl, params, function(response) {
		if (response == "OK") {
			form.submit();
		} else if (response == "DuplicateName") {
			showDialogBoxforBrandDuplicate("Warning", "There is anouther product having same name " + productName);
		} else {
			showDialogBoxforBrandDuplicate("Error", "There is Unknown response from server");
		}

	}).fail(function() {
		showDialogBoxforBrandDuplicate("Error", "Could not connect to server");
	})

	return false;

}

function showDialogBoxforBrandDuplicate(title, message) {

	$("#modalTitle").text(title);
	$("#modalBody").text(message);
	$("#modalDialog").modal();
}
