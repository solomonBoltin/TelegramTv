# TelegramTv3
Telegram Tv version for watching movies


This is a open source project to make a telegram version sutied for androidTv, that is focused on content like movies and tv searise rather then on chats and messages 
On the main view it exposes rows of movies where every line represents a chat and every movie is a video file in a chat 
Pepole can write there custom ways to aggreagte movies from chats using the structer of MovieAggregator 
the MovieAggregator will have functions 


Movie(client, message):
  fun isMovie() = 
    message.type = video
    
MovieAggregator(client, chat_id): 
  fun messagesFlow()
  
  fun moviesFlow() = messagesFlow.map {
      Movie.tryLoad(message)
     }.filterNull()
  }
  
  
the default movie aggregator is just displaing all video files and files that ends with video endings 
