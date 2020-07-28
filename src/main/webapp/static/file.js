var barcodeList = [];
var brandList = [];
var categoryList = [];
var inventoryMap = {};

var errorData = [];

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

function getInventoryUrl(){
	var baseUrl = $("meta[name=baseUrl]").attr("content");
	console.log(baseUrl);
	return baseUrl + "/api/inventory";
}

function createBarcodeList() {
	var url = getProductDetailsUrl();
	ajaxQuery(url,'GET','',formBarcodeList);
}

function createBrandCategoryList() {
	var url = getBrandUrl();
	ajaxQuery(url,'GET','',formBrandCategoryList);
}

function createInventoryMap() {
	var url = getInventoryUrl();
	ajaxQuery(url,'GET','',formInventoryMap);
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

function formInventoryMap(data) {
	for(var i in data){
		var e = data[i];
		inventoryMap[e.barcode] = e.quantity;
	}
}

function readFileData(file, callback){
	var config = {
		header: true,
		delimiter: "\t",
		preview: 1000,
		skipEmptyLines: "greedy",
		complete: function(results) {
			callback(results);
	  	}
	}
	Papa.parse(file, config);
}

function checkHeader(file,header_list,callback) {
	var allHeadersPresent = true;
	Papa.parse(file,{
		delimiter: "\t",
		step: function(results, parser) {

        for(var i=0; i<header_list.length; i++){
					if(!results.data.includes(header_list[i])){
						allHeadersPresent = false;
						break;
					}
				}

        parser.abort();
        results=null;
        delete results;

    }, complete: function(results){

        results=null;
        delete results;
				if(allHeadersPresent) {
					readFileData(file,callback);
				}
				else{
					alert("Improper or absent headers in file");
				}

    }
	});
}

function writeFileData(arr){
	var config = {
		quoteChar: '',
		escapeChar: '',
		delimiter: "\t"
	};

	var data = Papa.unparse(arr, config);
    var blob = new Blob([data], {type: 'text/tsv;charset=utf-8;'});
    var fileUrl =  null;

    if (navigator.msSaveBlob) {
        fileUrl = navigator.msSaveBlob(blob, 'download.tsv');
    } else {
        fileUrl = window.URL.createObjectURL(blob);
    }
    var tempLink = document.createElement('a');
    tempLink.href = fileUrl;
    tempLink.setAttribute('download', 'download.tsv');
    tempLink.click();
}

function handleAjaxError(response) {
	console.log(response.responseText);
	var response = JSON.parse(response.responseText);
	toastr.error(response.message);
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

function ajaxQuery(url, type, data, successFunction,errorFunction) {
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
	   		errorFunction(response);
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
			  var error_obj = JSON.parse(response.responseText);
			  var error = "For " + data;
				console.log(error_obj.message);
				error_obj.message = error + " " + error_obj.message;
				console.log(error_obj.message);
	   		errorData.push(error_obj);
				recurFunction();
	   }
	});
}

function isBlank(str) {
    return (!str || /^\s*$/.test(str));
}

function isInt(n) {
   return n % 1 === 0;
}

function init() {
	createBarcodeList();
	createBrandCategoryList();
	createInventoryMap();
	console.log(barcodeList);
	$(".barcode").autocomplete({
		minLength:0,
		source: barcodeList
	});
	$(".brand").autocomplete({
		minLength:0,
		source: brandList
	});
	$(".category").autocomplete({
		minLength:0,
		source: categoryList
	});
}
$(document).ready(init);
