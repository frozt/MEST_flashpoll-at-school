function checkLogin() {
	var userName = $('#username').val();
	var password = $('#password').val();
	var userType = "admin";
    $.get('UserServlet',{userType:userType ,username:userName,password:password},function(responseText) { 
        if(responseText === "true") {
       	   window.location = 'admin.html';
       		if(typeof(Storage)!=="undefined") {
	          sessionStorage.username=userName;
	          sessionStorage.loggedIn= true;
	    	}
       	}
        else {
        	alert("Wrong username or password!");
        	$('#password').val('');
        }
              
        });
}