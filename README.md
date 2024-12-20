# 7-1 Final Project and Milestones
     
## [SEE Journal Entry](#journal-entry)

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

- `app/src/main/java/com/example/weighttracker`: Contains the main package for the application
  - `/Controller`: Contains the Java classes that handle the application's logic between View and Model (LoggedWeightAdapter)
  - `/Data`: Contains the Java classes that handle the application's data (User, LoggedWeight, DAOs)
  - `/Views`: Contains the Java classes that handle the application's UI (MainActivity, LoginActivity)

- `app/src/main/res`: Contains the resource files such as layouts, drawables, and values
  - `/drawable`: Contains the drawable resources for the application (icons, backgrounds)
  - `/layout`: Contains the layout resources for the application (activity_main, activity_login)

## Sample Usage
To run the application, follow these steps:
1. Clone the repository: `git clone https://github.com/ecan0/WeightTracker-CS-360.git`
2. Open the project in Android Studio.
3. Build the project using Gradle.
4. Run the application on an emulator such as Android Virtual Device or a physical device.

## User Instructions
1. Upon opening the application, the user will be prompted to log in.
2. If the user has not created an account, they can click the "Register" button to create a new account with a username and password typed on the existing line.
3. Once logged in, the user will be taken to the main page where they can enter their weight for the day.
4  Users can modify their goal weight and add, edit, and delete entries which are selected.
5. Notifications can be enabled with the button on the bottom, which will alert user with SMS-style notifications if their goal weight is reached.

## Journal Entry

This Android weight-tracking application was developed with three key components: database integration, notifications, and user interactivity. The app features two main views: a login page and a weight tracking dashboard.
                                                                                                                                                                                                                             
## Design & Architecture
- Implemented Google Material Design principles for an intuitive, touch-friendly interface
- Followed Model-View-Controller (MVC) pattern to maintain clean separation of concerns
- Utilized SQLite with Room for efficient local data storage
- Employed LoggedWeightAdapter as a controller to manage data flow between views and models

## Technical Highlights
- Local SQLite database for storing user credentials and weight logs
- Room Persistence Library for streamlined database operations
- SMS notification system for weight tracking reminders
- Minimalist design focusing on essential features
- Fast load times with minimal resource usage
- No advertisements or unnecessary components

## Testing & Quality Assurance
Testing was conducted using Android Studio's AVD (Android Virtual Device) to verify:
- Button functionality and UI responsiveness
- Database operations and data persistence
- SMS notification system
- Overall user experience and performance

The result is a lightweight, efficient weight tracking solution that prioritizes user experience through fast performance and intuitive design.