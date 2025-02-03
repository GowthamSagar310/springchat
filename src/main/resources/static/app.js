'use strict';

var activeChatId = null;
var noActiveChatMessageElement = document.getElementById("no-active-chat-message");
var messageInputElement = document.getElementById("messageInput");
var sendMessageButton = document.getElementById("sendMessageButton");

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

    // var avatarElement = document.createElement('i'); // change it to display profile picture. GCP bucket ?
    // var avatarText = document.createTextNode((payloadJson.username)[0]);
    // avatarElement.style['background-color'] = "#5f8fc5"
    // avatarElement.appendChild(avatarText);
    //
    // messageElement.appendChild(avatarElement);

    var userIdElement = document.createElement("span");
    userIdElement.appendChild(document.createTextNode(payloadJson.userId + " :"));
    userIdElement.classList.add("username-style")
    messageElement.appendChild(userIdElement);

    var messageText = document.createElement("span");
    messageText.appendChild(document.createTextNode(payloadJson.message));
    messageElement.appendChild(messageText);

    messageElement.classList.add("chat-message")
    message_container.appendChild(messageElement);
    message_container.scrollTop = message_container.scrollHeight;
    return messageElement;

}

sendMessageButton.addEventListener("click", (event) => {
    var messageContent = messageInputElement.value.trim();
    if (messageContent && (stompClient && stompClient.connected) & activeChatId) {
        stompClient.publish({
            destination: '/app/sendMessage',
            body: JSON.stringify({
                userId: userId,
                username: username,
                chatId: activeChatId,
                message: messageContent,
            })
        });
    } else {
        console.error("WebSocket is not connected.");
        if (!activeChatId) {
            console.error("there is no active chat")
        }
    }
    messageInputElement.value = '';
    event.preventDefault();
})

// move this to new js file
// set active chat id
function setActiveChatId(event, chatLinkElement) {
    activeChatId = chatLinkElement.getAttribute('data-chat-id');
    console.log("change active chatId to: " + activeChatId);

    noActiveChatMessageElement.style.display = 'none';

    messageInputElement.disabled = false;
    sendMessageButton.disabled = false;
}