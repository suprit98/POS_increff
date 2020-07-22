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
	var check = validateOrderItem(json);
	if(check) {
		orderitemList.push(JSON.parse(json));
	}

	console.log(orderitemList);
	getOrderItemList();

}

function getOrderItemList() {
	displayOrderItemListFrontend(orderitemList);
}

function addOrderItem(event){
	$("#add-orderitem-modal").modal('toggle');
	var $form = $("#orderitem-add-form");
	var json = toJson($form);
	var order_id = $("#orderitem-add-form input[name=order_id]").val();
	var url = getOrderItemUrl() + "/" + order_id;
	var check = validateOrderItem(json);

	if(check) {
		ajaxQuery(url,'POST',json,getOrderList);
	}


	return false;
}

function addOrder(event) {
	$("#add-order-modal").modal('toggle');
	var json = JSON.stringify(orderitemList);
	var url = getOrderUrl();

	ajaxQuery(url,'POST',json,getOrderList);

	return false;
}

function updateOrder(event){
	$('#edit-orderitem-modal').modal('toggle');
	//Get the ID
	var id = $("#orderitem-edit-form input[name=id]").val();
	var url = getOrderItemUrl() + "/" + id;


	var $form = $("#orderitem-edit-form");
	var json = toJson($form);

	var check = validateOrderItem(json);
	if(check){
		ajaxQuery(url,'PUT',json,getOrderList);
	}

	return false;

}

function deleteOrderItemFromOrderList(id) {
	var url = getOrderItemUrl() + "/" + id;
	ajaxQuery(url,'DELETE','',getOrderList);
}

function deleteOrder(id) {
	var url = getOrderUrl() + "/" + id;
	ajaxQuery(url,'DELETE','',getOrderList);
}

function deleteOrderItem(id) {
	orderitemList.splice(id,1);
	getOrderItemList();
}


function getOrderList() {
	var url = getOrderUrl();
	ajaxQuery(url,'GET','',displayOrdersList);
}

function getOrderItemsHtml(id) {
	var url = getOrderUrl() + "/" + id;
	$.ajax({
		 url: url,
		 type: 'GET',
		 headers: {
				'Content-Type': 'application/json'
			 },
		 success: function(response) {
				createOrderItemsHtml(response,id);
		 },
		 error: function(response){
				handleAjaxError(response);
		 }
	});
}


//UI DISPLAY METHODS

function displayOrderItemListFrontend(data){
	console.log('Printing Order items');
	var $tbody = $('#orderitem-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button style="padding: 0;border: none;background: none;" onclick="deleteOrderItem(' + i + ')"><span class="material-icons" style="color:red">delete</span></button>'
		var row = '<tr>'
		+ '<td>' + (i) + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayOrdersList(data) {
	console.log('Printing Orders');
	var $tbody = $('#order-table2').find('tbody');
	$tbody.empty();
	data.reverse();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button style="padding: 0;border: none;background: none;" onclick="initializeDropdown(' + e.id + ')"><span class="material-icons">keyboard_arrow_down</span></button>';
		buttonHtml += '<button style="padding: 0;border: none;background: none;" onclick="deleteOrder(' + e.id + ')"><span class="material-icons" style="color:red">delete</span></button>';
		var row = '<tr class="order-header">'
		+ '<td>' + e.id + '</td>'
		+ '<td>'  + e.datetime + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		orderitemsHtml = '<tr><td colspan="3"><table style ="display:none;" class="table table-striped orderitemrows' + e.id +'"><tbody></tbody></table><td></tr>';
		console.log(orderitemsHtml);
    $tbody.append(row);
		$tbody.append(orderitemsHtml);
		getOrderItemsHtml(e.id);
	}
}

function displayEditOrderItem(id){
	var url = getOrderItemUrl() + "/" + id;
	ajaxQuery(url,'GET','',displayOrderItem);
}

function displayAddOrderModal() {
	$("#add-order-modal").modal('toggle');
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
	toastr.info("Order created");
	$("#container").append('<button onclick="downloadPDF(' + response.id +')">Download Invoice PDF</button>');
	getOrderList();
}

function createOrderItemsHtml(data,id) {

	var table = $('.orderitemrows' + id).find('tbody');
	var thHtml = '<tr>';
	thHtml += '<th scope="col">Barcode</th>';
	thHtml += '<th scope="col">Name</th>';
	thHtml += '<th scope="col">Quantity</th>';
	thHtml += '<th scope="col">Mrp</th>';
	thHtml += '<th scope="col">Actions</th>';
	thHtml += '</tr>';
	table.append(thHtml);
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button style="padding: 0;border: none;background: none;" onclick="deleteOrderItemFromOrderList(' + e.id + ')"><span class="material-icons" style="color:red">delete</span></button>';
		buttonHtml += '<button style="padding: 0;border: none;background: none;" onclick="displayEditOrderItem(' + e.id + ')"><span class="material-icons" style="color:#CCCC00">edit</span></button>';
		var row = '<tr>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.name + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + e.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
		table.append(row);
	}
	table.append('<tr><td colspan="3"><button class="btn btn-primary" onclick="downloadPDF('+id +')">Download Invoice PDF</button></td><td colspan="2"><button class="btn btn-primary" onclick="displayAddOrderItemModal(' + id + ')">Add Order Item</button></td></tr>');
}

function initializeDropdown(id) {
	console.log("Orderitems toggle");
	var orderitem_row = '.orderitemrows' + id;
  $(orderitem_row).toggle();
}

function validateOrderItem(json) {
	json = JSON.parse(json);
	if(isBlank(json.barcode)) {
		toastr.error("Barcode field must not be empty");
		return false;
	}
	if(isBlank(json.quantity) || isNaN(parseInt(json.quantity)) || !isInt(json.quantity)) {
		toastr.error("Quantity field must not be empty and must be an integer value");
		return false;
	}
	return true;
}


//INITIALIZATION CODE
function init(){
	$("#open-add-order").click(displayAddOrderModal);
	$('#add-orderitem').click(addOrderItemToList);
	$('#refresh-data').click(getOrderItemList);
	$('#add-order').click(addOrder);
	$("#add-orderitem-previousorders").click(addOrderItem);
	$('#update-orderitem').click(updateOrder);
}

$(document).ready(init);
$(document).ready(getOrderItemList);
$(document).ready(getOrderList);
