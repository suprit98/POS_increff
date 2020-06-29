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
