dropdownBrands = $("#brand");
dropdownCategories = $("#category");

$(document).ready(function() {
    $("#shortDescription").richText();
    $("#fullDescription").richText();

    dropdownBrands.change(function() {
        dropdownCategories.empty();
        getCategories();
    });

    getCategories();


});


function getCategories() {
    const brandId = dropdownBrands.val();
    const url = brandModuleURL + "/" + brandId + "/categories";

    $.get(url, function(responseJson) {
        // console.log(responseJson);
        $.each(responseJson, function(index, category) {
            $("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
        });
    });
}
function checkUnique(form) {
    const productId = $("#id").val();
    const productName = $("#name").val();

    const csrfValue = $("input[name='_csrf']").val();



    const params = {id: productId, name: productName, _csrf: csrfValue};

    $.post(checkUniqueUrl, params, function(response) {
        if (response === "OK") {
            form.submit();
        } else if (response === "Duplicate") {
            showModalDialog( "Warning ","There is another product having the name " + productName);
        } else {
            showModalDialog( "Error","Unknown response from server");
        }

    }).fail(function() {
        showModalDialog("Error","Could not connect to the server");
    });

    return false;
}