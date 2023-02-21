let dropDownCountry;
let dataListState;
let fieldState;

$(document).ready(function() {
    dropDownCountry = $("#country");
    dataListState = $("#listStates");
    fieldState = $("#state");

    dropDownCountry.on("change", function() {
        loadStatesForCountry();
        fieldState.val("").focus();
    });
});

function loadStatesForCountry() {
    let selectedCountry = $("#country option:selected");
    let countryId = selectedCountry.val();
    let url = contextPath + "settings/list_states_by_country/" + countryId;

    $.get(url, function(responseJSON) {
        dataListState.empty();

        $.each(responseJSON, function(index, state) {
            $("<option>").val(state.name).text(state.name).appendTo(dataListState);
        });

    }).fail(function() {
        alert('failed to connect to the server!');
    });
}

function checkPasswordMatch(confirmPassword) {
    if (confirmPassword.value !== $("#password").val()) {
        confirmPassword.setCustomValidity("Passwords do not match!");
    } else {
        confirmPassword.setCustomValidity("");
    }
}



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