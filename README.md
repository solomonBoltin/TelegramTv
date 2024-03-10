
// dear codex writer, this is a markdown file, so don't suggest code, but rather explain it in words
# TelegramTv

Telegram Tv is an open source Telegram version suitable for AndroidTV, which focuses on movies and TV series rather than chats and messages. 
On the main view, it displays rows of movies where every line represents a chat, and every movie is a video file in a chat. 
People can write custom ways to aggregate movies from chats using the structure of `MovieAggregator` and `Movie`.

These are the default aggregators:

```
MovieChatAggregator(client, chat): 
  fun messagesFlow() 
  
  fun moviesFlow() = messagesFlow.map {
      Movie.tryLoad(message)
     }.filterNull()
  }
```

```
Movie(client, message):
  fun isOk() = null not in listOf(movieInfo.title, movieFiles.defaultFile, posters.defaultPoster)
  
  fun movieInfo() = MovieInfo(
    title = message.caption,
    description = message.text
  )
  
  fun movieFiles() = MovieFiles(
      defaultFile = message.video,
      additionalFiles = []
  )
  
  fun posters() = Posters(
      defaultPoster = message.video.thumb,
      additionalPosters = []
  )
  
```

By editing the `MovieAggregator` and `Movie` classes, you can change the way movies are aggregated from chats.
and for example be able to aggregate a movie from multiple messages 
(like text, photo messages before the video file), so they can be more rich and display more information.


## The chats, messages aggregators
in order to get to the movies we first have to aggregate the chats and messages.
the way its happining in the app is by using two classes that have accses to the telegram client
the two classes are `chatAggregator` and `messageAggregator`

The chats aggregator uses the client.LoadChats() function (from TDLIB) to to request chatPositionUpdates from telegram.
Then sets a updateListner for ChatPositionUpdates and emits the chatPosition to the chatsPositionUpdates flow.
a chat flow maps the chatsPositionUpdates flow to chat id and reduces duplicates.

```

updates and sets updateListner for ChatPositionUpdates
and emits the chatPosition to the chatsPositionUpdates flow.
then the chatsPostion
class ChatsAggregator(private val client: TelegramClient) {
    val chatContentAggregators = listOf(MovieChatAggregator)
    
    private val chatsPositionFlow = SharedFlow<List<ChatPosition>>()

    init {
        client.chatPositionUpdates { update ->
            val chatPosition = update.chatPosition
            val chatPositions = chatsPositionFlow.value.toMutableList()
            chatPositions.add(chatPosition)
            chatsPositionFlow.emit(chatPositions)
        }
    }
    
    fun findeChatContentAggregator(chat: Chat) {
        chatContentAggregators.find { it.isApplicable(chat) }
    }

    fun chatsFlow() = chatsPositionFlow.map { it.chatId }
        .distinctUntilChanged().map { chatId ->
            client.getChat(chatId)
        }
        
    fun chatsContentAggregators = chatsFlow.map { chat ->
            findeChatContentAggregator(chat)
        }.filterNotNull()
        
    }
    
}
```
the views tree will look like this:
```
Main:
    Home:
        ContentDash
        SearchDash
        PalyingVideoView
    ConnectionTrobule:
        NoInternet
        UnuthorizedState:
            WaitingForScane
            UnautorizedDefault
            
```

the content Dash will have function loadContent() that will load the content from the chatsContentAggregators
and save updates its content class with the new content.
and reload
```

class ContentDash(
    private val chatsContentAggregators: List<ChatContentAggregator>,
    private val content: Content
) : Dash {
    private val contentFlow = MutableStateFlow(content)
    private val contentUpdateFlow = MutableSharedFlow<ContentUpdate>()
    private val contentUpdateJob = Job()
    private val contentUpdateScope = CoroutineScope(Dispatchers.IO + contentUpdateJob)

    init {
        contentUpdateScope.launch {
            contentUpdateFlow.collect { contentUpdate ->
                val newContent = contentUpdate.updateContent(content)
                contentFlow.emit(newContent)
            }
        }
    }

    override fun loadContent() {
        chatsContentAggregators.forEach { chatContentAggregator ->
            contentUpdateScope.launch {
                chatContentAggregator.contentUpdates().collect { contentUpdate ->
                    contentUpdateFlow.emit(contentUpdate)
                }
            }
        }
    }

    override fun contentFlow() = contentFlow
}

```





The default movie aggregator displays all video files and files that end with video endings.

Explanation:

- The text introduces the project, which is an open source version of Telegram for AndroidTV, designed to focus on movies and TV series instead of messaging.
- The main view of the project displays movies in rows, and each movie is a video file in a chat. The text also mentions that users can write their custom ways to aggregate movies from chats using the `MovieAggregator` structure.
- The code provided in the text is an implementation of the `MovieAggregator` structure, which has two functions: `messagesFlow()` and `moviesFlow()`. `messagesFlow()` retrieves messages from the chat, and `moviesFlow()` filters out only the messages that contain video files.
- The text also provides a code snippet that defines the `Movie` class, which has a function `isMovie()` to check if a message is a video file.