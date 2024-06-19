package com.example.app_android_2_version

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.app_android_2_version.databinding.ActivityMainBinding
import com.example.app_android_2_version.ui.trainings.MyBackGroundService
import com.github.mikephil.charting.data.Entry
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import java.util.Calendar


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding

    private lateinit var photoLauncher: ActivityResultLauncher<Intent>
    private lateinit var bottomNavigationView: BottomNavigationView

    private var sensorManager: SensorManager? = null


    // Creating a variable which will give the running status
    // and initially given the boolean value as false
    private var running = false

    // Creating a variable which will counts total steps
    // and it has been given the value of 0 float
    private var totalSteps = 0f
    // Creating a variable  which counts previous total
    // steps and it has also been given the value of 0 float
    private var previousTotalSteps = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSupportActionBar()?.hide()
        val navView: BottomNavigationView = binding.navView
        val entries = mutableListOf<Entry>() // ваши точки графика
        val entriesJson = Gson().toJson(entries)
        setnotificationSendService()





        startBackgroundService()
        val intent = Intent("com.android.ServiceStopped")
        sendBroadcast(intent)

        val navController = findNavController(R.id.nav_host_fragment_activity_main)


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_waterdrop, R.id.navigation_training, R.id.navigation_tasks))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        bottomNavigationView = findViewById(R.id.nav_view)

    }

    override fun onNavigateUp(): Boolean {
        return super.onSupportNavigateUp()
    }

    fun startBackgroundService() {
        val intent = Intent(this, MyBackGroundService::class.java)
        this.startService(intent)
    }






    fun onButtonclick(view: View)
    {
        Toast.makeText(this, "TaskButton", Toast.LENGTH_SHORT).show()
    }

    fun onButtonclick2(view: View)
    {
        Toast.makeText(this, "HabitatButton", Toast.LENGTH_SHORT).show()
    }

    fun onButtonclick3(view: View)
    {
        Toast.makeText(this, "DrinkButton", Toast.LENGTH_SHORT).show()
    }
    fun registrationClick(view: View)
    {
        Toast.makeText(this, "RegisterButton", Toast.LENGTH_SHORT).show()
    }


    // Кнопки выше все 4 нужно перенести по фрагментам, по каким сам определишься, если зайдешь в каждый из фрагментов
    // и наведешься на предупрежденые на аттрибутах onClick

    private fun setnotificationSendService() {
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        //creating a new intent specifying the broadcast receiver
        val i = Intent(this, AlarmManager::class.java)

        //creating a pending intent using the intent
        val pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT)

        //setting the repeating alarm that will be fired every day
        val alarmStartTime: Calendar = Calendar.getInstance()
        alarmStartTime.set(Calendar.HOUR_OF_DAY, 15)
        alarmStartTime.set(Calendar.MINUTE, 25)
        alarmStartTime.set(Calendar.SECOND, 0)
        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            alarmStartTime.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES, pi
        )
    }



}