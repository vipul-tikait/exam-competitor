

function checkModeratorSelected(id) {
		
		if (moderatorId != -1) {
			url = contextPath + "attempts/assign/moderator";
			var moderatorId = $('#selectModarator'+id+' option:selected').val();
			var attemptId = $('#attempt'+id).val();
			
			var csrfValue = $("input[name='_csrf'").val();
			
			params = {attemptId : attemptId, moderatorId: moderatorId, _csrf: csrfValue};
			
			$.post(url, params, function(response) {
				if (response == "OK") {
					window.location = contextPath+"attempts";
				} else {
					showErrorModal("Unknown response from server");
				}			
			}).fail(function() {
				showErrorModal("Could not connect to the server");	
			});
				
		} else {
			showErrorModal("No modarator selected, pls select then assign " );
			
		}

		return false;
	}


function showWarningModal(message) {
	showModalDialog("Warning", message);
}

async function uploadEvaluatedPdf(id) {
  let formData = new FormData(); 
  formData.append("evaluatedPdf", fileupload.files[0]);
  formData.append("_csrf",$("input[name='_csrf'").val());
  formData.append("attemptId", $('#attempt'+id).val());
  let response = await fetch(contextPath + 'attempts/upload/evaluatedPdf', {
    method: "POST", 
    body: formData
  }); 

  if (response.status == 200) {
    window.location = contextPath+"attempts";
  }
}

