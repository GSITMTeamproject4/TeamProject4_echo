var stompClient = null;

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
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public', function (message) {
            showMessage(message.body, 'received_message');
        });

        var welcomeMessage = "안녕하세요!! 반갑습니다!!";
        showMessage(welcomeMessage, 'sent_message');
        stompClient.send("/app/sendMessage", {}, JSON.stringify(welcomeMessage));
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

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });

    $("#send").off('click').on('click', function() { sendMessage(); });

    // keyup 이벤트 리스너를 한 번만 등록
    $("#msg").off('keyup').on('keyup', handleKeyUpEvent);

    // 채팅 시작 버튼을 누르면 자동으로 연결
    $("#chatModal").on("shown.bs.modal", function () {
        connect();
    });

    // 모달을 닫을 때 연결 해제
    $("#chatModal").on("hidden.bs.modal", function () {
        disconnect();
    });
});
