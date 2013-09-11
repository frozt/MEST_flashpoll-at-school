if(sessionStorage.loggedIn != "true" )
            	window.location = 'login.html';
            function uploadFile() {
                var file = $('#import').get(0).files[0]; 

                var formData = new FormData();
                formData.append("file", file);

                var xhr = new XMLHttpRequest();
                xhr.open("POST", "FileServlet");
                xhr.onreadystatechange=function() {
                	  if (xhr.readyState==4) {
                	   alert("Poll Number is: "+xhr.responseText);
                	  }
                	 };
                xhr.send(formData);                
              }
            function logout () {
      			sessionStorage.username = "";
      			sessionStorage.loggedIn = false;
      			window.location = 'login.html';
      		}