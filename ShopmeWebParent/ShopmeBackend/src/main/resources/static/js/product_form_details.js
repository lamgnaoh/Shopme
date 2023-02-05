function addNextDetailSection() {
	let allDivDetails = $("[id^='divDetail']");
	let divDetailsCount = allDivDetails.length;

	let htmlDetailSection = `
		<div class="d-flex flex-row align-items-center flex-wrap" id="divDetail${divDetailsCount}">
			<label class="m-3">Name:</label>
			<input type="text" class="form-control w-25" name="detailNames" maxlength="255" />
			<label class="m-3">Value:</label>
			<input type="text" class="form-control w-25" name="detailValues" maxlength="255" />
		</div>	
	`;
	
	$("#divProductDetails").append(htmlDetailSection);

	let previousDivDetailSection = allDivDetails.last();
	let previousDivDetailId = previousDivDetailSection.attr("id");

	let htmlLinkRemove = `
		<btn class="btn fas fa-times-circle fa-2x icon-gray"
			onclick = "removeDetailSectionById('${previousDivDetailId}')"
			title="Remove this detail"></btn>
	`;
	
	previousDivDetailSection.append(htmlLinkRemove);
	
	$("input[name='detailNames']").last().focus();
}

function removeDetailSectionById(id) {
	$("#" + id).remove();
}