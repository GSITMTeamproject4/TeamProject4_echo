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
    let message = $("#msg").val().trim(); // trim()을 사용하여 공백 제거
    if (message === "") {
        console.log("빈 메시지는 전송되지 않습니다."); // 빈 메시지 로그 추가
        return; // 빈 메시지일 경우 함수 종료
    }
    console.log("Sending message: " + message); // 메시지 전송 로그 추가
    showMessage(message, 'sent_message');
    stompClient.send("/app/sendMessage", {}, JSON.stringify(message));
    $("#msg").val(''); // 메시지 전송 후 입력 필드 초기화
}


function showMessage(message, messageClass) {
    $("#communicate").append("<div class='" + messageClass + "'>" + message + "</div>");
}

function handleKeyUpEvent(e) {
    if (e.key === 'Enter' && !e.shiftKey) { // Shift + Enter는 줄바꿈으로 처리
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

    // 채팅 시작 버튼을 누르면 자동으로 연결
    $("#send").off('click').on('click', function() {
        sendMessage();
    });

    // 모달을 닫을 때 연결 해제
    $("#msg").off('keyup').on('keyup', handleKeyUpEvent);

    $('#chatContainer').on('shown', function () {
        $('#msg').focus();
    });
});
