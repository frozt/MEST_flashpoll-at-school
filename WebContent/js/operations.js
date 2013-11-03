        if(sessionStorage.loggedIn != "true")
        	window.location = 'login.html';
        $(document).ready(function() {
        	var activePolls = "activePolls" ;
        	$.get('PollServlet',{requestType:activePolls},function(responseText) {
        		if(responseText != null) {
        			if(responseText.indexOf(";") != -1) {
        				var polls = responseText.split(";");
        				$("#pollsList").html("");
        				for(var i=0; i<polls.length; i++) {
        					$("#pollsList").append("<input type=\"radio\" name=\"polls\" id=\"" + polls[i] + "\" value=\""+polls[i]+"\"/>"+polls[i]+"<br>");
        				}
                        
        			}
        			else
        				$("#pollsList").append("<input type=\"radio\" name=\"polls\" id=\"" + responseText + "\" value=\""+responseText+"\"/>"+responseText+"<br>");
        		}
        	});
        });
        function deactivate() {
        	var poll_id = $('input[name="polls"]:checked').val();
        	$.get('PollServlet',{requestType:"deactivatePoll",pollId:poll_id},function(responseText) {
        		location.reload();
        	});
        }
        function deletePoll() {
        	var poll_id = $('input[name="polls"]:checked').val();
        	$.get('PollServlet',{requestType:"deletePoll",pollId:poll_id},function(responseText) {
        		location.reload();
        	});
        }
        function logout () {
  			sessionStorage.username = "";
  			sessionStorage.loggedIn = false;
  			window.location = 'login.html';
  		}