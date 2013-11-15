$(document).ready(function() {
		$('#submit').click(function(event) {
			var pollId = $('#poll_id').val();
			if (minValueCheck(pollId)) {
				var createPoll = "createPoll";
				$.get('PollServlet', {
					requestType : createPoll,
					poll : pollId,
					email : localStorage.email
				}, function(responseText) {
					if(responseText === "exist") {
						alert("Du svarade redan denna enkät. Du kan delta i varje omröstning gång.");
						window.location = 'poll.html';
					}
					else if (responseText === "invalid poll") {
						alert("Ogiltig Poll ID");
						window.location = 'poll.html';
					}
					else {
						$(responseText).insertAfter('#question');
						if (typeof (Storage) !== "undefined") {
							localStorage.poll_id = pollId;
						}
						window.location = '#question';
						window.location = '#question1';
					}
					
				});
				
			}
		});
		if(sessionStorage.poll_id) {
        	$('#poll_id').val(sessionStorage.poll_id);
        	$('#submit').trigger('click');
        }
	});
	var pageNumber = 1, results = [];
	function nextPage(isFinalPage) {
		var contents = $("#question" + pageNumber).find(".answers"), ranger = $(
				"#question" + pageNumber).find(".ranger");

		results.push("question" + pageNumber);
		contents.filter(":checked").each(function() {
			var text = $(this).next().find("span.ui-btn-text").text();
			if (text == undefined || text == "") {
				text = $(this).parent().next().text();
			}
			results.push(text);
		});
		contents.filter("textarea").each(function() {
			results.push($(this).val());
		});

		ranger.each(function() {
			if ($(this).attr("disabled") !== "disabled") {
				results.push($(this).val());
			}
		});
		if (isFinalPage) {
			if(sessionStorage.username)
				var mail = sessionStorage.username;
			else
				var mail = localStorage.email;
			var pollId = localStorage.poll_id;
			var answers = results.join(";");
			$.post('PollServlet', {
				email : mail,
				pollId : pollId,
				answers : answers
			}, function(data) {
				$("#feedback").append(data);
			});
			if(sessionStorage.poll_id) {
				sessionStorage.poll_id="";
			}
			window.location = '#final';
			return;
		} else {
			pageNumber += 1;
			window.location = '#question' + pageNumber;
		}
	}

	function prevPage() {
		pageNumber -= 1;
		var startingPoint = results.indexOf("question" + pageNumber);
		results.splice(startingPoint, results.length - startingPoint);
		window.location = '#question' + pageNumber;
	}

	function minValueCheck(value) {
		if (value < 1 || isNaN(value)) {
			alert("Ange ett giltigt nummer");
			$('#poll_id').val('');
			return false;
		}
		if (value > 0)
			return true;
	}