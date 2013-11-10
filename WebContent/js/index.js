$('#div-email').hide();
$('#div-username').hide();
$.get('PollServlet',{requestType:"loginType"},function(responseText) { 
                       if(responseText === "email") { 
                    	   $('#div-username').hide();
                    	   $('#div-email').show();
                    	   sessionStorage.loginType = "email";
                       }
                       else if (responseText === "username") { 
                    	   $('#div-email').hide(); 
                    	   $('#div-username').show;
                    	   sessionStorage.loginType = "username";
                       }
                    	  
                    });
$(document).ready(function() {
            	var poll_id = getQueryVariable("id");
            	if(poll_id) {
            	    sessionStorage.poll_id = poll_id;
            	}
            	if(localStorage.email) {           		
            		$('#email').val(localStorage.email);
            	}
            	
                       if(sessionStorage.loginType === "email") { 
                    	   $('#div-username').hide();
                    	   $('#div-email').show();
                    	   sessionStorage.loginType = "email";
                       }
                       else if (sessionStorage.loginType === "username") { 
                    	   $('#div-email').hide(); 
                    	   $('#div-username').show;
                    	   sessionStorage.loginType = "username";
                       }
                
            	
                $('#submit').click(function(event) {
                	if(sessionStorage.loginType === "email"){
                		var mail = $('#email').val(); 
                    	if(validateEmail(mail)){
                            $.get('UserServlet',{loginType:"email",userType:"user",email:mail},function(responseText) { 
                            	if(typeof(Storage)!=="undefined") {
            			              localStorage.email=mail;
            			        }
                                   if(responseText === "exist")
                                	   window.location = 'poll.html';
                                   else if(responseText === "not exist")
                                	   window.location = 'default.html';
                                });
                    	}
                    	else {
                    		alert("Ange en giltig e-postadress.");
                    		$('#email').val('');
                    	}
                	}
                	else {
                		//var password = $('#password').val();
                		var password;
                		var username = $('#username').val();
                		$.get('UserServlet',{loginType:"username",userType:"user",username:username,password:password},function(responseText) { 
	                        if(responseText === "login exist"){
	                        	sessionStorage.username = username;
	                       	   	window.location = 'poll.html';
	                        }
	                        else if(responseText === "login new") {
	                        	sessionStorage.username = username;
	                        	window.location = 'default.html';
	                        }
	                        else {
                         	   alert("Fel användarnamn eller lösenord!");
                         	   $('#password').val('');
                            }
                        });
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