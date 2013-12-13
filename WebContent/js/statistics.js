$(document).ready(function() {
            	
            });
var poll_id = getQueryVariable("id");
alert(poll_id);
if(poll_id) {
    sessionStorage.poll_id = poll_id;
}
alert("evet");

