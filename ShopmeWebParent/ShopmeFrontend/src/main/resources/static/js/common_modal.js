function showModalDialog(title , message){
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    // khoi tao 1 modal dialog
    let modal = new bootstrap.Modal($("#modalDialog") , {});
    modal.show();
}

function showErrorModal(message) {
    showModalDialog("Error", message);
}

function showWarningModal(message) {
    showModalDialog("Warning", message);
}