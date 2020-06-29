function getProductDetailsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/product_details";
}

//BUTTON ACTIONS
function addProductDetails(event){
	//Set the values to update
	var $form = $("#productdetails-form");
	var json = toJson($form);
	var url = getProductDetailsUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		console.log("ProductDetails created");
	   		getProductDetailsList();     //...
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});

	return false;
}

function updateProductDetails(event){
	$('#edit-productdetails-modal').modal('toggle');
	//Get the ID
	var id = $("#productdetails-edit-form input[name=id]").val();
	var url = getProductDetailsUrl() + "/" + id;


	var $form = $("#productdetails-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getProductDetailsList();
	   },
	   error: function(){
	   		handleAjaxError();
	   }
	});
	return false;

}


function getProductDetailsList(){
	var url = getProductDetailsUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("ProductDetails data fetched");
	   		console.log(data);
	   		displayProductDetailsList(data);     //...
	   },
	   error: function(){
	   		handleAjaxError();
	   }
	});
}

function deleteProductDetails(id){
	var url = getProductDetailsUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		console.log("ProductDetails deleted");
	   		getProductDetailsList();     //...
	   },
	   error: function(){
	   		handleAjaxError();
	   }
	});
}

//UI DISPLAY METHODS

function displayProductDetailsList(data){
	console.log('Printing ProductDetails data');
	var $tbody = $('#productdetails-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="deleteProductDetails(' + e.id + ')">delete</button>';
		buttonHtml += ' <button onclick="displayEditProductDetails(' + e.id + ')">edit</button>';
		console.log('brand');
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>'  + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>'  + e.name + '</td>'
		+ '<td>'  + e.mrp + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
    $tbody.append(row);
	}
}


function displayEditProductDetails(id){
	var url = getProductDetailsUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayProductDetails(data);
	   },
	   error: function(){
	   		handleAjaxError();
	   }
	});
}

function displayProductDetails(data){
	$("#productdetails-edit-form input[name=brand]").val(data.brand);
	$("#productdetails-edit-form input[name=category]").val(data.category);
	$("#productdetails-edit-form input[name=name]").val(data.name);
	$("#productdetails-edit-form input[name=mrp]").val(data.mrp);
	$("#productdetails-edit-form input[name=id]").val(data.id);
	$('#edit-productdetails-modal').modal('toggle');
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
	$('#add-productdetails').click(addProductDetails);
	$('#update-productdetails').click(updateProductDetails);
	$('#refresh-data-productdetails').click(getProductDetailsList);
}

$(document).ready(init);
$(document).ready(getProductDetailsList);
