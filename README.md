# 7-1 Final Project and Milestones

## Overview
This project is a Weight Tracker application developed in Java using Android Studio. The application allows users to log in, track their weight, and visualize their progress over time.

## Demo

<p float="left">
  <img src="https://github.com/ecan0/WeightTracker-CS-360/blob/main/loginPage.gif" width="45%" />
  <img src="https://github.com/ecan0/WeightTracker-CS-360/blob/main/MainWeightTrackerDemo.gif" width="45%" />

## Technologies Used
- Java
- Gradle
- SQLite / Room Database

## Project Structure
    - `app/src/main/java/com/example/weighttracker`: Contains the main package for the application.
        - `/Controller`: Contains the Java classes that handle the application's logic between View and Model. (LoggedWeightAdapter)
        - `/Data`: Contains the Java classes that handle the application's data (User, LoggedWeight, DAOs).
        - `/Views`: Contains the Java classes that handle the application's UI (MainActivity, LoginActivity).

    - `app/src/main/res`: Contains the resource files such as layouts, drawables, and values.
        - `/drawable`: Contains the drawable resources for the application (icons, backgrounds).
        - `/layout`: Contains the layout resources for the application (activity_main, activity_login).
        - `/values`: Contains the value resources for the application (strings, colors).

## Sample Usage
To run the application, follow these steps:
1. Clone the repository: `git clone https://github.com/ecan0/WeightTracker-CS-360.git`
2. Open the project in Android Studio.
3. Build the project using Gradle.
4. Run the application on an emulator such as Android Virtual Device or a physical device.

#User Instructions
1. Upon opening the application, the user will be prompted to log in.
2. If the user has not created an account, they can click the "Register" button to create a new account with a username and password typed on the existing line.
3. Once logged in, the user will be taken to the main page where they can enter their weight for the day.
4  Users can modify their goal weight and add, edit, and delete entries which are selected.
5. Notifications can be enabled with the button on the bottom, which will alert user with SMS-style notifications if their goal weight is reached.

## Journal Entry
This project was built upon the requirements for setting up a weight-tracking service in Android with database, notification, and user interactivity as the three key components. To support the user-facing functionality, a login page and main weight tracking page serve the user as the two main views of the app. Utilizing Google Material design helped to simplify layouts and arrange colors, typeface, and spacing that is easy to understand and interact with as a mobile user with a touchscreen-friendly design. The coding process revolved around utilizing a Model-View-Controller design pattern to decouple and separate Java components into their appropriate category. For example, User data including login information and logged weights are stored in the SQLite databases powered with Room, while the separate LoggedWeightAdapter mediates as a controller to serve data responses to the view of the application based on buttons pressed. Sticking to effective design patterns like MVC or Singleton to reduce coupling, enhance readability and performance, and minimize repeated code were crucial to the development process. Code functionality was mostly ensured via end-user testing, using the AVD in Android Studio to test button functions, responses from the backend database, and use of simulated SMS notifications. Overall, the app is innovative by simplistic design, only using and creating the essential components with no ads, excessive buffering, minimal load times, and local databases were instrumental in successfully demonstrating a full-stack mobile application that suits a user's weight goal needs effecitvely and with great ease-of-use.
