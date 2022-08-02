$(document).ready(function() {
	var defaultValue = '<option value=' + -1 + '>SELECT</option>';
	var qSetName = '';

	$("#mainExamTypeSelect").change(function() {

		var mainExamTypeId = $('#mainExamTypeSelect').find(":selected").val();//$(this).val();
		var csrf = $("input[name='_csrf']").val();
		var s = '<option value=' + -1 + '>SELECT</option>';
		//url = "getMainExams";
		url = "/exam-competitor/admin/getMainExams";

		params = { mainExamTypeId: mainExamTypeId };

		if (mainExamTypeId > 0) {
			$.get(url, params, function(result) {
				var mainExam = JSON.parse(result);
				for (var i = 0; i < mainExam.length; i++) {
					s += '<option value="' + mainExam[i].id + '">' + mainExam[i].name + '</option>';
				}
				$('#mainExamSelect').html(s);
				$('#examSelect').html(defaultValue);
				$('#examLevelSelect').html(defaultValue);
			}).fail(function() {
				showDialogBoxforExamDuplicate("Error", "Could not connect to server");
			})
			qSetName = ($('#mainExamTypeSelect').find(":selected").text().toUpperCase()
			).replaceAll(" ", "_");;
			$('#qName').val(qSetName);

		}
	});

	$("#mainExamSelect").change(function() {
		var mainExamId = $('#mainExamSelect').find(":selected").val();
		var csrf = $("input[name='_csrf']").val();
		var s = '<option value=' + -1 + '>SELECT</option>';
		url = "/exam-competitor/admin/getExams";

		params = { mainExamId: mainExamId };

		if (mainExamId > 0) {
			$.get(url, params, function(result) {
				var exam = JSON.parse(result);
				for (var i = 0; i < exam.length; i++) {
					s += '<option value="' + exam[i].id + '">' + exam[i].name + '</option>';
				}
				$('#examSelect').html(s);
				$('#examLevelSelect').html(defaultValue);
			}).fail(function() {
				showDialogBoxforExamDuplicate("Error", "Could not connect to server");
			})
			qSetName = ($('#mainExamTypeSelect').find(":selected").text().toUpperCase()
				+ "_" + $('#mainExamSelect').find(":selected").text().toUpperCase()
			).replaceAll(" ", "_");;
			$('#qName').val(qSetName);
		}
	});

	$("#examSelect").change(function() {
		var examId = $('#examSelect').find(":selected").val();
		var csrf = $("input[name='_csrf']").val();
		var s = '<option value=' + -1 + '>SELECT</option>';
		url = "/exam-competitor/admin/getExamLevels";

		params = { examId: examId };

		if (examId > 0) {
			$.get(url, params, function(result) {
				var el = JSON.parse(result);
				for (var i = 0; i < el.length; i++) {
					s += '<option value="' + el[i].id + '">' + el[i].name + '</option>';
				}
				$('#examLevelSelect').html(s);
			}).fail(function() {
				showDialogBoxforExamDuplicate("Error", "Could not connect to server");
			})
			qSetName = ($('#mainExamTypeSelect').find(":selected").text().toUpperCase()
				+ "_" + $('#mainExamSelect').find(":selected").text().toUpperCase()
				+ "_" + $('#examSelect').find(":selected").text().toUpperCase()
			).replaceAll(" ", "_");
			$('#qName').val(qSetName);
		}
	});


	$("#examLevelSelect").change(function() {
		var examLvlId = $('#examLevelSelect').find(":selected").val();


		if (examLvlId > 0) {
			qSetName = ($('#mainExamTypeSelect').find(":selected").text().toUpperCase()
				+ "_" + $('#mainExamSelect').find(":selected").text().toUpperCase()
				+ "_" + $('#examSelect').find(":selected").text().toUpperCase()
				+ "_" + $('#examLevelSelect').find(":selected").text().toUpperCase()
			).replaceAll(" ", "_");
			$('#qName').val(qSetName);
		}
	});

	selectTopicsForExamLevel = $("#selectTopicsForExamLevel");
	divChosenTopics = $("#chosenTopics");

	selectTopicsForExamLevel.change(function() {
		divChosenTopics.empty();
		showChosenTopics();
	});
	showChosenTopics();

	$(".link-questions-list-detail").on("click",
		function(e) {
			e.preventDefault();
			linkDetailURL = $(this).attr("href");
			$("#questionListModal").modal("show").find(".modal-content").load(linkDetailURL);
		});

	$("#buttonSaveQue").on("click", function() {
		$("#questionSetAssignQuetionsForm").submit();
	});

	$("#dropdownTopicAtAssignQue").on("change", function() {


		var topicId = $('#dropdownTopicAtAssignQue').find(":selected").val();
		var qSetId = $('#qSetId').val();
		var csrf = $("input[name='_csrf']").val();
		var s = '';
		url = "/exam-competitor/admin/questions/getQuestionByTopic";

		params = { topicId: topicId,qSetId:qSetId };

		//if (topicId > 0) {
			$.get(url, params, function(result) {
				var que = JSON.parse(result);
				$('#qList').html("");
				for (var i = 0; i < que.length; i++) {

					s += '<tr><td class="hideable-column">'
						+ '<input type="checkbox" class="m-2" name="questions" id="questions'+i+'" value="' + que[i].id + '" "/>'
						+ '&nbsp;&nbsp;' + que[i].id + '</td > '
						+ '<td></td>'
						+ '<td>' + que[i].name + '</td>'
						+ '<td>' + que[i].name + '</td>'
						+ '<td class="hideable-column">' + que[i].topic.name + '</td>'
						+ '<td></td>'
						+ '<td></td>';
						+ '</tr>';
					//s += '<option value="' + el[i].id + '">' + el[i].name + '</option>';
				}
				$('#qList').html(s);
			}).fail(function() {
				showDialogBoxforExamDuplicate("Error", "Could not connect to server");
			})

		//}
	});

	$("#dropdownTopicAtAddMoreQue").on("change", function() {
		alert('addMore');
		$("#addMoreQueForm").submit();
	});





});
function showChosenTopics() {
	selectTopicsForExamLevel.children("option:selected").each(function() {
		selectedTopic = $(this);
		catId = selectedTopic.val();
		catName = selectedTopic.text().replaceAll('--', "");

		divChosenTopics.append("<span class='badge badge-secondary m-1'>" + catName + "</span>");
	});
}


