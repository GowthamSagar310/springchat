if a for a chat application, there is a huge amount of data (message)
can we just store very old messages in some other dumb and reduce the load on main instances ? 
- only when user requests for a backup, we need to get all of them ? 
- what GCP service can be used here ? GCP's cloud storage options like Near-line, Cold-line, Archive can be used. 

---
## FLOW 

user opens chat application 
1. if not logged-in, show log-in screen (index.html)
   1. if successful: redirect to login page. 
   2. else: stay in index.html 
2. if user is already logged-in (spring security checks the authentication, JWT token?), show home screen (home.html)
3. to populate the inbox and chat window,
   1. query cassandra for chats for user_id (last 10), each chat should contain last_message, last_message_time, (maybe, who sent it also). so that the inbox can contain last messages displayed.
   2. if the user clicks on a chat, retrieve its last 15/20 messages.

---
## Doubts 
1. how are topics handled in real world applications like Whatsapp ?
2. 

