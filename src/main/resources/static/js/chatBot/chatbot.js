var stompClient = null;
var isFirstConnect = true;

function setConnected(connected) {
    $("#send").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#msg").html("");
}

function connect() {
    if (stompClient && stompClient.connected) {
        return; // 이미 연결되어 있으면 다시 연결하지 않음
    }

    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public', function (message) {
            showMessage(message.body, 'received_message');
        });

        if (isFirstConnect) {
            var welcomeMessage = "안녕하세요!! 반갑습니다!!";
            showMessage(welcomeMessage, 'sent_message');
            stompClient.send("/app/sendMessage", {}, JSON.stringify(welcomeMessage));
            isFirstConnect = false;
        }
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    let message = $("#msg").val().trim();
    if (message === "") {
        console.log("빈 메시지는 전송되지 않습니다.");
        return;
    }
    console.log("Sending message: " + message);
    showMessage(message, 'sent_message');
    stompClient.send("/app/sendMessage", {}, JSON.stringify(message));
    $("#msg").val('');
}

function showMessage(message, messageClass) {
    $("#communicate").append("<div class='" + messageClass + "'>" + message + "</div>");
    scrollToBottom();
}

function handleKeyUpEvent(e) {
    if (e.key === 'Enter' && !e.shiftKey) {
        e.preventDefault();
        sendMessage();
    }
}

function scrollToBottom() {
    var mainContent = document.getElementById('communicate');
    mainContent.scrollTop = mainContent.scrollHeight;
}

$(function () {
    $("#chatToggle").on('click', function() {
        $("#chatContainer").toggle();
        if ($("#chatContainer").is(":visible")) {
            connect();
        }
    });

    $("#chatClose").on('click', function() {
        $("#chatContainer").hide();
        // 연결 상태 유지, 창만 닫음
    });

    $(".chatbot-form").on('submit', function (e) {
        e.preventDefault();
    });

    $("#send").off('click').on('click', function() {
        sendMessage();
    });

    $("#msg").off('keyup').on('keyup', handleKeyUpEvent);

    $('#chatContainer').on('shown', function () {
        $('#msg').focus();
    });
});