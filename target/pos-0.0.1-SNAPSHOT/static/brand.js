function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	var $form = $("#brand-form");
	var json = toJson($form);
	var url = getBrandUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		console.log("Brand created");
	   		getBrandList();     //...
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});

	return false;
}

function updateBrand(event){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;


	var $form = $("#brand-edit-form");
	var json = toJson($form);

	$.ajax({
	   url: url,
	   type: 'PUT',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		getBrandList();
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
	return false;

}


function getBrandList(){
	var url = getBrandUrl();
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		console.log("Brand data fetched");
	   		console.log(data);
	   		displayBrandList(data);     //...
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}

function deleteBrand(id){
	var url = getBrandUrl() + "/" + id;

	$.ajax({
	   url: url,
	   type: 'DELETE',
	   success: function(data) {
	   		console.log("Brand deleted");
	   		getBrandList();     //...
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}

//UI DISPLAY METHODS

function displayBrandList(data){
	console.log('Printing Brand data');
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button onclick="deleteBrand(' + e.id + ')">delete</button>'
		buttonHtml += ' <button onclick="displayEditBrand(' + e.id + ')">edit</button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.brand + '</td>'
		+ '<td>'  + e.category + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}


function displayEditBrand(id){
	var url = getBrandUrl() + "/" + id;
	$.ajax({
	   url: url,
	   type: 'GET',
	   success: function(data) {
	   		displayBrand(data);
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}

function displayBrand(data){
	$("#brand-edit-form input[name=brand]").val(data.brand);
	$("#brand-edit-form input[name=category]").val(data.category);
	$("#brand-edit-form input[name=id]").val(data.id);
	$('#edit-brand-modal').modal('toggle');
}

//FILE METHODS

var fileData = [];
var rowsProcessed = 0;

function processData(){
	var file = $('#brandFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRows();
}

function uploadRows(){

	//If everything processed then return
	if(rowsProcessed==fileData.length){
		getBrandList();
		return;
	}

	//Process next row
	var row = fileData[rowsProcessed];
	console.log(row);
	rowsProcessed++;

	var json = JSON.stringify(row);

	var url = getBrandUrl();

	//Make ajax call
	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		uploadRows();
	   },
	   error: function(response){
			 	handleAjaxError(response);
	   		uploadRows();
	   }
	});

}

function displayUploadData(){
	$('#upload-brand-modal').modal('toggle');
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
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
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#brandFile').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getBrandList);
