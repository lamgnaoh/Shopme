$(document).ready(function() {
	$(".linkMinus").on("click", function(evt) {
		evt.preventDefault();
		decreaseQuantity($(this));
	});
	
	$(".linkPlus").on("click", function(evt) {
		evt.preventDefault();
		increaseQuantity($(this));
	});

	$(".linkRemove").on("click", function(evt) {
		evt.preventDefault();
		removeProduct($(this));
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
	let productCount = 0;
	$(".subtotal").each(function(index, element) {
		productCount++;
		total += parseFloat(element.innerHTML.replaceAll(",", ""));
	});

	if (productCount < 1) {
		showEmptyShoppingCart();
	} else {
		let formattedTotal = $.number(total, 2);
		$("#total").text(formattedTotal);
	}
}

function showEmptyShoppingCart() {
	$("#sectionTotal").hide();
	$("#sectionEmptyCartMessage").removeClass("d-none");
}


function removeProduct(link) {
	let url = link.attr("href");

	$.ajax({
		type: "DELETE",
		url: url,
		beforeSend: function(xhr) {
			xhr.setRequestHeader(csrfHeaderName, csrfValue);
		}
	}).done(function(response) {
		let rowNumber = link.attr("rowNumber");
		removeProductHTML(rowNumber);
		updateTotal();
		updateCountNumbers();

	}).fail(function() {
		showErrorModal("Error while removing product.");
	});
}

function removeProductHTML(rowNumber) {
	$("#row" + rowNumber).remove();
	$("#blankLine" + rowNumber).remove();
}

function updateCountNumbers() {
	$(".divCount").each(function(index, element) {
		element.innerHTML = "" + (index + 1);
	});
}
