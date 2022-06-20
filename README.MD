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
implementation 'org.bitbucket.square1.saytv_sdk_android:release:1.0.0-alpha05'
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
`SayTVSdk.firebaseDelegate.onNewToken(token)`.  See `:sample:firebase` for a working example.
```kotlin
class SampleFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        SayTVSdk.firebaseDelegate.onNewToken(token)
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
    
