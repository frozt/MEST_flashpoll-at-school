        if(sessionStorage.loggedIn != "true")
    		window.location = 'login.html';
  		function logout () {
  			sessionStorage.username = "";
  			sessionStorage.loggedIn = false;
  			window.location = 'login.html';
  		}