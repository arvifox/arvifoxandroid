package com.arvifox.arvi.google.firebase

import android.media.RingtoneManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.arvifox.arvi.R
import com.arvifox.arvi.siteback.BackGeoService
import com.arvifox.arvi.utils.BaseStorage
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FirebaseMessageService : FirebaseMessagingService() {

    override fun onMessageReceived(p0: RemoteMessage) {
        val count = p0!!.data.size
        val s1 = p0.data.get("key1")
        if (s1.equals("pos", true)) {
            BackGeoService.start(this)
        } else {
//            val s2 = p0.notification!!.body
            val s3 = s1 + count
            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val nb = NotificationCompat.Builder(this, BaseStorage.notificationChannelID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("FCM message")
                    .setContentText(s3)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
            val nm = NotificationManagerCompat.from(this)
            nm.notify(2349, nb.build())
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }
}