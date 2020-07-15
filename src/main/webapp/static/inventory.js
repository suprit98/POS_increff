function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/inventory";
}

//BUTTON ACTIONS
function addInventory(event){
	$('#add-inventory-modal').modal('toggle');
	//Set the values to update
	var $form = $("#inventory-form");
	var json = toJson($form);
	var url = getInventoryUrl();

	var check = validateInventory(json);
	if(check){
		ajaxQuery(url,'POST',json,getInventoryList);
	}


	return false;
}

function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();
	var url = getInventoryUrl() + "/" + id;


	var $form = $("#inventory-edit-form");
	var json = toJson($form);
	var check = validateInventory(json);
	if(check){
		ajaxQuery(url,'PUT',json,getInventoryList);
	}
	return false;

}

function getInventoryList(){
	var url = getInventoryUrl();
	ajaxQuery(url,'GET','',displayInventoryList);
}

function deleteInventory(id){
	var url = getInventoryUrl() + "/" + id;
	ajaxQuery(url,'DELETE','',getInventoryList);
}

//FILE METHODS

var fileData = [];
var errorData = [];
var rowsProcessed = 0;

function processDataInventory(){
	var file = $('#inventoryFile')[0].files[0];
	readFileData(file, readFileDataCallback);
}

function readFileDataCallback(results){
	fileData = results.data;
	uploadRowsInventory();
}

function uploadRowsInventory(){

	//If everything processed then return
	if(rowsProcessed==fileData.length){
		getInventoryList();
		return;
	}

	//Process next row
	var row = fileData[rowsProcessed];
	console.log(row);
	rowsProcessed++;

	var json = JSON.stringify(row);

	var url = getInventoryUrl();

	$.ajax({
	   url: url,
	   type: 'POST',
	   data: json,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		uploadRowsInventory(response);
	   },
	   error: function(response){
				errorData.push(JSON.parse(response.responseText));
				uploadRowsInventory();
	   }
	});

}

function downloadErrors(){
	writeFileData(errorData);
}


//UI DISPLAY METHODS

function displayUploadDataInventory(){
	resetUploadDialogInventory();
	$('#upload-inventory-modal').modal('toggle');
}

function updateFileNameInventory(){
	var $file = $('#inventoryFile');
	var fileName = $file.val();
	$('#inventoryFileName').html(fileName);
}

function displayInventoryList(data){
	console.log('Printing Inventory data');
	var $tbody = $('#inventory-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button style="padding: 0;border: none;background: none;" onclick="deleteInventory(' + e.id + ')"><span class="material-icons" style="color:red">delete</span></button>'
		buttonHtml += ' <button style="padding: 0;border: none;background: none;" onclick="displayEditInventory(' + e.id + ')"><span class="material-icons" style="color:#CCCC00">edit</span></button>'
		var row = '<tr>'
		+ '<td>' + e.id + '</td>'
		+ '<td>' + e.barcode + '</td>'
		+ '<td>' + e.quantity + '</td>'
		+ '<td>' + buttonHtml + '</td>'
		+ '</tr>';
        $tbody.append(row);
	}
}

function displayEditInventory(id){
	var url = getInventoryUrl() + "/" + id;
	ajaxQuery(url,'GET','',displayInventory);
}

function openAddInventoryModal() {
	$('#add-inventory-modal').modal('toggle');
}

function displayInventory(data){
	$("#inventory-edit-form input[name=barcode]").val(data.barcode);
	$("#inventory-edit-form input[name=quantity]").val(data.quantity);
	$("#inventory-edit-form input[name=id]").val(data.id);
	$('#edit-inventory-modal').modal('toggle');
}

function resetUploadDialogInventory(){

	var $file = $('#inventorysFile');
	$file.val('');
	$('#inventoryFileName').html("Choose File");

	processCount = 0;
	fileData = [];
	errorData = [];
}

function validateInventory(json) {
	json = JSON.parse(json);
	if(isBlank(json.barcode)) {
		alert("Barcode field must not be empty");
		return false;
	}
	if(isBlank(json.quantity) || isNaN(parseInt(json.quantity))) {
		alert("Quantity field must not be empty and must be an integer value");
		return false;
	}
	return true;
}


//INITIALIZATION CODE
function init(){
	$('#open-add-inventory').click(openAddInventoryModal);
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data-inventory').click(getInventoryList);
	$('#upload-data-inventory').click(displayUploadDataInventory);
	$('#process-data-inventory').click(processDataInventory);
	$('#inventoryFile').on('change', updateFileNameInventory);
	$('#download-errors-inventory').click(downloadErrors);
}

$(document).ready(init);
$(document).ready(getInventoryList);
