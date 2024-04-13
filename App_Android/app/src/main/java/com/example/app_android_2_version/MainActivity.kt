package com.example.app_android_2_version
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.app_android_2_version.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var Task: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
            R.id.navigation_home, R.id.navigation_waterdrop, R.id.navigation_training, R.id.navigation_tasks))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val taskList = findViewById<ListView>(R.id.tasks_tasklist)
        val newTask: EditText = findViewById(R.id.tasks_newtask)
        val createTask: Button = findViewById(R.id.tasks_createTask)

        //Настройка списка
        val actualTasks: MutableList<String> = mutableListOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, actualTasks)
        taskList.adapter = adapter

        val calendar = Calendar.getInstance()

        //Удаление по клику на элемент
        taskList.setOnItemClickListener { parent, view, position, id ->
            val text = taskList.getItemAtPosition(position).toString()
            adapter.remove(text)
        }

        //Добавление задачи по клику
        createTask.setOnClickListener {
            Task = newTask.text.toString().trim()

            val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateLabel(calendar)
            }

            if(Task != ""){
                DatePickerDialog(this, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                adapter.insert(Task, 0)
            }
        }
    }

    private fun updateLabel(calendar: Calendar) {
        val format = "dd-mm-yyyy"
        val std = SimpleDateFormat(format, Locale.TRADITIONAL_CHINESE)
        Task.plus(std.format(calendar.time).toString())
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




}