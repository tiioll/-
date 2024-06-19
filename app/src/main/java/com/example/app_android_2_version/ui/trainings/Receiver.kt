import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.app_android_2_version.ui.trainings.MyBackGroundService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Start your service here
            startMyService(context)
        }
    }

    private fun startMyService(context: Context) {
        // Start your service, e.g., using an IntentService or a regular Service
        val serviceIntent = Intent(context, MyBackGroundService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(serviceIntent)
        } else {
            context.startService(serviceIntent)
        }
    }
}