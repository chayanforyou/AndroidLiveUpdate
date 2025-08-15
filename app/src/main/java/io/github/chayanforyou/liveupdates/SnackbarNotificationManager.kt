package io.github.chayanforyou.liveupdates

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Handler
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationCompat.ProgressStyle
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.IconCompat

object SnackbarNotificationManager {
    private lateinit var notificationManager: NotificationManager
    private lateinit var appContext: Context
    const val CHANNEL_ID = "live_updates_channel_id"
    private const val CHANNEL_NAME = "live_updates_channel_name"
    private const val NOTIFICATION_ID = 1234


    @RequiresApi(Build.VERSION_CODES.O)
    fun initialize(context: Context, notifManager: NotificationManager) {
        notificationManager = notifManager
        val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, IMPORTANCE_DEFAULT)
        appContext = context
        notificationManager.createNotificationChannel(channel)
    }

    private enum class OrderState(val delay: Long) {
        INITIALIZING(2000) {
            @RequiresApi(Build.VERSION_CODES.BAKLAVA)
            override fun buildNotification(): NotificationCompat.Builder {
                return buildBaseNotification(appContext, INITIALIZING)
                    .setContentTitle("Placing your order")
                    .setContentText("We’re confirming with the restaurant...")
                    .setShortCriticalText("Placing order")
                    .setStyle(buildBaseProgressStyle(INITIALIZING).setProgressIndeterminate(true))
            }
        },
        FOOD_PREPARATION(7000) {
            @RequiresApi(Build.VERSION_CODES.BAKLAVA)
            override fun buildNotification(): NotificationCompat.Builder {
                return buildBaseNotification(appContext, FOOD_PREPARATION)
                    .setContentTitle("Your food is being prepared")
                    .setContentText("The restaurant is cooking up your order fresh!")
                    .setShortCriticalText("Preparing")
                    .setStyle(
                        buildBaseProgressStyle(FOOD_PREPARATION)
                            .setProgressTrackerIcon(
                                IconCompat.createWithResource(
                                    appContext, R.drawable.ic_preparing
                                )
                            ).setProgress(25)
                    )
                    .setLargeIcon(
                        IconCompat.createWithResource(
                            appContext, R.drawable.preparing
                        ).toIcon(appContext)
                    )
            }
        },
        FOOD_ENROUTE(13000) {
            @RequiresApi(Build.VERSION_CODES.BAKLAVA)
            override fun buildNotification(): NotificationCompat.Builder {
                return buildBaseNotification(appContext, FOOD_ENROUTE)
                    .setContentTitle("Rider is on the way")
                    .setContentText("Your food has been picked up and is on the way")
                    .setShortCriticalText("On the way")
                    .setStyle(
                        buildBaseProgressStyle(FOOD_ENROUTE)
                            .setProgressTrackerIcon(
                                IconCompat.createWithResource(
                                    appContext, R.drawable.ic_delivering
                                )
                            )
                            .setProgress(50)
                    )
                    .setLargeIcon(
                        IconCompat.createWithResource(
                            appContext, R.drawable.delivering
                        ).toIcon(appContext)
                    )
            }
        },
        FOOD_ARRIVING(18000) {
            @RequiresApi(Build.VERSION_CODES.BAKLAVA)
            override fun buildNotification(): NotificationCompat.Builder {
                return buildBaseNotification(appContext, FOOD_ARRIVING)
                    .setContentTitle("Order is almost there")
                    .setContentText("Rider is arriving soon — get ready to enjoy!")
                    .setShortCriticalText("Arriving soon")
                    .setStyle(
                        buildBaseProgressStyle(FOOD_ARRIVING)
                            .setProgressTrackerIcon(
                                IconCompat.createWithResource(
                                    appContext, R.drawable.ic_arrived
                                )
                            )
                            .setProgress(75)
                    )
                    .setLargeIcon(
                        IconCompat.createWithResource(
                            appContext, R.drawable.arrived
                        ).toIcon(appContext)
                    )
            }
        },
        ORDER_COMPLETE(21000) {
            @RequiresApi(Build.VERSION_CODES.BAKLAVA)
            override fun buildNotification(): NotificationCompat.Builder {
                return buildBaseNotification(appContext, ORDER_COMPLETE)
                    .setContentTitle("Order delivered")
                    .setContentText("Thanks for ordering! Enjoy your meal.")
                    .setShortCriticalText("Delivered")
                    .setStyle(
                        buildBaseProgressStyle(ORDER_COMPLETE)
                            .setProgressTrackerIcon(
                                IconCompat.createWithResource(
                                    appContext, R.drawable.ic_delivered
                                )
                            )
                            .setProgress(100)
                    )
                    .setLargeIcon(
                        IconCompat.createWithResource(
                            appContext, R.drawable.delivered
                        ).toIcon(appContext)
                    )
            }
        };

        @RequiresApi(Build.VERSION_CODES.BAKLAVA)
        fun buildBaseProgressStyle(orderState: OrderState): ProgressStyle {
            val segmentColor = Color.valueOf(255f / 255f, 43f / 255f, 133f / 255f, 1f).toArgb()
            val pointColor = Color.valueOf(255f / 255f, 43f / 255f, 133f / 255f, 0.5f).toArgb()
            val pointColorCompleted =
                Color.valueOf(255f / 255f, 43f / 255f, 133f / 255f, 1f).toArgb()

            val progressStyle = ProgressStyle()
                .setProgressSegments(
                    listOf(
                        ProgressStyle.Segment(25).setColor(segmentColor),
                        ProgressStyle.Segment(25).setColor(segmentColor),
                        ProgressStyle.Segment(25).setColor(segmentColor),
                        ProgressStyle.Segment(25).setColor(segmentColor)

                    )
                ).setProgressPoints(
                    listOf(
                        ProgressStyle.Point(25).setColor(pointColor),
                        ProgressStyle.Point(50).setColor(pointColor),
                        ProgressStyle.Point(75).setColor(pointColor),
                        ProgressStyle.Point(100).setColor(pointColor)
                    )
                )
            when (orderState) {
                INITIALIZING -> {}
                FOOD_PREPARATION -> progressStyle.setProgressPoints(emptyList())
                FOOD_ENROUTE -> progressStyle.setProgressPoints(
                    listOf(
                        ProgressStyle.Point(25).setColor(pointColorCompleted)
                    )
                )

                FOOD_ARRIVING -> progressStyle.setProgressPoints(
                    listOf(
                        ProgressStyle.Point(25).setColor(pointColorCompleted),
                        ProgressStyle.Point(50).setColor(pointColorCompleted)
                    )
                )

                ORDER_COMPLETE -> progressStyle.setProgressPoints(
                    listOf(
                        ProgressStyle.Point(25).setColor(pointColorCompleted),
                        ProgressStyle.Point(50).setColor(pointColorCompleted),
                        ProgressStyle.Point(75).setColor(pointColorCompleted)
                    )
                )
            }
            return progressStyle
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun buildBaseNotification(
            appContext: Context,
            orderState: OrderState
        ): NotificationCompat.Builder {
            val notificationBuilder = NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setColor(ContextCompat.getColor(appContext, R.color.pink))
                .setOngoing(true)
                .setRequestPromotedOngoing(true)
                .setWhen(System.currentTimeMillis())

            when (orderState) {
                INITIALIZING -> {}
                FOOD_PREPARATION -> {}
                FOOD_ENROUTE -> {}
                FOOD_ARRIVING ->
                    notificationBuilder
                        .addAction(
                            NotificationCompat.Action.Builder(null, "Got it", null).build()
                        )
                        .addAction(
                            NotificationCompat.Action.Builder(null, "Tip", null).build()
                        )

                ORDER_COMPLETE ->
                    notificationBuilder
                        .addAction(
                            NotificationCompat.Action.Builder(
                                null, "Rate delivery", null
                            ).build()
                        )
            }
            return notificationBuilder
        }

        abstract fun buildNotification(): NotificationCompat.Builder
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun start() {
        for (state in OrderState.entries) {
            val notification = state.buildNotification().build()
            Handler(Looper.getMainLooper()).postDelayed({
                notificationManager.notify(NOTIFICATION_ID, notification)
            }, state.delay)
        }
    }
}