# Saytv Chat Example Android
App that is going to use the Saytv Chat library.

Table of contents
- [What's New](#whats-new) 
- [Prerequisites](#prerequisites) 
- [Installation](#chat-sdk)
- [Initialization](#initialization)
- [Register](#register)
- [Login](#login)
- [Logout](#logout)
- [ChatView](#chat-view)
- [Header](#header)
- [Dummy header](#dummy-header)
- [Full Chat](#full-chat)
- [Get active users](#get-active-users)
- [Profile](#profile)
- [Customization and Theming](#customization-and-theming)
- [Options](#options)
- [Known Issues](#known-issues)
- [Troubleshooting](#troubleshooting)

## What's New
### 1.0.0-alpha74
- Harmonized quiz Results Format and percentages with iOS

Check older versions on the [CHANGELOG](CHANGELOG.md)

## Prerequisites
1. Create a Jitpack account and generate an authentication token.
2. In the `gradle.properties` replace string `<PROVIDED JITPACK AUTH TOKEN>` with the provided
    Jitpack.io authentication token

## Installation

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
implementation 'org.bitbucket.square1.saytv_sdk_android:release:1.0.0-alpha74'
```
4. You're all set!

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

```kotlin
lifecycle.coroutineScope.launch {
    when (val register = SayTVSdk.register(
            apiToken = ..., 
            digicelId = ...,
            username = ...,
            avatar = ...,
        )
    ) {
        is Result.Success -> doSomething()
        else -> doSomething()
    }
}
```

## Login

Call this only after the `SayTVSdk.init()` method, given an account has already been created. 
Doing so an authentication token will be generated and cached by the `SayTVSdk`
in order for the `SayTVChatView` to work properly

```kotlin
lifecycle.coroutineScope.launch {
    when (val login = SayTVSdk.login(apiToken = ..., digicelId = ...)) {
        is Result.Success -> doSomething()
        else -> doSomething()
    }
}
```

## Logout

To logout inside the SDK, call `SayTvSdk.logout()` like below 

``` Kotlin
when (val logout = SayTVSdk.logout()) {
    is Result.Success -> doSomething()
    else -> doSomething()
}
```

## ChatView

This is an Android View containing the chat itself. For this view to work properly, 2 things must be met first:
1. `SayTVSdk.init()` has been called and completed successfully 
2. `SayTVSdk.login` or `SayTVSdk.register` has been called and completed successfully

You can add the `SayTVChatView` component to your XML file and give it an ID you can reference later on in code, for example

```xml
 <io.square1.saytvsdk.app.scenes.SayTVChatView
        android:id="@+id/chat_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

In your Kotlin file, initialize the component by calling SayTVChatView.init(...) with its relevant parameters, for example

```kotlin
binding.chatView.init(
    chatId = ...,
    chatName = ...,
    chatImage = ...,
    startDate = ...,
    endDate = ...,
    isFanzone = true,
    shouldAutoUnsubscribe = true,
    shouldDisplayQuizzes = false,
    displayButtonBar = true,
    language = "fr"
)
```

You can further customize the `SayTVChatView` by directly calling custom methods, for example

```kotlin
binding.chatView.apply {
    setCustomFiltersBottomSheetBackgroundColor(R.color.black)
    setCustomFiltersBottomSheetBodyTextColor(io.square1.saytvsdk.R.color.lime)
    setCustomFiltersBottomSheetTitleTextColor(R.color.red)
    setCustomFiltersBottomSheetCloseButtonTextColor(R.color.red)
}
```

Or you can customize by using the customizeTheme method on the `SayTVChatView` component. For example

```kotlin
binding.chatView.customizeTheme {
    quizTextColor = ContextCompat.getColor(this@ChatActivity, R.color.red)
    quizOptionsTextColor = ContextCompat.getColor(this@ChatActivity, R.color.red)
    quizOptionsButtonTextColor = ContextCompat.getColor(this@ChatActivity, R.color.red)
    quizBackgroundDrawable = resources.getDrawable(R.drawable.test_drawable)
}
```

For more on customizations, see the [customization section](#customization-and-theming)

When initialising a chat component SDK automatically subscribes user to the chat events. While subscription is done automatically by the SDK, you need to handle unsubscribe manually if the `shouldAutoUnsubscribe` attribute of the `SayTVChatView` is set to false. You can unsubscribe from a chat by calling the `unsubscribe()` method off the `SayTVChatView` component:

```kotlin
launch {
    when (val unsubscribeOp = binding.chatView.unsubscribe()) {
        is Result.Success -> doSomething()
        else -> doSomething()
    }
}
```

Yet there are some exceptions where SDK also unsubscribe user automatically from the chat: - when user has been banned from the chat by admin.

There could be only one active chat at a time in the SDK. If the new chat is created (different chatId) without unsubscribing from old chat then unsubscribe from old chat will be performed automatically before subscribing to a new chat. This provides possibility to control subscribe status when recreating chat component in different scenarios so the SDK can store all chat data between subscribe and unsubscribe so if chat is multiple time recreated (for example on device orientation change) all the data of the chat are up to date and are removed only when unsubscribe is called. This improves data synchronisation when recreating the chat without unsubscribing.

## Header

This is an Android View containing the chat header. For this view to work properly, 2 things must be met first:
1. `SayTVSdk.init()` has been called and completed successfully 
2. `SayTVSdk.login` or `SayTVSdk.register` has been called and completed successfully

You can add the `SayTVChatHeaderView` component to your XML file and give it an ID you can reference later on in code, for example

```xml
 <io.square1.saytvsdk.app.scenes.SayTVChatHeaderView
        android:id="@+id/chat_header_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

In your Kotlin file, initialize the component by calling SayTVChatHeaderView.init(...) with its relevant parameters, for example

```kotlin
binding.chatHeaderView.init(
    chatId = ...,
    chatName = ...,
    chatImage = ...,
    startDate = ...,
    endDate = ...,
    isFanzone = true,
    shouldAutoUnsubscribe = true,
    shouldDisplayQuizzes = false,
    displayButtonBar = true,
    language = "fr"
)
```

You can further customize the `SayTVChatHeaderView` by directly calling custom methods, for example

```kotlin
binding.chatHeaderView.apply {
    setExpirationTimeTextNormalColorValue(R.color.red)
    setExpirationTimeTextExpiringColorValue(R.color.teal_700)
}
```

Or you can customize by using the customizeTheme method on the `SayTVChatHeaderView` component. For example

```kotlin
binding.chatHeaderView.customizeTheme {
    quizTextColor = ContextCompat.getColor(this@ChatActivity, R.color.red)
    quizOptionsTextColor = ContextCompat.getColor(this@ChatActivity, R.color.red)
    quizOptionsButtonTextColor = ContextCompat.getColor(this@ChatActivity, R.color.red)
    quizBackgroundDrawable = resources.getDrawable(R.drawable.test_drawable)
}
```

For more on customizations, see the [customization section](#customization-and-theming)

## Dummy Header

This is an Android View containing the chat header but without the ability to subscribe dynamically to a chat or make any service calls, this view acts as a sort of dummy for a header. For this view to work properly, 2 things must be met first:
1. `SayTVSdk.init()` has been called and completed successfully 
2. `SayTVSdk.login` or `SayTVSdk.register` has been called and completed successfully

You can add the `SayTVChatHeaderView` component to your XML file and give it an ID you can reference later on in code, for example

```xml
 <io.square1.saytvsdk.app.scenes.SayTVChatHeaderView
        android:id="@+id/dummy_chat_header_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

In your Kotlin file, initialize the component by calling SayTVChatHeaderView.initWithoutSubscribeDynamic(...) with its relevant parameters, for example

```kotlin
binding.dummyChatHeaderView.initWithoutSubscribeDynamic(
    chatId = ...,
    chatName = ...,
    chatImage = ...,
    startDate = ...,
    endDate = ...,
)
```
Customization for this is the same as customization for a [header](#header)

## Full Chat

This is an Android View containing the chatView and chatHeaderView in one component. For this view to work properly, 2 things must be met first:
1. `SayTVSdk.init()` has been called and completed successfully 
2. `SayTVSdk.login` or `SayTVSdk.register` has been called and completed successfully

You can add the `SayTVChatFullView` component to your XML file and give it an ID you can reference later on in code, for example

```xml
 <io.square1.saytvsdk.app.scenes.SayTVChatFullView
        android:id="@+id/full_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
```

In your Kotlin file, initialize the component by calling SayTVChatFullView.init(...) with its relevant parameters, for example

```kotlin
binding.fullView.init(
    chatId = ...,
    chatName = ...,
    chatImage = ...,
    startDate = ...,
    endDate = ...,
    isFanzone = true,
    shouldAutoUnsubscribe = true,
    shouldDisplayQuizzes = false,
    displayButtonBar = true,
    language = "fr"
)
```

Since the fullView is a merger of the header and chatView, you can customize the fullView by customizing the headerView and chatView independently, for example

```kotlin
binding.fullView.headerView.customizeTheme {
    ...
}

binding.fullView.commentsView.customizeTheme {
    ...
}
```

## Get active users
Get the active users in as many chat rooms as you need. Simply pass the externalIDs of the chat in a list to the `getActiveUsersFor()` method as seen below. The result on success is a list containing objects with the information

```kotlin
launch {
    when (val activeUsers = SayTVSDK.getActiveUsersFor(listOf("", "", ""))) {
        is Result.Success -> doSomething()
        else -> doSomething()
    }
}
```

## Profile
This is an Android View containing the profileView. For this view to work properly, 2 things must be met first:
1. `SayTVSdk.init()` has been called and completed successfully 
2. `SayTVSdk.login` or `SayTVSdk.register` has been called and completed successfully

You can add the `SayTVProfileView` component to your XML file and give it an ID you can reference later on in code, for example

```kotlin
<io.square1.saytvsdk.app.scenes.profile.SayTVProfileView
    android:id="@+id/profile_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    ...>
```
In your Kotlin file, initialize the component by calling SayTVProfileView.setUserId(...) with its relevant parameters, for example

```kotlin
binding.profileView.setUserId(
    userID = ...,
    language = ...
)
```

NB: Both `userID` and `language` are optional, where a userID is passed, the profile information for the relevant user is collected, where nothing is passed, the profile information for the currently logged in user is collected.

You can further customize the `SayTVProfileView` by directly calling custom methods, for example

```kotlin
binding.profileView.apply {
    setCustomProfileBadgesActivityBackgroundColor(R.color.black)
    setCustomProfileBadgesActivityProgressBarColor(io.square1.saytvsdk.R.color.lime)
    setCustomProfileBadgesActivityToolBarColor(R.color.red)
    setCustomProfileBadgesActivityToolBarTextColor(R.color.red)
}
```

Or you can customize by using the customizeTheme method on the `SayTVProfileView` component. For example h

```kotlin
binding.profileView.customizeTheme {
    profileBackgroundColor = ContextCompat.getColor(this@ChatActivity, R.color.red)
    nameTextColor = ContextCompat.getColor(this@ChatActivity, R.color.red)
    memberSinceTextColor = ContextCompat.getColor(this@ChatActivity, R.color.red)
}
```

For more on customizations, see the [customization section](#customization-and-theming)


## Customization and Theming

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
    nextChatButtonTextColor = someInt
    nextChatButtonBackground = someInt
    nextChatMessageTextColor = someInt
    nextChatTitleTextColor = someInt
    quizTextColor = someInt
    quizTitleTextColor = someInt
    quizTitleBackground = someDrawable
    quizOptionsTextColor = someInt
    quizXButtonTintColor = someInt
    quizCollapseButtonTintColor = someInt
    quizOptionsButtonTextColor = someInt
    quizBackgroundDrawable = someDrawable
    quizExpirationTimeBackground = someDrawable
    quizOptionsButtonBackground = someDrawable
    quizOptionProgressBarNormalColor = someInt
    quizOptionProgressBarFilledColor = someInt
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
    timeRemainingTextColor = someInt
    quizTextColor = someInt
    quizTitleTextColor = someInt
    quizTitleBackground = someDrawable
    quizOptionsTextColor = someInt
    quizXButtonTintColor = someInt
    quizCollapseButtonTintColor = someInt
    quizOptionsButtonTextColor = someInt
    quizBackgroundDrawable = someDrawable
    quizExpirationTimeBackground = someDrawable
    quizOptionsButtonBackground = someDrawable
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
    quizOptionProgressBarNormalColor = someInt
    quizOptionProgressBarFilledColor = someInt
}

binding.profileView.customizeTheme {
    rootContainerBackgroundColor = someInt
    nameTextColor = someInt
    memberSinceTextColor= someInt
    profileBadgesBackground = someDrawable
    profileInfoLayoutBackground = someDrawable
    seeAllEarnedBadgesBackground = someDrawable
    seeAllEarnedBadgesBackground = someDrawable
    progressBarColor = someInt
}
```

Components have several methods that can be used to customize different other elements not customizable by the theme. Use this section of the documentation to refer to them.

`SayTVChatView`
```kotlin
    /**
     * Set custom text color for the filter bottom sheet title
     * @param color Custom color resource
     */
    fun setCustomFiltersBottomSheetTitleTextColor(color: Int?)
    
    /**
     * Set custom text color for the filter bottom sheet
     * @param color Custom color resource
     */
    fun setCustomFiltersBottomSheetBodyTextColor(color: Int?)
    
    /**
     * Set custom background color for the filter bottom sheet
     * @param color Custom color resource
     */
    fun setCustomFiltersBottomSheetBackgroundColor(color: Int?)

    /**
     * Set custom text color for the filter bottom sheet close button
     * @param color Custom color resource
     */
    fun setCustomFiltersBottomSheetCloseButtonTextColor(color: Int?)

    /**
     * Set custom text color for the chat rules fragment
     * @param color Custom color resource
     */
    fun setCustomChatRulesTextColor(color: Int?)

    /**
     * Set custom text color for the chat rules button
     * @param color Custom color resource
     */
    fun setCustomChatRulesButtonTextColor(color: Int?)

    /**
     * Set custom background color for the chat rules button
     * @param color Custom color resource
     */
    fun setCustomChatRulesButtonColor(color: Int?)

    /**
     * Set custom background drawable for the chat rules fragment
     * @param drawable Custom drawable resource
     */
    fun setCustomChatRulesBackgroundDrawable(drawable: Int?)
    
    /**
     * Set custom background drawable for the empty chat button
     * @param drawable Custom drawable resource
     */
    fun setCustomEmptyChatButtonBackgroundDrawable(drawable: Int)

    /**
     * Set the custom text of the error button
     * @param text Custom text
     */
    fun setErrorText(text: String)
    
    /**
     * Set the custom text of the error view
     * @param text Custom text
     */
    fun setErrorMessageText(text: String)

    /**
     * Set the color for the quiz time remaining in normal mode
     * @param color The color resource for the quiz time remaining in normal mode
     */
    fun setExpirationTimeTextNormalColorValue(color: Int)

    /**
     * Set the color for the quiz time remaining in expiring mode
     * @param color The color resource for the quiz time remaining in expiring mode
     */
    fun setExpirationTimeTextExpiringColorValue(color: Int)
```

`SayTVChatHeaderView`
```kotlin
    /**
     * Set the color for the quiz time remaining in normal mode
     * @param color The color resource for the quiz time remaining in normal mode
     */
    fun setExpirationTimeTextNormalColorValue(color: Int)

    /**
     * Set the color for the quiz time remaining in expiring mode
     * @param color The color resource for the quiz time remaining in expiring mode
     */
    fun setExpirationTimeTextExpiringColorValue(color: Int)
```

`SayTVProfileView`
```kotlin
    fun setCustomProfileBadgesActivityBackgroundColor(color: Int?)

    fun setCustomProfileBadgesActivityProgressBarColor(color: Int?)

    fun setCustomProfileBadgesActivityToolBarColor(color: Int?)

    fun setCustomProfileBadgesActivityToolBarTextColor(color: Int?)

    fun setCustomProfileBadgesActivityToolBarText(text: String?)

    fun setCustomProfileBadgesActivityToolBarBackIcon(drawable: Int?)

    fun setCustomSelectAvatarActivityBackgroundColor(color: Int?)

    fun setCustomSelectAvatarActivityProgressBarColor(color: Int?)

    fun setCustomSelectAvatarActivityToolBarColor(color: Int?)

    fun setCustomSelectAvatarActivityToolBarTextColor(color: Int?)

    fun setCustomSelectAvatarActivityToolBarText(text: String?)

    fun setCustomSelectAvatarActivityToolBarBackIcon(drawable: Int?)
```
## Options

## Known Issues

## Troubleshooting

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
    
