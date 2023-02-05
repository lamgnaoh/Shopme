let extraImagesCount = 0;
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

    $("input[name='extraImage']").each(function(index) {
        extraImagesCount++;

        $(this).change(function() {
            showExtraImageThumbnail(this, index);
        });
    });

});

function showExtraImageThumbnail(fileInput, index) {
    let file = fileInput.files[0];
    let reader = new FileReader();
    reader.onload = function(e) {
        $("#extraThumbnail" + index).attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
    // nếu index >= phần tử extra image section cuối cùng thi mới thêm section mới
    if (index >= extraImagesCount - 1) {
        addNextExtraImageSection(index + 1);
    }
}

function addNextExtraImageSection(index) {
    let htmlExtraImage = `
		<div class="col border m-3 p-2" id="divExtraImage${index}">
			<div id="extraImageHeader${index}">
			    <label>Extra Image #${index + 1}:</label>
			</div>
			<div class="m-2">
				<img id="extraThumbnail${index}" alt="Extra image #${index + 1} preview" class="img-fluid"
					src="${defaultImageThumbnailSrc}"/>
			</div>
			<div>
				<input type="file" name="extraImage"
					onchange="showExtraImageThumbnail(this, ${index})" 
					accept="image/png, image/jpeg" />
			</div>
		</div>	
	`;

    let htmlLinkRemove = `
		<btn class="btn fas fa-times-circle fa-2x icon-dark float-end"
			onclick="removeExtraImage(${index - 1})" 
			title="Remove this image"></btn>
	`;

    $("#divProductImages").append(htmlExtraImage);

    $("#extraImageHeader" + (index - 1)).append(htmlLinkRemove);

    extraImagesCount++;
}

function removeExtraImage(index) {

    $("#divExtraImage" + index).remove();
}


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