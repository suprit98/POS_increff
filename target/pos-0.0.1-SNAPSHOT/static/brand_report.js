function getReportUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/report";
}


function getBrandReport(){
	var url = getReportUrl() + "/brand";
	console.log(url);
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
      	link.download="BrandReport_" + new Date() + ".pdf";
      	link.click();
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}

function getInventoryReport(){
	var url = getReportUrl() + "/inventory";
	console.log(url);
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
      	link.download="InventoryReport_" + new Date() + ".pdf";
      	link.click();
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}

function getSalesReport(){
	var $form = $("#sales-report-form");
	var json = toJson($form);
	var check = validateSalesForm(json);
	if(check) {
		var url = getReportUrl() + "/sales";
		console.log(url);
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
					console.log(blob.size);
	      	var link=document.createElement('a');
	      	link.href=window.URL.createObjectURL(blob);
	      	link.download="SalesReport_" + new Date() + ".pdf";
	      	link.click();
		   },
		   error: function(response){
		   		alert("Error has occurred");
		   }
		});
	}

}

function setDefaultDateValues() {
	document.getElementById("inputStartDate").defaultValue = "2020-01-01";
	document.getElementById("inputEndDate").defaultValue = "2020-12-31";
}
function endDateValidation() {
	var startDate = document.getElementById("inputStartDate").value;
	var endDate = document.getElementById("inputEndDate").value;

	if ((Date.parse(startDate) > Date.parse(endDate))) {
			toastr.error("End date should be greater than Start date");
			document.getElementById("inputEndDate").value = "2020-12-31";
	}
}

function startDateValidation() {
	var startDate = document.getElementById("inputStartDate").value;
	var endDate = document.getElementById("inputEndDate").value;

	if ((Date.parse(startDate) > Date.parse(endDate))) {
			toastr.error("Start date should be lesser than End date");
			document.getElementById("inputStartDate").value = "2020-01-01";
	}
}

function validateSalesForm(json) {
	json = JSON.parse(json);
	if(!brandList.includes(json.brand) && !isBlank(json.brand)) {
		toastr.error("Please enter valid brand");
		return false;
	}
	if(!categoryList.includes(json.category) && !isBlank(json.category)) {
		toastr.error("Please enter valid category");
		return false;
	}
	return true;
}

function init(){
	console.log("brand_report init");
	$('#brand-report').click(getBrandReport);
	$('#inventory-report').click(getInventoryReport);
	$('#sales-report').click(getSalesReport);
	$("#inputEndDate").change(endDateValidation);
	$("#inputStartDate").change(startDateValidation);
}

$(document).ready(init);
$(document).ready(setDefaultDateValues);
