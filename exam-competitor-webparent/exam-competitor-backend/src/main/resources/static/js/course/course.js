$('#course_start_datepicker').datepicker({
	weekStart: 1,
	daysOfWeekHighlighted: "6,0",
	autoclose: true,
	todayHighlight: true,
	dateFormat: "dd-mm-yyyy", 
    timeFormat: "HH:mm:ss"
});
$('#course_start_datepicker').datepicker("startDate", new Date());

$('#course_end_datepicker').datepicker({
	weekStart: 1,
	daysOfWeekHighlighted: "6,0",
	autoclose: true,
	todayHighlight: true,
	dateFormat: "dd-mm-yyyy", 
    timeFormat: "HH:mm:ss"
});
$('#course_end_datepicker').datepicker("endDate", new Date());
$(document).ready(function(){
	$(".link-detail").on("click", function(e) {
			e.preventDefault();
			linkDetailURL = $(this).attr("href");
			$("#detailModal").modal("show").find(".modal-content").load(linkDetailURL);
		});
});
