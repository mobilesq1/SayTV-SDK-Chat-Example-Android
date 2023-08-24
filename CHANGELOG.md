# CHANGELOG
> **Note**\
> This is in reverse chronological order, so newer entries are added to the top.

### 13.0.0
- Overhauled the notification system in favour of websockets over firebase messaging
### 12.2.2
- Minor bug fixes involving links
- Extended individual ad showtime to 20s
### 12.2.0
- Changed ad campaign appearance
- Enabled ad campaign displays to be persistent until their endtime is reached
### 12.0.0
- Introduced ad campaigns to SDK
### 1.0.0-testing55
- Features fixes to quick reaction bugs reported earlier in the week
- Features method to close the currently logged in account, simply call the `closeAccount()`. method on the `SayTVSDK` object
```kotlin
lifecycle.coroutineScope.launch {
    when (val closeAccount = SayTVSdk.closeAccount()) {
        is Result.Success -> doSomething()
        else -> doSomething()
    }
}
```
- Features ads in both the header and background of chats 
### 1.0.0-alpha74
- Harmonized quiz Results Format and percentages with iOS
### 1.0.0-alpha73
- Bug fixes
### 1.0.0-alpha72
- Quiz now supports up to 4 options
### 1.0.0-alpha71
- Installed Sentry
- Fixed issue with the back button on the Avatar screen
- Moved the "liked" reaction thumb to the right
- The active users threshold can be set from the init method of the `SayTVChatView`, `SayTVChatHeaderView` and `SayTVChatFullView`. For example, the below code snippet sets the threshold to 10, active user events will not be received with active user values below 10.
```kotlin
    init(
        ..., 
        activeUserThreshold = 10
    )
```
- Attaching the below code snippet allows for the sending of SDK events to Google Analytics.
```kotlin
    class ChatActivity : AppCompatActivity(), ChatActionListener {
        override fun onCreate(savedInstanceState: Bundle?) {
            ...
            binding.chatView.commentsView.addChatActionListener(this)
        }
        ...
        override fun eventOnUserAnalyticsRequested(analyticsInformation: Map<String, Any?>) {
            val firebaseAnalytics = Firebase.analytics
            val bundle = Bundle()
            for ((key, value) in analyticsInformation) {
                bundle.putString(key, value.toString())
            }

            firebaseAnalytics.logEvent("SAYTV_CHAT_SUBSCRIPTION", bundle)
        }
    }
```
### 1.0.0-alpha70
- Subscibing to the `ChatActionListener` and `ChatHeaderActionListener` can give us access to the realtime changes of the active users count. NB, this only shows when the number is above 25.
```kotlin
class ChatActivity : AppCompatActivity(), ChatActionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.chatView.addActionListener(this)
        ...
    }

    override fun eventActiveUsers(episodeId: Int, activeUsers: Int) {
        // Do something with activeUsers count
    }
}
```
or
```kotlin
class ChatActivity : AppCompatActivity(), ChatHeaderActionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.chatHeaderView.addActionListener(this)
        ...
    }

    override fun eventActiveUsers(episodeId: Int, activeUsers: Int) {
        // Do something with activeUsers count
    }
}
```
- Decimals in quiz results have been truncated
- Sentry installed
### 1.0.0-testing30
- Fixes pending issues with the quizzes
- Can now modify the text on the "Next Chat" button with the help of a method "setErrorText()" on the chat component
### 1.0.0-testing27
- Features the new chat approach
- Clicking on a link triggers a callback that house the result(s) in a `LinkAndMetaDataPayload` object. The results can be collected like so:

```kotlin
class ChatActivity : AppCompatActivity(), DeepLinkActionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.chatView.addDeepLinkActionListener(this)
        ...
    }

    override fun eventWebLink(linkAndMetaDataPayload: LinkAndMetaDataPayload) {
        println(linkAndMetaDataPayload)
    }
}
```
- The `addActionListener()` method of the chat component has been renamed to `addChatActionListener()`.
### 1.0.0-testing25
- Link previews are enabled by default
- The chat closed page now features a button that can be tapped by the user to subscribe to the next chat. This button triggers a ChatActionListener that can be listened to like so:

```kotlin
class ChatActivity : AppCompatActivity(), ChatActionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.chatView.addActionListener(this)
        ...
    }

    override fun eventOnNextChatClicked() {
        // Do something
    }
}
```
### 1.0.0-alpha62
- Reverted changes made in 1.0.0-alpha61
### 1.0.0-alpha61
- Removed "fox"'s image from quizview
### 1.0.0-alpha60
- Fixed issues with ChatRulesDialogFragment when available device memory is low
### 1.0.0-alpha59
- Fixed: IllegalArgumentException in SayTVChatHeaderView
- Fixed: NullPointerException in SayTVQuizView
- Fixed: NullPointerException in ChatRulesDialogFragment
- Fixed: NullPointerException in GlobalContext
- Fixed: NullPointerException in SayTVChatView
### 1.0.0-alpha57
- Fixed: RuntimeException in ChatRulesDialogFragment
### 1.0.0-alpha56
- Fixed: InvocationTargetException on InAppNotificationBinding
- Fixed: NullPointerException on BaseViewModel.java
- Fixed: NoSuchMethodError in ChatRulesDialogFragment
- Fixed: NullPointerException in SayTVChatView
### 1.0.0-alpha55
- Added a new chat action listener for use in both SayTVChatView and SayTVChatHeaderView. It can be used like so:
```kotlin
class ChatActivity : AppCompatActivity(), ChatActionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.chatView.addActionListener(this)
        ...
    }

    override fun eventActiveUsers(episodeId: Int, activeUsers: Int) {
        // Do something with activeUsers count
    }
}
```
or
```kotlin
class ChatActivity : AppCompatActivity(), ChatHeaderActionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.chatHeaderView.addActionListener(this)
        ...
    }

    override fun eventActiveUsers(episodeId: Int, activeUsers: Int) {
        // Do something with activeUsers count
    }
}
```
### 1.0.0-alpha54
- Fixed alignment and design of profile error message
- Enabled customization of the ProfileBadgesActivity and SelectAvatarActivity's toolbar icon through the `setCustomProfileBadgesActivityToolBarBackIcon()` and `setCustomSelectAvatarActivityToolBarBackIcon()` methods
- Username update is realtime
- Features the new user joined behaviour
### 1.0.0-alpha52
- Enabled customization for the SayTVProfileBadges, toolbar, background, loading bar and the SayTVSelectAvatar toolbar, background and loading bar. These customization methods need to be called on the SayTVProfileView component, for example
```kotlin
binding.profileView.apply {
    setUserId()
    setCustomProfileBadgesActivityBackgroundColor(R.color.black)
    setCustomProfileBadgesActivityProgressBarColor(R.color.white)
    setCustomProfileBadgesActivityToolBarTextColor(R.color.blue)
    setCustomProfileBadgesActivityToolBarColor(R.color.red)
    setCustomProfileBadgesActivityToolBarText("Badges")
    setCustomSelectAvatarActivityBackgroundColor(R.color.black)
    setCustomSelectAvatarActivityProgressBarColor(R.color.white)
    setCustomSelectAvatarActivityToolBarTextColor(R.color.red)
    setCustomSelectAvatarActivityToolBarColor(R.color.blue)
    setCustomSelectAvatarActivityToolBarText("Avatar")
}
```
### 1.0.0-alpha50
- Enabled avatar selection by tapping on the edit button in SayTVProfileView
- Added more XML customization SayTVProfileView 
- Enabled chat rules for first time users
- To use the profile component, declare it in your xml file and then initialize programmatically using the setUserId() method. For example, to use in xml, do like so
```xml
    <io.square1.saytvsdk.app.scenes.profile.SayTVProfileView
            android:id="@+id/profile_view"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            ...>
```
   In kotlin however, you can use the setUserID method to initialize the component. It accepts two optional parameters, a userID and a language option
```kotlin
    fun setUserId(userID: Int? = null, language: String = "en")
```
   Setting the ```userID``` allows you to view the profile of the user in question, however, ignoring it shows the profile for the currently logged in user. The ```language``` parameter defaults to "en" for english
### 1.0.0-alpha49
- Fixed issues with Firebase messaging and EventManager class
- Fixed issues with overriding USER_JOINED event
### 1.0.0-alpha48
- Features better spam control
- Features better design for in-app notifications
### 1.0.0-alpha41
- Fixed the error with username changes 
- UI does not scroll when comment is liked
- Hidden comments are properly hidden and the appropriate messages shown
- Chat scrolls to bottom when filter is applied
- Quiz errors related to killing and restarting the app and multiple quizzes have been resolved
### 1.0.0-alpha40
- Fixed issue with banning users and hiding comments
- Fixed UI differences betwwen Android and iOS where the TOP and MY_ACTIVITY filters are applied
- Fixed how the UI responds to celebrity comments
- Fixed the sortting problems where comment is unliked while TOP filter is selected
### 1.0.0-alpha39
- Added the "UserNameForbidden" error to the error handler
### 1.0.0-alpha38
- This release features better error handling for network calls. The `Result.Error` class now contains better description of failed calls and are accessible through the throwable field. For example:
```kotlin
when (register) {
    is Result.Success -> doSomething()
    is Result.Error.Server -> {
        val thr = register.throwable.message
        println(thr)
    }
    else -> doNothing()
}
```
### 1.0.0-alpha37 
- The SDK initializer now accepts an extra parameter, the baseURL which defines the environment. For example, in the application class, do this:
```kotlin
class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        
        Firebase.initialize(this)?.initSayTvSdk(this, environment = SayTVSdk.Environment.STAGING.url)
    }
}
```
The SayTVSdk.Environment enum class has a url property that allows you to switch between staging and production environments. Where an environment url is not specified in the `initSayTvSdk` method, the default staging environment is used.
- Due to the changes above, you now have to specify your staging and/or production API_TOKEN when you call the `SayTVSdk.login()` or `SayTVSdk.register()` methods. These can be string files in your BuildConfig class so you can use like so:
```kotlin
lifecycle.coroutineScope.launch {
    when (val login = SayTVSdk.login(apiToken = BuildConfig.STAGING_API_TOKEN, ...)) {
        is Result.Success -> doSomething
        else -> doNothing()
    }
}

// and

lifecycle.coroutineScope.launch {
    when (val register = SayTVSdk.register(apiToken = BuildConfig.STAGING_API_TOKEN, ...)) {
        is Result.Success -> doSomething
        else -> doNothing()
    }
}
```
### 1.0.0-alpha36
- The getLoggedInUser() has been exposed and can be used like so:
```kotliSayTVSdk.register()n
lifecycle.coroutineScope.launch {
    val loggedInUser: LoggedInUser?
    loggedInUser = SayTVSdk.getLoggedInUser()
}
```
- When the TOP filter is selected and a comment is liked, the list does not automatically re-sort itself
### 1.0.0-alpha35
- Top and My Activity filters now work as expected
### 1.0.0-alpha34
- Added an extra method to customize the color of the bottom sheet close button `setCustomFiltersBottomSheetCloseButtonTextColor(color: Int)`
- Added more quiz component customizable parameters `quizOptionsButtonBackground`, `quizOptionsButtonTextColor` and `quizOptionsTextColor`
- Reverted the functionality for filters and the play-pause feature
### 1.0.0-alpha33
- Fixed bug related to customizing quiz component
- Modified like icon to look more like Zeplin's
- Added more logs to quizz component
### 1.0.0-alpha32
- The quiz component can be customized through the header component. There properties of the quiz component can be customized, `quizBackgroundDrawable` as a drawable, `quizTextColor` as a color, and `quizTimeRemainingTextColor` as a color
- Play-pause functionality doesn't interfare with that of the filters
### 1.0.0-alpha31
- To customize the filters bottom sheet, use the following methods on the `SayTVChatHeaderView`, `setCustomFiltersBottomSheetTitleTextColor(color)`, `setCustomFiltersBottomSheetBodyTextColor(color)`, and `setCustomFiltersBottomSheetBackgroundColor(color)`. For example
```kotlin
binding.headerView.setCustomFiltersBottomSheetTitleTextColor(R.color.blue)
binding.headerView.setCustomFiltersBottomSheetBodyTextColor(R.color.black)
binding.headerView.setCustomFiltersBottomSheetBackgroundColor(R.color.lime)
```
- Play services crashes have now been swallowed
- All the latest quiz related bugs have been fixed
### 1.0.0-alpha30
- The chat component is now cleared on unSubscribe()
- There's better user experience on loading
### 1.0.0-alpha29
- A major change with this release is `SayTVChatView.init(...)`,  `SayTVChatHeaderView.init(...)`, and `SayTVChatFullView.init(...)` must now be called from within a coroutine or another suspend function.
### 1.0.0-alpha28
- The unsubscribe() is now exposed through the `SayTVChatView` component. It is a suspend function that must be called within a coroutine like so:
```kotlin
launch {
    when (val unsubscribeOp = binding.chatView.unsubscribe()) {
        is Result.Success -> doSomething
        else -> doSomething
    }
}
```
- Several user update methods have now been exposed for the currently logged in user. Amongst which are `updateUserName`, `updateSocialLinks` and `updateGenderAndBirthday`. They can be used like so:
```kotlin
when (val result = SayTVSdk.updateUserName(userName)) {
    is Result.Success -> doSomething
    else -> doSomething
}

when (val result = SayTVSdk.updateSocialLinks(facebookLink, instagramLink, twitterLink)) {
    is Result.Success -> doSomething
    else -> doSomething
}

when (val result = SayTVSdk.updateGenderAndBirthday(gender = Gender.MALE, birthday = "1997-06-07")) {
    is Result.Success -> doSomething
    else -> doSomething
}
```
The specifics of use for each are present in the kDocs
- All instances of user email and name have been stipped off the SDK
- Scroll to bottom on filter switch is now enabled
- SnackBar that shows when chat initialization fails has been removed
### 1.0.0-alpha27
- `SayTVChatView` and `SayTVChatHeaderView` init methods now return `Result<Boolean>` types. They can be assigned to variables and switched on to determine the success of the init operation. For example:
```kotlin
when (val initOp = binding.chatView.init(...)) {
    is Result.Success -> doSomething
    else -> doSomething
}
```
- Fixed the size of the play/pause toggle button
- Fixed crashes and bugs with Fanzones chat
- The `Unsubscribe()` is now exposed. It is used like 
```kotlin
when (val unsubscribe = SayTVSdk.unsubscribe(episodeId)) {
    is Result.Success -> doSomething
    else -> doSomething
}
```
- Quiz button is removed
### 1.0.0-alpha26
- In app notifications now works 
- Fixed other chat items issue with using animations in landscape mode
- Release now features a logout method to be used like 
```kotlin
when (val logout = SayTVSdk.logout()) {
    is Result.Success -> doSomething
    else -> doSomething
}
```
### 1.0.0-alpha24
- Fixed chat issue with using animations in landscape mode
- Fixed issues with the handling of date parameters for fanzones
### 1.0.0-alpha20
- Fixed error with uninitialized chat
- Fixed bugs with quizzes and chat moderation
### 1.0.0-alpha19
- Bug with the order of messages has been fixed
- The keyboard ime option (tick) now sends a message
- Crash that happens with orientation change has been fixed
- Strange animation when receiving messages has been fixed
### 1.0.0-alpha18
- Bug fixes and improvements
### 1.0.0-alpha17
- Fixed issue with displayName
- Fixed the customisation for "headerBackground" in both XML and kotlin. For example, in kotlin
```kotlin
binding.chatHeaderView.customizeTheme {
    headerBackground = someDrawable
}
```
```xml
<io.square1.saytvsdk.app.scenes.header.SayTVChatHeaderView
    ...
    app:headerBackground="@drawable/someDrawable"
    ... />
```
### 1.0.0-alpha16
- The bug with message ordering has been fixed
### 1.0.0-alpha15
- The SDK now converts UTC time from the backend to local time on the device and vice versa
- All action listeners now follow a consistent naming and parameter convention
``` kotlin
eventSendChat(userId: Int, commentText: String)
eventAddFavourite(userId: Int, commentId: Long, commentText: String)
eventEnterChatroom(userId: String, episodeId: Int)
eventFirstComment(userId: Int, commentText: String)
eventTenthComment(userId: Int, commentText: String)
eventCreateQuiz(userId: Int, question: String, answer1: String, answer2: String)
eventEnterQuiz(userId: Int, question: String, answer1: String, answer2: String)
eventViewBadges(userId: Int)
```
- Fixed the blinking of the header when the create quiz dialog is dismissed
### 1.0.0-alpha14
- The `init()` methods for all components now accept an optional parameter for language as a String, eg. "en" for English, "fr" for French, etc.
- DateTime is now converted from local time to UTC when sending to backend
### 1.0.0-alpha13
- Minor bug fixes and improvements
### 1.0.0-alpha12
- To start a timeless fanzone chat, pass in the `fanzone` flag as a boolean `true` to any of the `SayTVChatFullView`, `SayTVChatView` or `SayTVChatHeaderView` init methods. Null values can be passed for startDate and endDate. For example:
```kotlin
binding.sayTVChatFullView.init(
    chatId: chatId,
    chatName: chatName,
    chatImage: chatImage,
    startDate: null,
    endDate: null,
    isFanzone: true
)
```
- To use the `SayTVChatHeaderView` as a component without subscribing to a chat, use the `initWithoutSubscribeDynamic` function in place of the `init` function to initialize. For example
```kotlin
binding.sayTVChatHeaderView.initWithoutSubscribeDynamic(
    episodeId: episodeId,
    chatName: chatName,
    chatImage: chatImage,
    startDate: startDate,
    endDate: endDate,
)
```
### 1.0.0-alpha11
- You can specify a custom name for the application as opposed to the default "SayTV". For example, you can initialize the SDK like so:
```kotlin
FirebaseApp.initializeApp(this)?.initSayTvSdk(this, "preferredName")
```
- Quizzes can be created and auto accepted without the need for an admin to endorse.
- In chatrooms, admins appear differently from everyone else
- SDK now features more animations than previous versions
- Poke funtionality have been removed
- When the `<io.square1.saytvsdk.app.scenes.SayTVChatFullView>` component is used, the chat header can be hidden when the soft keyboard is visible
### 1.0.0-alpha10
- Fixed bugs with chat action listeners
### 1.0.0-alpha09
- Sparse array bug fixes and improvements
### 1.0.0-alpha08
- Updated with the new security feature
- Updated with the new Firebase configuration
- Updated with a few cosmetic changes like colors for mentions, better time formatting, etc. 
### 1.0.0-alpha07
- Minor bug fixes and improvements
### 1.0.0-alpha06
- `SayTVSdk.register()` and `SayTVSdk.login()` now mandatorily accept an extra parameter `apiToken`. This secure token can be generated from the `Developer` section of the admin application.

For example:
```kotlin
SayTVSdk.register(
    apiToken = apiToken,
    digicelId = digicelId,
    username = username,
    email = email,
    avatar = avatar
)
```
and 
```kotlin
SayTVSdk.login(
    apiToken = apiToken,
    digicelId = digicelId
)
```
### 1.0.0-alpha05
- Minor bug fixes and improvements
### 1.0.0-alpha04

- `SayTVChatView` and `SayTVChatHeaderView` have moved to new packages
```kotlin
io.square1.saytvsdk.app.scenes.chat.SayTVChatView
io.square1.saytvsdk.app.scenes.header.SayTVChatHeaderView
```
- Added a new way of doing view customization
    - One can now use the new `.customizeTheme` helper method to edit the current theme at runtime 
      or set an all together new theme object that will override the whole view theme.
      
    Disclaimer: This feature is subject to change and more params are to be added 
  
    For example:
```kotlin
binding.chatView.theme = object : ChatTheme {
    override var chatBackground: Drawable? = ...
    override var chatMessageBackgroundColor: Int = ...
    override var chatEventItemBackgroundColor: Int = ...
    override var quizResultItemBackgroundColor: Int = ...
    override var messageInputBackground: Drawable? = ...
    override var mentionsBackground: Drawable? = ...
    override var pokeFriendButtonBackground: Drawable? = ...
    override var shareWithFriendsButtonBackground: Drawable? = ...
}

// ---- OR ---

binding.chatView.customizeTheme {
    chatBackground = ...
    chatMessageBackgroundColor = ...
    chatEventItemBackgroundColor = ...
    quizResultItemBackgroundColor = ...
    messageInputBackground = ...
    mentionsBackground = ...
    pokeFriendButtonBackground = ...
    shareWithFriendsButtonBackground = ...
}
```
- Added an `view.isOverlay` parameter that allows to display the SayTV components over videos

```kotlin
binding.chatView.isOverlay = true
```
- Added a `ChatActionListener` which is one of several ActionListener interfaces to allow for action subscription and analytics.
  
  It can be used like so:
```kotlin
class ChatActivity : AppCompatActivity(), ChatActionListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        ...
        binding.chatView.addActionListener(this)
        ...
    }

    override fun onNewCommentSent(comment: Comment) {
        // Do something with sent comment
    }
}
```
- You can send and like messages
- `SayTVProfileView` can now display user badges
- Quiz creation and participation is now fully functional within `SayTVChatHeaderView`
- Mentions now work within the `SayTVChatView` component using `@`
