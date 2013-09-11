$(document).ready(function() {
            	var poll_id = getQueryVariable("id");
            	if(poll_id) {
            	    sessionStorage.poll_id = poll_id;
            	}
                $('#submit').click(function(event) {
                	var mail = $('#email').val(); 
                	if(validateEmail(mail)){
                        $.get('UserServlet',{userType:"user",email:mail},function(responseText) { 
                        	if(typeof(Storage)!=="undefined") {
        			              localStorage.email=mail;
        			        }
                               if(responseText === "exist")
                            	   window.location = 'poll.html';
                               else
                            	   window.location = 'default.html';   
                            });
                	}
                	else {
                		alert("Please enter a valid email.");
                		$('#email').val('');
                	}
                });
            });
            function validateEmail(email) { 
            	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
                return re.test(email);
            } 
            function getQueryVariable(variable) {
                var query = window.location.search.substring(1);
                var vars = query.split("&");
                for (var i=0;i<vars.length;i++) {
                  var pair = vars[i].split("=");
                  if (pair[0] == variable) {
                    return pair[1];
                  }
                } 
              }