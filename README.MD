# Summary

## Structure

### `:common`

This is an Android gradle module that contains classes and resources that can be shared between the 
two apps below such as the common screens `MainActivity` and `ChatActivity`

### `:sample:no-firebase`

This is a sample app demonstrating simply using the SDK in a common environment where the app
integrating this SDK is not using Firebase Messaging services

### `:sample:firebase`

This is a sample demonstrating using the SDK when it has to be coupled with an already existing
Firebase implementation.

For this the project is using a 
[mock-google-services.json](https://github.com/firebase/quickstart-android/blob/master/mock-google-services.json)
from the `quickstart-android` Firebase repository. **Please don't' forget to replace it with
your own `google-services.json` file if you want it to work alongside your Firebase services**.

## Prerequisites
1. In the `gradle.properties` replace string `<PROVIDED JITPACK AUTH TOKEN>` with the provided
    Jitpack.io authentication token
2. For the `:sample:firebase` app to work, create a `google-services.json` file with the 
package name: `io.square1.saytvsdk.sample.firebase`. The existing one is there for the
compilation process to succeed. It's a mock file and it's not tied to any Firebase project.
[See :sample:firebase section](#samplefirebase) 


# SDK
## What's new
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

## How to install

1. In the `gradle.properties` of your app, add the following line:
```
JITPACK_AUTH_TOKEN=<PROVIDED JITPACK AUTH TOKEN>
```
where the `<PROVIDED JITPACK AUTH TOKEN>` is the Jitpack Authentication token you have been provided.
Example: See [gradle.properties](gradle.properties) at line 24
2. In your `settings.gradle` file, in the `dependencyResolutionManagement` block, add the following
Jitpack repository and sync your project. See [settings.gradle](settings.gradle)
```groovy
 maven {
    url 'https://www.jitpack.io'
    credentials { username JITPACK_AUTH_TOKEN }
}
 ```
3. In your library or app `build.gradle` file add the following dependency:
```
implementation 'org.bitbucket.square1.saytv_sdk_android:release:1.0.0-alpha59'
```
4. You're all set!

# Using the SDK

## Initialization

Before any operation, the SDK must be initialized, this is done mainly through the
`SayTVSdk.init()` method and can be achieved in 2 ways:
1. Using our helper components that take care of initialization, automatically (OPTIONAL)
2. Using your own custom `Application` and `FirebaseMessagingService` implementations 

### I. Initialization - Using the helper components (OPTIONAL)

- `SayTVApplication` -  Helper class to handle the SDK initialization. Extend this
class or simply declare it in your `AndroidManifest.xml` and the SDK initialization 
will be handled automatically. To listen to it's result one could override
`onSayTvSdkInitCompleted()` or subscribe to `SayTVSdk.initResultLiveData` events.

  **Extending and using this class is optional. Please refer to section
  [II. Initialization - Custom implementation](#ii-initialization---custom-implementation)
  for a more custom approach**

- `SayTVFirebaseService`- Helper class to automatically delegate the Firebase events to the SDK. 
Extend this class or simply declare it in your `AndroidManifest.xml`
and the component will automatically forward the Firebase events to the SDK.
    
  **Extending and using this class is optional. Please refer to section
  [II. Initialization - Custom implementation](#ii-initialization---custom-implementation)
  for a more custom approach**

Please reference to  `:sample:no-firebase` for an example on how to use the helper components.
Please notice that you still have to add the `io.square1.saytvsdk.SayTVApplication` and 
`io.square1.saytvsdk.SayTVFirebaseService` (or their correspondent children classes when extending 
them) in your `AndroidManifest` in order for it to properly work, like so:

```xml
<application
    android:name="io.square1.saytvsdk.SayTVApplication"
    ... >
...
    <service
        android:name="io.square1.saytvsdk.SayTVFirebaseService"
        android:exported="false">
        <intent-filter>
            <action android:name="com.google.firebase.MESSAGING_EVENT" />
        </intent-filter>
    </service>

</application>
```

### II. Initialization - Custom implementation

If you already have existing `Application` or `FirebaseMessagingService` components and you don't
need to use the helper ones from section
[I. Initialization - Using the helper components](#i-initialization---using-the-helper-components-optional)
then to integrate with our SDK you'll have to:
1. If you don't have an `Application` class already, create one, declare it in your 
`AndroidManifest.xml`, and in your `onCreate()` method call `SayTVSdk.init()`. In the
`SayTVSdk.init()` `onCompleted()` parameter function, you'll be notified when the 
initialization has completed. See `:sample:firebase` for a working example.
```kotlin
class SampleApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SayTVSdk.init(this) {
            when (it) {
                is Result.Success -> logcat { "SayTVSdk initialized successfully" }
                is Result.Error.Firebase -> logcat(Log.ERROR, it.throwable) { "SayTVSdk failed to initialize:" }
            }
        }
    }

}
```

2. If you don't have a `FirebaseMessagingService`, create one, declare it in your
`AndroidManifest.xml` and in the `onNewToken(String)` method call
`SayTVSdk.firebaseDelegate.onNewToken(token)`, also in your `onMessageReceived(message)` method call SayTVSdk.firebaseDelegate.onMessageReceived(message).  See `:sample:firebase` for a working example.
```kotlin
class SampleFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        SayTVSdk.firebaseDelegate.onNewToken(token)
    }
    
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        SayTVSdk.firebaseDelegate.onMessageReceived(message)
    }

}
```
Don't forget to declare them in your `AndroidManifest.xml` file:
```xml
<application
    android:name=".SampleApp"
    ... >
...
    <service
        android:name=".SampleFirebaseMessagingService"
        android:exported="false">
        <intent-filter>
            <action android:name="com.google.firebase.MESSAGING_EVENT" />
        </intent-filter>
    </service>

</application>
```

## Register

To create an account the `SayTVSdk.register()` method must be used with the proper params
only after `SayTVSdk.init()` has completed. Doing so an authentication token will be generated and cached 
by the `SayTVSdk` in order for the `SayTVChatView` to work properly
Please keep in mind that the avatar `Uri` param has to be an URL. This method **does not** upload an image!

## Login

Call this only after the `SayTVSdk.init()` method, given an account has already been created. 
Doing so an authentication token will be generated and cached by the `SayTVSdk`
in order for the `SayTVChatView` to work properly

## ChatView

An Android View containing the chat itself. For this view to work properly, 3 things must be met first:
1. `SayTVSdk.init()` has been called and completed successfully 
2. `SayTVSdk.login` or `SayTVSdk.register` has been called and completed successfully
3. `SayTVChatView.init()` has been called with the proper parameters, see `SayTVChatView.init()`

You are now able to integrate and use our `SayTVChatView` in your layouts. See `ChatActivity` for a 
working example.

```xml
 <io.square1.saytvsdk.app.scenes.SayTVChatView
        android:id="@+id/chat_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

## Customization

Several parameters are available for theming `SayTVChatView` xml layout  as shown below
```xml
<io.square1.saytvsdk.app.scenes.SayTVChatView
    android:id="@+id/chat_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    app:chatBackground="@drawable/some_drawable"
    app:headerBackground="@color/some_color"
    app:headerTextColor="@color/some_color"
    app:viewerCountIcon="@drawable/some_drawable"
    app:viewerCountTextColor="@color/some_color"
    app:viewerCountBackground="@drawable/some_drawable"
    app:tvShowProgressBarDrawable="@drawable/some_drawable"
    app:settingsFilterBackground="@drawable/some_drawable"
    app:settingsPauseBackground="@drawable/some_drawable"
    app:settingsWallpaperBackground="@drawable/some_drawable"
    app:settingsQuizBackground="@drawable/some_drawable"
    app:settingsHashtagBackground="@drawable/some_drawable"
    app:settingsFilterIcon="@drawable/some_drawable"
    app:settingsPauseIcon="@drawable/some_drawable"
    app:settingsWallpaperIcon="@drawable/some_drawable"
    app:settingsQuizIcon="@drawable/some_drawable"
    app:settingsHashtagIcon="@drawable/some_drawable"
    app:settingsFilterTextColor="@color/some_color"
    app:settingsQuizTextColor="@color/some_color"
    app:timeRemainingTextColor="@color/some_color"
    app:timeRemainingIcon="@drawable/some_drawable"
    app:timeIntervalTextColor="@color/some_color"
    app:timeIntervalIcon="@drawable/some_drawable"
    app:createChatBackground="@drawable/some_drawable"
    app:storeIcon="@drawable/some_drawable"
/>
```
Explanation: 

- `app:chatBackground` - Sets the background for the entire chat view as a drawable
- `app:headerBackground` - Sets the background for the header view as a color
- `app:headerTextColor` - Sets the text color for the header text as a color
- `app:viewerCountIcon` - Sets the image/icon for the number of viewers as a drawable
- `app:viewerCountTextColor` - Sets the text color for the number of viewers as a color
- `app:viewerCountBackground` - Sets the background for the number of viewers as a drawable
- `app:tvShowProgressBarDrawable` - Sets the background for the show progress as a drawable
- `app:settingsFilterBackground` - Sets the background for the filter button as a drawable
- `app:settingsPauseBackground` - Sets the background for the pause button as a drawable
- `app:settingsWallpaperBackground` - Sets the background for the wallpaper button as a drawable
- `app:settingsQuizBackground` - Sets the background for the quiz button as a drawable
- `app:settingsHashtagBackground` - Sets the background for the hashtag button as a drawable
- `app:settingsFilterIcon` - Sets the image/icon for the filter button as a drawable
- `app:settingsPauseIcon` - Sets the image/icon for the pause button as a drawable
- `app:settingsWallpaperIcon` - Sets the image/icon for the wallpaper button as a drawable
- `app:settingsQuizIcon` - Sets the image/icon for the quiz button as a drawable
- `app:settingsHashtagIcon` - Sets the image/icon for the hashtag button as a drawable
- `app:settingsFilterTextColor` - Sets the text color for the filter button as a color
- `app:settingsQuizTextColor` - Sets the text color for the quiz button as a color
- `app:timeRemainingTextColor` - Sets the text color for time remaining as a color
- `app:timeRemainingIcon` - Sets the image/icon for time remaining as a drawable
- `app:timeIntervalTextColor` - Sets the text color for time interval as a color
- `app:timeIntervalIcon` - Sets the image/icon for time interval as a drawable
- `app:createChatBackground` - Sets the background for the chat edit text as a drawable
- `app:storeIcon` - Sets the image/icon for the store button as a drawable

Also, attributes can be customised using the component theme(s). `SayTVChatView`, `SayTVChatHeaderView` and `SayTVProfileView` can all be customised using their themes. For example,

```kotlin
binding.chatView.customizeTheme {
    chatBackground = someDrawable
    chatMessageBackgroundColor = someInt
    chatEventItemBackgroundColor = someInt
    quizResultItemBackgroundColor = someInt
    chatHintColor = someInt
    chatTextColor = someInt
    messageInputBackground = someDrawable
    mentionsBackground = someDrawable
}

binding.chatHeaderView.customizeTheme {
    chatBackgroundColor = someInt
    cardBackground = someDrawable
    cardBackgroundColor = someInt
    cardElevation = someFloat
    viewersCounterTextColor = someInt
    viewersCounterIcon = someDrawable
    headerDescriptionTextColor = someInt
    filtersTextColor = someInt
    quizTextColor = someInt
    timeRemainingTextColor = someInt
    programTimeTextColor = someInt
    progressBarForegroundColor = someInt
    progressBarBackgroundColor = someInt
    viewerCounterBackground = someDrawable
    settingsFilterBackground = someDrawable
    settingsPauseBackground = someDrawable
    settingsWallpaperBackground = someDrawable
    settingsQuizBackground = someDrawable
    settingsHashtagBackground = someDrawable
    tvShowProgressBarDrawable = someDrawable
}

binding.profileView.customizeTheme {
    rootContainerBackgroundColor = someInt
    nameTextColor = someInt
    memberSinceTextColor= someInt
    profileBadgesBackground = someDrawable
    profileInfoLayoutBackground = someDrawable
    seeAllEarnedBadgesBackground = someDrawable
}
```

# Troubleshooting

1. Gradle Sync -> Jitpack.io 403 response:

   ```
    Could not GET 'https://www.jitpack.io/org/bitbucket/square1/saytv_sdk_android/release/1.0.0-alpha01/release-1.0.0-alpha01.pom'. Received status code 403 from server: Forbidden****
   ```

    Solution: Please check your `JITPACK_AUTH_TOKEN` variable inside `gradle.properties` that has been
    set correctly
2. Chat doesn't load
   
   Solution: Please make sure the SDK initialized correctly and that you called `register` or `login` 
   methods
3. We are not receiving messages - //TODO :)

4. `SayException.IllegalState` upon registering or logging in
   
    Solution: Please make sure the SDK initialized correctly. If you are using our Helper Components,
    please make sure you have correctly declared them in your `AndroidManifest.xml`
    
5.  Calling the `SayTVChatView` init method on a closed chat will result in an error. If a chat is closed, you need to either reopen it in the Admin panel or start a new chat with an unused externalID. NB: Atttempting to modify the start and end time on a closed chat and reentering the chat will result in an error 
    
