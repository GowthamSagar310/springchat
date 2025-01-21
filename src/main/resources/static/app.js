'use strict';

var username = null;
// stomp client
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:9090/chat',
    reconnectDelay: 5000,
    heartbeatIncoming: 20000,
    heartbeatOutgoing: 20000,
    onConnect: function (frame) {
        const subscription = stompClient.subscribe('/topic/public', onMessageReceived)
        username = frame.username;
    }
});
stompClient.activate();

var message_container = document.getElementById("message-container");

function onMessageReceived(payload) {
    // client recieves message / payload from the server
    var message = JSON.parse(payload.body);
    var messageElement = document.createElement("div");
    messageElement.classList.add("message")
    messageElement.appendChild(document.createTextNode("RECEIEVED: " + message));
    message_container.appendChild(messageElement);
}

// send message
var sendMessageButton = document.getElementById("sendMessageButton")
sendMessageButton.addEventListener("click", (event) => {
    if (stompClient && stompClient.connected) {
        stompClient.publish({
            destination: '/app/sendMessage',
            body: JSON.stringify({user: username, message: "dummy message"}),
        });
        // var messageElement = document.createElement("div");
        // messageElement.appendChild(document.createTextNode("SENT: User n: dummy message"));
        // messageElement.classList.add("message");
        // message_container.appendChild(messageElement);
    } else {
        console.error("WebSocket is not connected.");
    }
    event.preventDefault();
})
