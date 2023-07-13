# Device Management Admin Portal

The Device Management Admin Portal is an Android application built using the MVVM (Model-View-ViewModel) architecture. 
It enables administrators to manage and monitor devices in an organization, with data synchronization between Room Database and Firestore. 
The application supports both online and offline modes.

## Features

- Login: Only administrators can log in to the system.
- Dashboard: Provides an overview of the total devices used in the organization, displayed using a categorized pie chart (phone, laptop, dashboard, peripherals).
- Device Listing: View a list of devices with search functionality to filter data based on device type, device ID, and employee ID.
- Device Details: Provides detailed information about individual devices.
- Delete Device: Long-press on a device in the list to open a dialog to delete the device.
- User Listing: View a list of users with search functionality to filter employees based on name and employee ID.
- User Details: View user details, including the total device count allocated to the user.
- Device Allocation: Clicking on the device count for a user opens a dialog sheet displaying the list of devices allocated to that user. Includes a search box to filter devices based on device ID.
- Profile Screen: View and update the admin's profile information. Includes a logout button.
- Signup Screen: Allows the creation of new user accounts.

## Usage

1. Launch the application on your Android device.
2. Log in to the system using your administrator credentials.
3. Upon successful login, you will be directed to the dashboard, displaying the total devices used in the organization.
4. Navigate through the various sections using the bottom navigation bar to access device listing, user listing, and profile screens.
5. Use the search box provided in the device listing and user listing screens to filter the data based on your requirements.
6. Click on a device from the device listing to view its details, including specifications and allocation status. If the device is assigned to a user, the user's id will also be displayed.
7. Long-press on a device from the device listing to delete it from the system.
8. Click on a user from the user listing to view their details, including the total device count allocated to them.
9. Click on the device count for a user to view the list of devices allocated to them. Use the search box to filter the devices based on the device ID.
10. Visit the profile screen to view and update your admin profile information. You can change your name and phone number.
11. To log out from the system, click on the "Logout" button on the profile screen.
12. Use the signup screen to create new user accounts.

## Architecture

The application follows the MVVM (Model-View-ViewModel) architectural pattern. 

The major components of the architecture are:

- **Model**: Represents the data and business logic of the application. It includes data models, repositories for data access, and remote data sources (Firestore).
- **View**: Represents the UI layer of the application. It includes activities, fragments, XML layout files, and corresponding view models.
- **ViewModel**: Acts as a mediator between the View and the Model. It contains the presentation logic, handles UI-related events, and exposes observable data to the View using LiveData.

## Prerequisites

Before running the application, ensure you have the following:

- Android Studio
- Android SDK
- Kotlin
- JDK
- Gradle

## Setup

1. Download the ZIP folder containing the project code.
2. Extract the contents of the ZIP folder to a location on your computer.
3. Open Android Studio.
4. Click on "Open an Existing Project" and navigate to the extracted project folder.
5. Select the project folder and click "OK" to open it in Android Studio.
6. Connect your Android device or start an emulator.
7. Click on the "Run" button in the toolbar or go to "Run" > "Run 'app'" to build and run the application on the emulator.

## Installing and Running the APK

APK file is attached in the zip folder (devicemanagement_adminportal.apk) for the application, you can directly install and run it on an Android device. 

Here's how:

1. Transfer the APK file to your Android device.
2. On your Android device, go to "Settings" > "Security" or "Privacy"and enable the "Unknown Sources" option to allow installation from sources other than the Play Store.
3. Using a file manager app, navigate to the location of the APK file on your device.
4. Tap on the APK file to start the installation process.
5. Follow the on-screen prompts to install the application.
6. Once installed, you can find the application on your device's app launcher. 
7. Tap on the app icon to run it.

## Configuration

To configure the application, follow these steps:

1. Open the `app` module.
2. Update the `app/src/main/res/values/strings.xml` file with your desired configurations, such as API endpoints, keys, or other settings.
3. Build and run the application.

## Testing

The application includes unit tests to ensure its functionality and reliability. 

To run the tests, follow these steps:

1. Open the `app` module.
2. Navigate to the `androidTest` or `test` directory to run unit tests, respectively.
3. Right-click on the desired test file or directory and select "Run" or use the corresponding Gradle command.

## Database Setup

The Device Management Admin Portal utilizes two databases for data storage:

1. Firebase Firestore: Firestore is a flexible, scalable NoSQL cloud database provided by Firebase. It serves as the remote database for real-time data synchronization. The application uses the following Firestore dependencies:

    - `implementation 'com.google.firebase:firebase-firestore-ktx:24.6.1'`: Firestore Kotlin extension for Android.
    - `implementation 'com.google.firebase:firebase-auth-ktx:22.0.0'`: Firebase Authentication Kotlin extension for Android.

   To set up Firestore for your project, follow the Firebase documentation: [Add Firebase to your Android project](https://firebase.google.com/docs/android/setup).

2. Room Database: Room is an SQLite object mapping library provided by Android Jetpack. It acts as the local database for offline data storage. The application uses the following Room dependencies:

    - `implementation "androidx.room:room-runtime:2.4.0"`: Room library for runtime support.
    - `kapt "androidx.room:room-compiler:2.4.0"`: Room annotation processor for generating necessary code.
    - `implementation "androidx.room:room-ktx:2.2.5"`: Room Kotlin extensions for simplified database operations.

   To set up Room Database for your project, refer to the Android Developer documentation: [Android Room Persistence Library](https://developer.android.com/training/data-storage/room).

## Libraries and Dependencies

The Device Management Admin Portal utilizes several libraries and dependencies to support its functionality. Here's a list of the major dependencies used in the project:

- Kotlin core and coroutines:
    - `implementation 'androidx.core:core-ktx:1.10.1'`: Kotlin extensions for Android.
    - `implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")`: Coroutines support for Android.
    - `implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")`: Core coroutines library.

- AndroidX libraries:
    - `implementation 'androidx.appcompat:appcompat:1.6.1'`: AppCompat library for compatibility across different Android versions.
    - `implementation 'com.google.android.material:material:1.9.0'`: Material Design components.
    - `implementation 'androidx.constraintlayout:constraintlayout:2.1.4'`: ConstraintLayout for flexible UI design.

- Testing packages:
    - `testImplementation 'junit:junit:4.13.2'`: JUnit testing framework.
    - `androidTestImplementation 'androidx.test.ext:junit:1.1.5'`: JUnit extensions for Android.
    - `androidTestImplementation 'androidx.test.espresso:espresso-core:3.5.1'`: Espresso UI testing framework.
    - `testImplementation 'androidx.arch.core:core-testing:2.1.0'`: Architecture Components testing library.

- Gson:
    - `implementation 'com.google.code.gson:gson:2.9.1'`: Gson library for JSON parsing.

- Lifecycle component:
    - `implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1"`: ViewModel Kotlin extensions for the Lifecycle components.

- Hilt:
    - `implementation "com.google.dagger:hilt-android:2.44"`: Hilt dependency injection library for Android.
    - `kapt "com.google.dagger:hilt-compiler:2.44"`: Annotation processor for Hilt.

- Pie chart dependency:
    - `implementation "com.github.PhilJay:MPAndroidChart:v3.1.0"`: MPAndroidChart library for pie chart visualization.

The complete list of dependencies can be found in the `build.gradle` file of the project.

## Contact

For any inquiries or support, please contact [ishikanimade56@gmail.com](mailto:ishikanimade56@gmail.com).

Feel free to customize the sections and content as per your specific application requirements and reach out to us with any questions, feedback, or suggestions you may have.


