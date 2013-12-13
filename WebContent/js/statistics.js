$(document).ready(function() {
	var poll_id = getQueryVariable("id");
	if (poll_id) {
		sessionStorage.poll_id = poll_id;
	}
	$.get('PollServlet', {requestType:"statistics" ,pollId : sessionStorage.poll_id}, function(responseText) {
		$('#results').append(responseText);
	});

});
function getQueryVariable(variable) {
	var query = window.location.search.substring(1);
	var vars = query.split("&");
	for (var i = 0; i < vars.length; i++) {
		var pair = vars[i].split("=");
		if (pair[0] == variable) {
			return pair[1];
		}
	}
}