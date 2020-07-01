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
