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
	ajaxQuery(url,'POST',json,getInventoryList);

	return false;
}

function updateInventory(event){
	$('#edit-inventory-modal').modal('toggle');
	//Get the ID
	var id = $("#inventory-edit-form input[name=id]").val();
	var url = getInventoryUrl() + "/" + id;


	var $form = $("#inventory-edit-form");
	var json = toJson($form);
	ajaxQuery(url,'PUT',json,getInventoryList);
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



//UI DISPLAY METHODS

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




//INITIALIZATION CODE
function init(){
	$('#open-add-inventory').click(openAddInventoryModal);
	$('#add-inventory').click(addInventory);
	$('#update-inventory').click(updateInventory);
	$('#refresh-data-inventory').click(getInventoryList);
}

$(document).ready(init);
$(document).ready(getInventoryList);
