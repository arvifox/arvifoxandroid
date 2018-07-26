package com.arvifox.arvi.firebase

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import com.arvifox.arvi.MainActivity
import com.arvifox.arvi.R
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

class FirebaseMessageService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val count = p0!!.data.size
        val s1 = p0.data.get("key1")
        val s2 = p0.notification!!.body
        val s3 = s2 + s1 + count
        val pend = PendingIntent.getActivity(this, 123, intent, PendingIntent.FLAG_ONE_SHOT)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val res = this.resources
        val nb = Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setContentTitle("FCM message")
                .setContentText(s3)
                .setStyle(Notification.BigTextStyle().bigText(s3))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pend)
        val nm = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(234, nb.build())
    }

    override fun onNewToken(p0: String?) {
        super.onNewToken(p0)
    }
}