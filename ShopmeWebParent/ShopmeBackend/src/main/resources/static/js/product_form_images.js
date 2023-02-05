let extraImagesCount = 0;

$(document).ready(function() {

    $("input[name='extraImage']").each(function(index) {
        extraImagesCount++;

        $(this).change(function() {
            if(!checkFileSize(this)){
                return ;
            }
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
		<btn class="btn fas fa-times-circle fa-2x icon-gray float-end"
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
