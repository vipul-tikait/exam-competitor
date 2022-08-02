$(document).ready(function() {
	$("a[name='linkRemoveCourseDetail']").each(function(index) {
		$(this).click(function() {
			removeCourseDetailSectionByIndex(index);
		});
	});

});

function addNextCourseDetailSection() {
	allDivCourseDetails = $("[id^='divCourseDetail']");
	divCourseDetailsCount = allDivCourseDetails.length;

	htmlCourseDetailSection = `
		<div id="divCourseDetail${divCourseDetailsCount}" class="row">
			<div class="col-sm-7">
				<input type="hidden" name="detailIDs" value="${divCourseDetailsCount}" />
				<input type="text" class="form-control" name="detailNames" maxlength="255" />&nbsp; 
			</div>
			<div class="col-sm-3">
					<select class="form-control"  name="detailValues"  >
					  <option value="Y">Yes</option>
					  <option value="N">No</option>
					</select>
			</div>
		</div>
	`;

	

	$("#divCourseDetails").append(htmlCourseDetailSection);
	previousDivCourseDetailSection = allDivCourseDetails.last();
	previousDivCourseDetailID = previousDivCourseDetailSection.attr("id");

	htmlLinkRemove = `
		<a class="btn fas fa-times-circle fa-2x icon-dark" style='color: red'
			href="javascript:removeCourseDetailSectionById('${previousDivCourseDetailID}')"
			title="Remove this Course detail"></a>
	`;

	previousDivCourseDetailSection.append(htmlLinkRemove);

	$("input[name='detailNames']").last().focus();
}

function removeCourseDetailSectionById(id) {
	$("#" + id).remove();
}

function removeCourseDetailSectionByIndex(index) {
	$("#divCourseDetail" + index).remove();
}