var barcodeList = [];
var brandList = [];
var categoryList = [];

function getBrandUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/brand";
}

function getProductDetailsUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/product_details";
}

function createBarcodeList() {
	var url = getProductDetailsUrl();
	ajaxQuery(url,'GET','',formBarcodeList);
}

function createBrandCategoryList() {
	var url = getBrandUrl();
	ajaxQuery(url,'GET','',formBrandCategoryList);
}

function formBarcodeList(data) {
	for(var i in data){
		var e = data[i];
		barcodeList.push(e.barcode);
	}
}

function formBrandCategoryList(data) {
	for(var i in data){
		var e = data[i];
		brandList.push(e.brand);
		categoryList.push(e.category);
	}
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}
	}
	Papa.parse(file, config);
}

function handleAjaxError(response) {
	console.log(response.responseText);
	var response = JSON.parse(response.responseText);
	alert(response.message);
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

function ajaxQuery(url, type, data, successFunction) {
	$.ajax({
	   url: url,
	   type: type,
	   data: data,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		successFunction(response);
	   },
	   error: function(response){
	   		handleAjaxError(response);
	   }
	});
}

function ajaxQueryRecur(url, type, data, successFunction,recurFunction) {
	$.ajax({
	   url: url,
	   type: type,
	   data: data,
	   headers: {
       	'Content-Type': 'application/json'
       },
	   success: function(response) {
	   		successFunction(response);
	   },
	   error: function(response){
	   		handleAjaxError(response);
				recurFunction();
	   }
	});
}

function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

function init() {
	createBarcodeList();
	createBrandCategoryList();
	console.log(barcodeList);
	$(".barcode").autocomplete({
		source: barcodeList
	});
	$(".brand").autocomplete({
		source: brandList
	});
	$(".category").autocomplete({
		source: categoryList
	});
}
$(document).ready(init);
