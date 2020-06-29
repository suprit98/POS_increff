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
	   success: function(response) {
	   		console.log("Order created");
	   		getBrandList();     //...
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});

	return false;
}

function deleteOrderItem(id) {
	orderitemList.splice(id,1);
	getOrderItemList();
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
		+ '<td>' + (i+1) + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}





//HELPER METHOD
function toJson($form){
    var serialized = $form.serializeArray();
    console.log(serialized);
    var s = '';
    var data = {};
    for(s in serialized){
        data[serialized[s]['name']] = serialized[s]['value']
    }
    var json = JSON.stringify(data);
    console.log(json);
    return json;
}


//INITIALIZATION CODE
function init(){
	$('#add-orderitem').click(addOrderItemToList);
	$('#refresh-data').click(getOrderItemList);
	$('#add-order').click(addOrder);
}

$(document).ready(init);
$(document).ready(getOrderItemList);
