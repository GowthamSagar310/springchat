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
    var payloadJson = JSON.parse(payload.body);
    message_container.appendChild(createMessageElement(payloadJson))
}

// helper function to create a message element (chat-message)
function createMessageElement(payloadJson) {
    var messageElement = document.createElement("div");

    var avatarElement = document.createElement('i'); // change it to display profile picture. GCP bucket ?
    var avatarText = document.createTextNode((payloadJson.username)[0]);
    avatarElement.style['background-color'] = "#5f8fc5"
    avatarElement.appendChild(avatarText);

    messageElement.appendChild(avatarElement);
    messageElement.classList.add("chat-message")

    var messageText = document.createElement("span");
    messageText.appendChild(document.createTextNode(payloadJson.message));
    messageElement.appendChild(messageText);

    message_container.appendChild(messageElement);
    message_container.scrollTop = message_container.scrollHeight;
    return messageElement;

}

// send message
var sendMessageButton = document.getElementById("sendMessageButton")
sendMessageButton.addEventListener("click", (event) => {
    var messageInputElement = document.getElementById("messageInput");
    var messageContent = messageInputElement.value.trim();
    if (messageContent && (stompClient && stompClient.connected)) {
        stompClient.publish({
            destination: '/app/sendMessage',
            body: JSON.stringify({user: username, message: messageContent}),
        });
    } else {
        console.error("WebSocket is not connected.");
    }
    event.preventDefault();
})
