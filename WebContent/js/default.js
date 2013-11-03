$(document).ready(function() {   
            $('#submit').click(function(event) {  
            	var pollId = $('#poll_id').val(); 
            	if(numberCheck(pollId)) {
            		var createPoll = "createPoll";
                    $.get('PollServlet',{poll:pollId,requestType:createPoll},function(responseText) { 
                           $(responseText).insertAfter('#occupation');
                        });
                    if(typeof(Storage)!=="undefined") {
        	              localStorage.poll_id=pollId;
        	        }
                    window.location = '#gender';
            	}
            	else {
            		alert("Ange ett giltigt nummer.");
                    $('#poll_id').val('');
            	}
            });         
            if(sessionStorage.poll_id) {
            	$('#poll_id').val(sessionStorage.poll_id);
            	$('#submit').trigger('click');
            }
        });
			function saveUser() {
				var gender = $('input[name=gender]').filter(':checked').val();
				var age = $('#ageBox').val();
				var occupation = $('#occupt').val();
				if(sessionStorage.loginType === "email") {
					var mail=localStorage.email;
					$.post('UserServlet',{loginType:sessionStorage.loginType,email:mail, gender:gender, age:age, occupation:occupation},function(data) {
					});
				}
				else {
					var username = sessionStorage.username;
					$.post('UserServlet',{loginType:sessionStorage.loginType,username:username, gender:gender, age:age, occupation:occupation},function(data) {
					});	
				}
				window.location = '#question1';				
			}
			
			var pageNumber=1,
        	results = [];
        function nextPage(isFinalPage) {
            var contents = $("#question" + pageNumber).find(".answers"),
                ranger = $("#question" + pageNumber).find(".ranger");

            results.push("question" + pageNumber);
            contents.filter(":checked").each(function () {
                var text = $(this).next().find("span.ui-btn-text").text();
                if (text == undefined || text == "") {
                    text = $(this).parent().next().text();
                }
                results.push(text);
            });
            contents.filter("textarea").each(function () {
                results.push($(this).val());
            });

            ranger.each(function () {
                if ($(this).attr("disabled") !== "disabled") {
                    results.push($(this).val());
                }
            });
            if (isFinalPage) {
            	if(sessionStorage.username)
            		var mail=sessionStorage.username;
            	else
            		var mail=localStorage.email;
                var pollId = localStorage.poll_id;
                var answers = results.join(";");
                $.post('PollServlet',{email:mail, pollId:pollId, answers:answers},function(data) {
                	$("#feedback").append(data);
				}); 
                if(sessionStorage.poll_id) {
    				sessionStorage.poll_id="";
    			}
                window.location = '#final';
                return;
            }
            else {
            	pageNumber += 1;
                window.location = '#question' + pageNumber;
            }
        }
        
        function prevPage() {
        	pageNumber -= 1;
            var startingPoint = results.indexOf("question"+pageNumber);
            results.splice(startingPoint,results.length - startingPoint);
            window.location = '#question' +pageNumber;
        }
        function ageCheck () {
        	if(!numberCheck($('#ageBox').val())) {
        		alert("Ange ett giltigt nummer.");
        		$('#ageBox').val('');
        	}
        }
        function numberCheck (value) 
        {
            if(value < 1 || isNaN(value)) {
                return false;
            }
            if (value >0)
            	return true;
        }