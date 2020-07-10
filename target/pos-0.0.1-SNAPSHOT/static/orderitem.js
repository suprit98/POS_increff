var orderitemList = [];

function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/order";
}

function getOrderItemUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/order_item";
}

function getAllOrdersUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/all_orders";
}

function getInvoiceUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/invoice";
}

function addOrderItemToList(event) {
	var $form = $("#orderitem-form");
	var json = toJson($form);
	orderitemList.push(JSON.parse(json));
	console.log(orderitemList);
	getOrderItemList();

}

function getOrderItemList() {
	displayOrderItemListFrontend(orderitemList);
}

function addOrder(event){
	var $form = $("#orderitem-add-form");
	var json = toJson($form);
	var order_id = $("#orderitem-add-form input[name=order_id]").val();
	var url = getOrderItemUrl() + "/" + order_id;

	ajaxQuery(url,'POST',json,getPreviousOrders);

	return false;
}

function addOrderItem(event) {
	var json = JSON.stringify(orderitemList);
	var url = getOrderUrl();

	ajaxQuery(url,'POST',json,displayDownloadPdfButton);

	return false;
}

function updateOrder(event){
	$('#edit-orderitem-modal').modal('toggle');
	//Get the ID
	var id = $("#orderitem-edit-form input[name=id]").val();
	var url = getOrderUrl() + "/" + id;


	var $form = $("#orderitem-edit-form");
	var json = toJson($form);

	ajaxQuery(url,'PUT',json,getPreviousOrders);
	return false;

}

function deleteOrderItemFromOrderList(id) {
	var url = getOrderUrl() + "/" + id;
	ajaxQuery(url,'DELETE','',getPreviousOrders);
}

function deleteOrderItem(id) {
	orderitemList.splice(id,1);
	getOrderItemList();
}

function getPreviousOrders() {
	var url = getOrderUrl();
	ajaxQuery(url,'GET','',displayOrderItemList);
}

function getOrderList() {
	var url = getAllOrdersUrl();
	ajaxQuery(url,'GET','',displayOrdersList);
}

function getOrderItemsHtml(id) {
	var orderitemsHtml = "";
	var url = getAllOrdersUrl() + "/" + id;
	$.ajax({
		 url: url,
		 type: 'GET',
		 headers: {
				'Content-Type': 'application/json'
			 },
		 success: function(response) {
				orderitemsHtml = createOrderItemsHtml(response,id);
		 },
		 error: function(response){
				handleAjaxError(response);
		 }
	});
	return orderitemsHtml;
}


//UI DISPLAY METHODS

function displayOrderItemListFrontend(data){
	console.log('Printing Order items');
	var $tbody = $('#orderitem-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="deleteOrderItem(' + i + ')">delete</button>'
		var row = '<tr>'
		+ '<td>' + (i) + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayOrderItemList(data){
	console.log('Printing Order data');

	var $tbody = $('#order-table').find('tbody');
	$tbody.empty();

	var prev=0;
	var thHtml = '<tr>';
	thHtml += '<th scope="col">ID</th>';
	thHtml += '<th scope="col">Barcode</th>';
	thHtml += '<th scope="col">Quantity</th>';
	thHtml += '<th scope="col">Order Id</th>';
	thHtml += '<th scope="col">Actions</th>';
	thHtml += '</tr>';
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="deleteOrderItemFromOrderList(' + e.id + ')">delete</button>';
		buttonHtml += '<button onclick="displayEditOrderItem(' + e.id + ')">edit</button>';
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>'  + e.orderId + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';


		if(parseInt(prev) != parseInt(e.orderId) && parseInt(prev)!=0){
			$tbody.append('<tr><td colspan="3"><button onclick="downloadPDF('+prev +')">Download Invoice PDF</button></td><td colspan="2"><button onclick="displayAddOrderItemModal(' + prev + ')">Add Order Item</button></td></tr>');
			$tbody.append(thHtml);
		}
		$tbody.append(row);

    prev = parseInt(e.orderId);
	}
	$tbody.append('<tr><td colspan="3"><button onclick="downloadPDF('+prev +')">Download Invoice PDF</button></td><td colspan="2"><button onclick="displayAddOrderItemModal(' + prev + ')">Add Order Item</button></td></tr>');
}

function displayOrdersList(data) {
	console.log('Printing Orders');
	var $tbody = $('#order-table2').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="displayAddOrderItemModal(' + e.id + ')">Add  Order Item</button>';
		buttonHtml += '<button onclick="initializeDropdown(' + e.id + ')">Show OrderItems</button>';
		var row = '<tr class="order-header">'
		+ '<td>' + e.id + '</td>'
		+ '<td>'  + e.datetime + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		var orderitemsHtml = getOrderItemsHtml(e.id);
    $tbody.append(row);
		$tbody.append(ordersitemHtml);
	}
}

function displayEditOrderItem(id){
	var url = getOrderUrl() + "/" + id;
	ajaxQuery(url,'GET','',displayOrderItem);
}

function downloadPDF(id) {
	var url = getInvoiceUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
		 xhrFields: {
        responseType: 'blob'
     },
	   success: function(blob) {
				console.log(blob.size);
      	var link=document.createElement('a');
      	link.href=window.URL.createObjectURL(blob);
      	link.download="Invoice_" + new Date() + ".pdf";
      	link.click();
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});

}

function displayOrderItem(data){
	$("#orderitem-edit-form input[name=barcode]").val(data.barcode);
	$("#orderitem-edit-form input[name=quantity]").val(data.quantity);
	$("#orderitem-edit-form input[name=id]").val(data.id);
	$('#edit-orderitem-modal').modal('toggle');
}

function displayAddOrderItemModal(order_id) {
	$("#orderitem-add-form input[name=order_id]").val(order_id);
	$('#add-orderitem-modal').modal('toggle');
}

function displayDownloadPdfButton(response) {
	alert("Order created");
	$("#container").append('<button onclick="downloadPDF(' + response.id +')">Download Invoice PDF</button>');
}

function createOrderItemsHtml(data,id) {
	var orderitemsHtml = '<table class="orderitemrows' +id +'"><tbody></tbody></table>';
	var table = $(orderitemsHtml).find('tbody');
	var thHtml = '<tr>';
	thHtml += '<th scope="col">ID</th>';
	thHtml += '<th scope="col">Barcode</th>';
	thHtml += '<th scope="col">Quantity</th>';
	thHtml += '<th scope="col">Order Id</th>';
	thHtml += '<th scope="col">Actions</th>';
	thHtml += '</tr>';
	table.append(thHtml);
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="deleteOrderItemFromOrderList(' + e.id + ')">delete</button>';
		buttonHtml += '<button onclick="displayEditOrderItem(' + e.id + ')">edit</button>';
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>'  + e.orderId + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		$tbody.append(row);
	}
	$tbody.append('<tr><td colspan="3"><button onclick="downloadPDF('+id +')">Download Invoice PDF</button></td><td colspan="2"><button onclick="displayAddOrderItemModal(' + id + ')">Add Order Item</button></td></tr>');
	return orderItemsHtml;
}

function initializeDropdown(id) {
	console.log("Orderitems toggle");
	var orderitem_row = '.orderitemrows' + id;
  $(orderitem_row).toggle();
}


//INITIALIZATION CODE
function init(){
	$('#add-orderitem').click(addOrderItemToList);
	$('#refresh-data').click(getOrderItemList);
	$('#add-order').click(addOrderItem);
	$("#add-orderitem-previousorders").click(addOrder);
	$('#update-orderitem').click(updateOrder);
}

$(document).ready(init);
$(document).ready(getOrderItemList);
$(document).ready(getPreviousOrders);
$(document).ready(getOrderList);
