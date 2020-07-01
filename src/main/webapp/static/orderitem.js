var orderitemList = [];

function getOrderUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/order";
}

function addOrderItemToList(event) {
	var $form = $("#orderitem-form");
	var json = toJson($form);
	orderitemList.push(JSON.parse(json));
	console.log(orderitemList);
	getOrderItemList();

}

function getOrderItemList() {
	displayOrderItemList(orderitemList);
}

function addOrder(event){
	//Set the values to update
	var json = JSON.stringify(orderitemList);
	var url = getOrderUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
     },
		 xhrFields: {
        responseType: 'blob'
     },
	   success: function(blob) {
	   		alert("Order created");
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

	return false;
}

function updateOrder(event){
	$('#edit-orderitem-modal').modal('toggle');
	//Get the ID
	var id = $("#orderitem-edit-form input[name=id]").val();
	var url = getOrderUrl() + "/" + id;


	var $form = $("#orderitem-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getPreviousOrders();
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
	return false;

}

function deleteOrderItemFromOrderList(id) {
	var url = getOrderUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		console.log("Order Item deleted");
	   		getPreviousOrders();     //...
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}

function deleteOrderItem(id) {
	orderitemList.splice(id,1);
	getOrderItemList();
}

function getPreviousOrders() {
	var url = getOrderUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log(data);
	   		displayOrderList(data);     //...
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}


//UI DISPLAY METHODS

function displayOrderItemList(data){
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

function displayOrderList(data){
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

		console.log("Prev: " + prev);
		console.log("orderId: "+ parseInt(e.orderId));

		if(parseInt(prev) != parseInt(e.orderId) && parseInt(prev)!=0){

			$tbody.append(thHtml);
		}
		$tbody.append(row);

    prev = parseInt(e.orderId);
	}
}

function displayEditOrderItem(id){
	var url = getOrderUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayOrderItem(data);
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


//INITIALIZATION CODE
function init(){
	$('#add-orderitem').click(addOrderItemToList);
	$('#refresh-data').click(getOrderItemList);
	$('#add-order').click(addOrder);
	$('#update-orderitem').click(updateOrder);
}

$(document).ready(init);
$(document).ready(getOrderItemList);
$(document).ready(getPreviousOrders);
