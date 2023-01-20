function clearFilter() {
	window.location = moduleURL;	
}

function showDeleteConfirmModal(link, entityName) {
	entityId = link.attr("entityId");
	$("#confirmText").text("Are you sure you want to delete this "
							 + entityName + " ID " + entityId + "?");
	// set href yes button to delete user url
	$("#confirmButton").attr("href" , link.attr("href"));

	var modal = new bootstrap.Modal($("#confirmDialog") , {});
	modal.show();
}