package org.jush.notificationcounter

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.provider.Settings
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import org.jush.notificationcounter.databinding.ActivityMainBinding
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    private lateinit var contentView: ActivityMainBinding

    private var notificationListenerReadyReceiver: NotificationListenerReadyReceiver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentView = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()
        val weHaveNotificationListenerPermission =
                NotificationManagerCompat.getEnabledListenerPackages(this).any {
                    it == packageName
                }

        contentView.missingPermissions = !weHaveNotificationListenerPermission
        contentView.btEnablePermission.setOnClickListener { askNotificationPermission() }
        if (notificationListenerReadyReceiver != null) {
            try {
                val copy = notificationListenerReadyReceiver
                notificationListenerReadyReceiver = null
                unregisterReceiver(copy)
            } catch (e: IllegalArgumentException) {
                Timber.w(e, "Ignored exception")
            }
        }
    }

    private fun askNotificationPermission() {
        notificationListenerReadyReceiver = NotificationListenerReadyReceiver()
        registerReceiver(notificationListenerReadyReceiver,
                IntentFilter(ACTION_NOTIFICATION_LISTENER_READY))
        //ask for permission
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        startActivity(intent)
    }
}

class NotificationListenerReadyReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Timber.d("Got action that notification listener is ready")
        context.startActivity(
                Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
    }

}
