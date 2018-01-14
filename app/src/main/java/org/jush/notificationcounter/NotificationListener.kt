package org.jush.notificationcounter

import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import timber.log.Timber

class NotificationListener : NotificationListenerService() {
    override fun onNotificationPosted(sbn: StatusBarNotification?, rankingMap: RankingMap?) {
        super.onNotificationPosted(sbn, rankingMap)
        Timber.d("Got notification: $sbn")
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Timber.d("onListenerConnected")
        activeNotifications.forEach { Timber.d("Active notification: $it") }
        sendBroadcast(Intent(ACTION_NOTIFICATION_LISTENER_READY).setPackage(packageName))
    }
}
