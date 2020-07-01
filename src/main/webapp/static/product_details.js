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
	ajaxQuery(url,'POST',json,getProductDetailsList);

	return false;
}

function updateProductDetails(event){
	$('#edit-productdetails-modal').modal('toggle');
	//Get the ID
	var id = $("#productdetails-edit-form input[name=id]").val();
	var url = getProductDetailsUrl() + "/" + id;


	var $form = $("#productdetails-edit-form");
	var json = toJson($form);

	ajaxQuery(url,'PUT',json,getProductDetailsList);
	return false;

}


function getProductDetailsList(){
	var url = getProductDetailsUrl();
	ajaxQuery(url,'GET','',displayProductDetailsList);
}

function deleteProductDetails(id){
	var url = getProductDetailsUrl() + "/" + id;
	ajaxQuery(url,'DELETE','',getProductDetailsList);
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
	ajaxQuery(url,'GET','',displayProductDetails);
}

function displayProductDetails(data){
	$("#productdetails-edit-form input[name=brand]").val(data.brand);
	$("#productdetails-edit-form input[name=category]").val(data.category);
	$("#productdetails-edit-form input[name=name]").val(data.name);
	$("#productdetails-edit-form input[name=mrp]").val(data.mrp);
	$("#productdetails-edit-form input[name=id]").val(data.id);
	$('#edit-productdetails-modal').modal('toggle');
}

//FILE METHODS

var fileData = [];
var rowsProcessed = 0;

function processDataProductDetails(){
	var file = $('#productdetailsFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRowsProductDetails();
}

function uploadRowsProductDetails(){

	//If everything processed then return
	if(rowsProcessed==fileData.length){
		getProductDetailsList();
		return;
	}

	//Process next row
	var row = fileData[rowsProcessed];
	console.log(row);
	rowsProcessed++;

	var json = JSON.stringify(row);

	var url = getProductDetailsUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		uploadRowsProductDetails();
	   },
	   error: function(response){
			 	handleAjaxError(response);
	   		uploadRowsProductDetails();
	   }
	});

}

function displayUploadDataProductDetails(){
	$('#upload-productdetails-modal').modal('toggle');
}

function updateFileNameProductDetails(){
	var $file = $('#productdetailsFile');
	var fileName = $file.val();
	$('#productdetailsFileName').html(fileName);
}



//INITIALIZATION CODE
function init(){
	$('#add-productdetails').click(addProductDetails);
	$('#update-productdetails').click(updateProductDetails);
	$('#refresh-data-productdetails').click(getProductDetailsList);
	$('#upload-data-productdetails').click(displayUploadDataProductDetails);
	$('#process-data-productdetails').click(processDataProductDetails);
	$('#productdetailsFile').on('change', updateFileNameProductDetails);
}

$(document).ready(init);
$(document).ready(getProductDetailsList);
