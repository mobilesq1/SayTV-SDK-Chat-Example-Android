# Summary

## Structure

### `:common`

This is an Android gradle module that contains classes and resources that can be shared between the 
two apps below.

### `:sample:no-firebase`

This is a sample app demonstrating simply using the SDK in a common environment where the app
integrating this SDK is not using Firebase Messaging services

### `:sample:firebase`

This is a sample demonstrating using the SDK when it has to be coupled with an already existing
firebase implementation.

For this the project is using a 
[mock-google-services.json](https://github.com/firebase/quickstart-android/blob/master/mock-google-services.json)
from the `quickstart-android` Firebase repository. **Please don't' forget to replace it with
your own `google-services.json` file if you want it to work as it is**.

## Prerequisites
1. In the `gradle.properties` replace string `<PROVIDED JITPACK AUTH TOKEN>` with the provided
    Jitpack.io authentication token
2. For the `:sample:firebase` app to work, create a `google-services.json` file with the 
package name: `io.square1.saytvsdk.sample.firebase`. The existing one is there for the
compilation process to succeed. It's a mock file and it's not tied to any Firebase project.
[See :sample:firebase section](#samplefirebase) 


# SDK

## How to install

1. In the `gradle.properties` of your app, add the following line:
`JITPACK_AUTH_TOKEN=<PROVIDED JITPACK AUTH TOKEN>`
where the `<PROVIDED JITPACK AUTH TOKEN>` is the Jitpack Authentication token you have been provided.
Example: See [gradle.properties](gradle.properties) at line 24
2. In your `settings.gradle` file, in the `dependencyResolutionManagement` block, add the following
Jitpack repository and sync your project:
```groovy
 maven {
    url 'https://www.jitpack.io'
    credentials { username JITPACK_AUTH_TOKEN }
}
 ```
3. In your library or app `build.gradle` file add the following dependency:
```
implementation 'org.bitbucket.square1.saytv_sdk_android:release:1.0.0-alpha01'
```
4. You're all set!

## Using the SDK

## Initialization

Before any operation, the SDK must be initialized, this is done mainly through the
`SayTVSDK.init()` method and can be achieved in 2 ways:
1. Using our helper components that take care of initialization, automatically (OPTIONAL)
2. Using your own custom `Application` and `FirebaseMessagingService` implementations 

#### Using the helper components (OPTIONAL)

- `SayTVApplication`
- `SayTVFirebaseService`


If using the helper components, please notice in `:sample:no-firebase` that you still have to add
the `io.square1.saytvsdk.SayTVFirebaseService` in your `AndroidManifest` in order for it to properly
work.

#### Custom implementation

## Register

## Login

## ChatView

## Customization

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
