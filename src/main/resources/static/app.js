'use strict';

var activeChatId = null;
var noActiveChatMessageElement = document.getElementById("no-active-chat-message");
var messageInputElement = document.getElementById("messageInput");
var sendMessageButton = document.getElementById("sendMessageButton");

var currentSubscription = null; // to track current chat websocket subscription

// stomp client
const stompClient = new StompJs.Client({
    brokerURL: 'ws://localhost:9090/chat',
    reconnectDelay: 5000,
    heartbeatIncoming: 20000,
    heartbeatOutgoing: 20000,
    onConnect: function (frame) {
        username = frame.username;
        console.log("WebSocket connected");
        subscribeToInboxTopic();
    },
    onDisconnect: function () {
        console.log("WebSocket disconnected");
        if (currentSubscription) {
            currentSubscription.unsubscribe();
            currentSubscription = null
        }
    }
});
stompClient.activate();

var message_container = document.getElementById("message-container");

// handle inbox notification
function subscribeToInboxTopic() {
    const inboxTopic = `/topic/inbox/${userId}`;
    stompClient.subscribe(inboxTopic, (message) => {
        handleInboxNotification(JSON.parse(message.body));
    });
    console.log("Subscribed to inbox topic: " + inboxTopic);
}

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
    if (messageContent && (stompClient && stompClient.connected) && activeChatId) {
        stompClient.publish({
            destination: '/app/sendMessage',
            body: JSON.stringify({
                senderId: userId,
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

    // unsubscribe from previous chat
    if (currentSubscription) {
        console.log("Unsubscribing from previous topic");
        currentSubscription.unsubscribe();
        currentSubscription = null;
    }

    // subscribe to new chat
    if (activeChatId) {
        const topic = `/topic/chat/${activeChatId}`;
        console.log("Subscribing to topic: " + topic);
        currentSubscription = stompClient.subscribe(topic, onMessageReceived);

        // remove unread chat class
        const chatItem = document.getElementById(`chat-item-${activeChatId}`);
        if (chatItem) {
            chatItem.classList.remove('unread-chat');
        }

    } else {
        // No chat selected - show "no chat" message, disable input
        document.getElementById('no-active-chat-message').style.display = 'block';
        document.getElementById('message-container').style.display = 'none';
        document.querySelector('.chat-input').classList.add('disabled');
        document.getElementById('messageInput').disabled = true;
        document.getElementById('sendMessageButton').disabled = true;
    }


    noActiveChatMessageElement.style.display = 'none';

    messageInputElement.disabled = false;
    sendMessageButton.disabled = false;
}

function handleInboxNotification(notification) {
    console.log("Inbox Notification received:", notification);
    const chatId = notification.chatId;
    const messagePreview = notification.messagePreview;
    // const senderUsername = notification.senderUsername; // You can use senderUsername if needed

    const chatItem = document.getElementById(`chat-item-${chatId}`); // Select chat item by ID
    if (chatItem) {
        const lastMessageContentElement = chatItem.querySelector('.last-message-content'); // Select last message <p>
        if (lastMessageContentElement) {
            lastMessageContentElement.innerText = messagePreview; // Update last message preview
        }
        chatItem.classList.add('unread-chat'); // Add 'unread-chat' class to make it bold
    } else {
        console.warn("Chat item not found in inbox for chatId: " + chatId); // Log if chat item is not found (for debugging)
    }
}
