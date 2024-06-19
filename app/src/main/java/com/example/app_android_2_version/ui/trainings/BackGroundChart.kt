package com.example.app_android_2_version.ui.trainings

import Restarter
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.github.mikephil.charting.data.Entry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit


class MyBackGroundService : Service() {

    private lateinit var scheduledExecutor: ScheduledExecutorService
    override fun onCreate() {
        super.onCreate()
        // Выполняем инициализацию сервиса
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Запускаем фоновые задачи
        startForegroundTask()
        return START_STICKY
    }

    private fun startForegroundTask() {
        // Создаем уведомление для сервиса
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)

        addDataToLineChart()
        scheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        scheduledExecutor.scheduleAtFixedRate(::addDataToLineChart, 0, 1, TimeUnit.MINUTES)
    }

    override fun onDestroy() {
        //stopService(mServiceIntent);
        val broadcastIntent = Intent()
        broadcastIntent.setAction("restartservice")
        broadcastIntent.setClass(this, Restarter::class.java)
        this.sendBroadcast(broadcastIntent)
        super.onDestroy()
    }



    private fun createNotification(): Notification {
        // Создаем и настраиваем уведомление
        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("My Service")
            .setContentText("Service is running")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        // Создаем канал для уведомления (для Android 8.0 и выше)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "My Service", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        return notificationBuilder.build()
    }



    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val CHANNEL_ID = "my_service_channel"
    }

    private fun addDataToLineChart() {

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val dataChange = snapshot.child("joinDate").getValue(String::class.java)
                        val steps = snapshot.child("steps").getValue(Long::class.java)?.toFloat()
                        val entries = snapshot.child("entry").getValue(String::class.java)
                        val entriesForData = Gson().fromJson(entries, Array<Entry>::class.java).toMutableList()
                        val lastTwoChars = dataChange?.substring(dataChange.length - 2)?.toFloat()
                        val date: DateFormat = SimpleDateFormat("MMM dd yyyy, h:mm")
                        val dateFormatted: String = date.format(Calendar.getInstance().time)
                        val lastTwoCharForCompariosn = dateFormatted.substring(dateFormatted.length - 2).toFloat()
                        val dayNumber = dateFormatted.substring(5, 7).toFloat()
                        val dayNumberForCompariosn = dataChange?.substring(5, 7)?.toFloat()
                        var entries02 = Entry(lastTwoCharForCompariosn, steps!!)
                        /*val iterator: MutableIterator<Entry> = entriesForData.iterator()
                            while (iterator.hasNext()) {
                                val value: Entry = iterator.next()
                                if (value.x == entries02.x) {
                                    iterator.remove()
                                }

                        }
                        entriesForData.add(entries02)*/
                        entriesForData.add(entries02)

                        saveData(entriesForData)




                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
//val entries = mutableListOf<Entry>()
        /*entries.add(Entry(0f, 3600f))
        entries.add(Entry(1f, 10000f))
        entries.add(Entry(2f, 1000f))
        entries.add(Entry(3f, 5000f))*/




    }

    private fun saveData(entries: MutableList<Entry>) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)

            val entriesForSave = Gson().toJson(entries)
            userReference.child("entry").setValue(entriesForSave)

        }
    }


}