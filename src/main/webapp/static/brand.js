function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/brand";
}

//BUTTON ACTIONS
function addBrand(event){
	//Set the values to update
	$('#add-brand-modal').modal('toggle');
	var $form = $("#brand-form");
	var json = toJson($form);
	var check = validateBrand(json);
	if(check) {
		var url = getBrandUrl();
		ajaxQuery(url,'POST',json,getBrandList);
	}
	return false;
}

function updateBrand(event){
	$('#edit-brand-modal').modal('toggle');
	//Get the ID
	var id = $("#brand-edit-form input[name=id]").val();
	var url = getBrandUrl() + "/" + id;


	var $form = $("#brand-edit-form");
	var json = toJson($form);

	var check = validate(json);
	if(check) {
		ajaxQuery(url,'PUT',json,getBrandList);
	}

	return false;

}


function getBrandList(){
	var url = getBrandUrl();
	ajaxQuery(url,'GET','',displayBrandList);
}

function deleteBrand(id){
	var url = getBrandUrl() + "/" + id;
	ajaxQuery(url,'DELETE','',getBrandList);
}

//UI DISPLAY METHODS

function displayBrandList(data){
	console.log('Printing Brand data');
	var $tbody = $('#brand-table').find('tbody');
	$tbody.empty();
	for(var i in data){
		var e = data[i];
		var buttonHtml = '<button style="padding: 0;border: none;background: none;" onclick="deleteBrand(' + e.id + ')"><span class="material-icons" style="color:red">delete</span></button>'
		buttonHtml += ' <button style="padding: 0;border: none;background: none;" onclick="displayEditBrand(' + e.id + ')"><span class="material-icons" style="color:#CCCC00">edit</span></button>'
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
	ajaxQuery(url,'GET','',displayBrand);
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
	ajaxQueryRecur(url,'POST',json,uploadRows,uploadRows);

}

function displayUploadData(){
	$('#upload-brand-modal').modal('toggle');
}

function displayAddBrandModal() {
	$("#add-brand-modal").modal('toggle');
}

function updateFileName(){
	var $file = $('#brandFile');
	var fileName = $file.val();
	$('#brandFileName').html(fileName);
}

function validateBrand(json) {
	json = JSON.parse(json);
	if(isBlank(json.brand)) {
		alert("Brand field must not be empty");
		return false;
	}
	if(isBlank(json.category)) {
		alert("Category field must not be empty");
		return false;
	}
	return true;
}


//INITIALIZATION CODE
function init(){
	$("#open-add-brand").click(displayAddBrandModal);
	$('#add-brand').click(addBrand);
	$('#update-brand').click(updateBrand);
	$('#refresh-data').click(getBrandList);
	$('#upload-data').click(displayUploadData);
	$('#process-data').click(processData);
	$('#brandFile').on('change', updateFileName);
}

$(document).ready(init);
$(document).ready(getBrandList);
