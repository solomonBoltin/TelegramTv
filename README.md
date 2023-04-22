# TelegramTv3

Telegram Tv version for watching movies.

This is an open source project to create a Telegram version suitable for AndroidTV, which focuses on content like movies and TV series rather than chats and messages. On the main view, it displays rows of movies where every line represents a chat, and every movie is a video file in a chat. People can write their custom ways to aggregate movies from chats using the structure of `MovieAggregator`.

The `MovieAggregator` will have the following functions:

```
Movie(client, message):
  fun isMovie() = message.type == video
  
MovieAggregator(client, chat_id): 
  fun messagesFlow()
  
  fun moviesFlow() = messagesFlow.map {
      Movie.tryLoad(message)
     }.filterNull()
  }
```

The default movie aggregator displays all video files and files that end with video endings.

Explanation:

- The text introduces the project, which is an open source version of Telegram for AndroidTV, designed to focus on movies and TV series instead of messaging.
- The main view of the project displays movies in rows, and each movie is a video file in a chat. The text also mentions that users can write their custom ways to aggregate movies from chats using the `MovieAggregator` structure.
- The code provided in the text is an implementation of the `MovieAggregator` structure, which has two functions: `messagesFlow()` and `moviesFlow()`. `messagesFlow()` retrieves messages from the chat, and `moviesFlow()` filters out only the messages that contain video files.
- The text also provides a code snippet that defines the `Movie` class, which has a function `isMovie()` to check if a message is a video file.