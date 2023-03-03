$(document).ready(function() {
	$(".linkMinus").on("click", function(evt) {
		evt.preventDefault();
		decreaseQuantity($(this));
	});
	
	$(".linkPlus").on("click", function(evt) {
		evt.preventDefault();
		increaseQuantity($(this));
	});	
});

function decreaseQuantity(link) {
	productId = link.attr("pid");
	let quantityInput = $("#quantity" + productId);
	let newQuantity = parseInt(quantityInput.val()) - 1;
	
	if (newQuantity > 0) {
		quantityInput.val(newQuantity);
		updateQuantity(productId, newQuantity);
	} else {
		showWarningModal('Minimum quantity is 1');
	}	
}

function increaseQuantity(link) {
		productId = link.attr("pid");
		let quantityInput = $("#quantity" + productId);
		let newQuantity = parseInt(quantityInput.val()) + 1;
		
		if (newQuantity <= 5) {
			quantityInput.val(newQuantity);
			updateQuantity(productId, newQuantity);
		} else {
			showWarningModal('Maximum quantity is 5');
		}	
}

function updateQuantity(productId, quantity) {
	let url = contextPath + "cart/update/" + productId + "/" + quantity;
	
	$.ajax({
		type: "POST",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(updatedSubtotal) {
		updateSubtotal(updatedSubtotal, productId);
		updateTotal();
	}).fail(function() {
		showErrorModal("Error while updating product quantity.");
	});	
}

function updateSubtotal(updatedSubtotal, productId) { // update subtotal per product
	let formattedSubtotal = $.number(updatedSubtotal, 2);
	$("#subtotal" + productId).text(formattedSubtotal);
}

function updateTotal() { // update estimated total
	let total = 0.0;
	
	$(".subtotal").each(function(index, element) {
		total += parseFloat(element.innerHTML.replaceAll(",", ""));
	});
	
	let formattedTotal = $.number(total, 2);
	$("#total").text(formattedTotal);
}