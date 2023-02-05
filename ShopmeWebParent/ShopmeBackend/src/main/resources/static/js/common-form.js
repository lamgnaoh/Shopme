$("#cancelButton").on("click", function (){
    window.location = moduleURL
})

// validate file size
$("#fileImage").change(function (){

    if(!checkFileSize(this)){
    } else {
        showImageThumbnail(this);
    }
})

function showImageThumbnail(fileInput) {
    const  file = fileInput.files[0];
    const  reader = new FileReader();
    reader.onload = function(e) {
        $("#thumbnail").attr("src", e.target.result);
    };

    reader.readAsDataURL(file);
}


function showModalDialog(title , message){
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    // khoi tao 1 modal dialog
    var modal = new bootstrap.Modal($("#modalDialog") , {});
    modal.show();
}

function checkFileSize(fileInput) {
    const fileSize = fileInput.files[0].size;

    if (fileSize > 1048576) {
        fileInput.setCustomValidity("You must choose an image less than 1 MB bytes!");
        fileInput.reportValidity();

        return false;
    } else {
        fileInput.setCustomValidity("");

        return true;
    }
}