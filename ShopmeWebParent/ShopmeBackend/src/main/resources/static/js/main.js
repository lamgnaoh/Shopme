$(document).ready(function () {
    $("#logoutLink").on("click" , function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    })
})
if($("#messageBlock")){
    setTimeout(() =>{
        const el = $("#messageBlock");
        el.css("display", "none");
    },3000);
}