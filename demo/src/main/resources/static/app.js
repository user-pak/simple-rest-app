var stompClient = null;

function setConnected(connected) {
	
	$("#connect").prop("disabled", connected);
	$("#disconnect").prop("disabled",!connected);
	
	if(connected) {
		$("#message").show();
	}
	else {
		$("#message").hide();
	}
	
	$("#messaging").html('');
}

function connect() {
	
	var socket = new SockJS("/gs-guide-websocket");
	stompClient = Stomp.over(socket);
	stompClient.connect({}, function(frame) {
		
		console.log(frame.user-name);
		setConnected(true);
		stompClient.subscribe('/topic/messaging', function(serverMessage) {
			showMessage(JSON.parse(serverMessage.body).content);
			$("#message").val("");
		})
	})
}

function disconnect() {
	
	if(stompClient != null) {
		stompClient.disconnect();
	}
	setConnected(false);
}

function sendMessage() {
	
	stompClient.send("/app/hello", {} , JSON.stringify({'content': $("#message").val()}));
}

function showMessage(message) {
	
	$("#messaging").append("<tr><td>" + message + "</td></tr>");
}

$(function() {
	
	$("form").on("submit", function(e) {
		e.preventDefault();
	})
	$("#connect").click(function() { connect();})
	$("#disconnect").click(function() { disconnect(); })
	$("#send").click(function() { sendMessage(); })
})