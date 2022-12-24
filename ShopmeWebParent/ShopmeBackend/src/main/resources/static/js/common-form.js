$("#cancelButton").on("click", function (){
    window.location = moduleURL
})

// validate file size
$("#fileImage").change(function (){
    const file = this.files[0];
    const fileSize = this.files[0].size;
    if(fileSize > 1048576){
        this.setCustomValidity("You must choose an image less than 1 MB");
        this.reportValidity();
    } else {
        const reader = new FileReader();
        reader.onload = function (e) {
            $("#thumbnail").attr("src" , e.target.result);
        }
        reader.readAsDataURL(file);
    }
})

function showModalDialog(title , message){
    $("#modalTitle").text(title);
    $("#modalBody").text(message);
    // khoi tao 1 modal dialog
    var modal = new bootstrap.Modal($("#modalDialog") , {});
    modal.show();
}