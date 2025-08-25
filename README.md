## Live Updates Notification for Android 16 🚀

Exploring the Next Evolution of Notifications

Android 16 introduces powerful new capabilities for apps to provide real-time, progress-focused notifications. With this, apps like ride-sharing, food delivery, or navigation can surface critical information—such as delivery status, ride ETA, or order progress—without requiring the user to open the app.

This project demonstrates how to build Live Updates Notifications that make your app more interactive and user-friendly.

## Features

- **Progress-Focused Notifications** <br>
  Use Notification.ProgressStyle to show users live updates of key tasks, like ride arrival or food delivery progress.

- **Real-Time Updates** <br>
  Deliver critical information in real-time for journeys, deliveries, and ride-sharing experiences.

- **Enhanced User Experience** <br>
  Minimize unnecessary app interactions by surfacing relevant updates directly in notifications.

## Getting Started
1. Clone this repository:
```bash
git clone https://github.com/yourusername/live-updates-android16.git
```
2. Open in Android Studio and run on a device with Android 16 or higher.
3. Explore the sample notifications with `LiveUpdateSample` composable.

## Usage

Use the `Notification.ProgressStyle` API to create notifications that update dynamically:

```kotlin
val builder = Notification.Builder(context, CHANNEL_ID)
    .setContentTitle("Delivery Progress")
    .setContentText("Your order is on the way!")
    .setProgress(maxValue, currentValue, false)
    .setOngoing(true)
```

Trigger updates through `SnackbarNotificationManager` or your own notification service to reflect real-time progress.

## Video Demo 🎥

Check out the live demo to see **Live Updates Notifications** in action:

https://github.com/user-attachments/assets/2314cc53-3f61-4fa3-a05b-6b19e4bf170e

## License

MIT License © [Chayan Mistry]

