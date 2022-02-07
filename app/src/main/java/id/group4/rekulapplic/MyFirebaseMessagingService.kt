package id.group4.rekulapplic
import android.content.Context
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {


    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        showNotification(p0.notification?.title.toString(),p0.notification?.body.toString())
    }

    fun showNotification(title:String, body:String) {
        val mBuilder = NotificationCompat.Builder(this, AddJadwal.CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val mNotificationMgr = NotificationManagerCompat.from(this)
        mNotificationMgr.notify(1, mBuilder.build())
    }

    fun subscribeTopic(context: Context, topic: String) {
        FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener {
            Toast.makeText(context, "Subscribed $topic", Toast.LENGTH_LONG).show()
        }.addOnFailureListener {
            Toast.makeText(context, "Failed to Subscribe $topic", Toast.LENGTH_LONG).show()
        }
    }



    //    override fun onMessageReceived(remoteMessage: RemoteMessage) {
//        super.onMessageReceived(remoteMessage)
//
//        if(remoteMessage.notification !== null){
//            val title = remoteMessage.notification!!.title
//            val body = remoteMessage.notification!!.body
//
//            val notificationHelper = NotificationHelper()
//            val displayNotification = notificationHelper.displayNotification(applicationContext,title!!,body!!)
//        }
//    }
}