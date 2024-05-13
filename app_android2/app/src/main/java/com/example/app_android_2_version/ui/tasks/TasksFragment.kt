package com.example.app_android_2_version.ui.tasks


import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_android_2_version.databinding.FragmentTasksBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class TasksFragment : Fragment(), TaskAdapter.Listener, TaskCalendarAdapter.Listener {

    private var _binding: FragmentTasksBinding? = null
    private val binding get() = _binding!!

    private var Task: String = ""
    private lateinit var adapter: TaskAdapter
    private lateinit var CalendarAdapter: TaskCalendarAdapter
    private lateinit var TaskDate: String

    val database = Firebase.database
    var auth = FirebaseAuth.getInstance()
    val myRef = database.getReference("tasks")

    var ActiveYear: Int = 2024
    var ActiveMonth: Int = 1
    var ActiveDay: Int = 1

    lateinit var SelectedDay: Day

    var Months = listOf(
        "Январь",
        "Февраль",
        "Март",
        "Апрель",
        "Май",
        "Июнь",
        "Июль",
        "Август",
        "Сентябрь",
        "Октябрь",
        "Ноябрь",
        "Декабрь"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("dd.MM.yyyy")
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        var nowDate: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDate.now().format(formatter)
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        ActiveDay = (nowDate[0].toString() + nowDate[1].toString()).toInt()
        ActiveMonth = (nowDate[3].toString() + nowDate[4].toString()).toInt()
        ActiveYear = (nowDate[6].toString() + nowDate[7].toString() + nowDate[8].toString() + nowDate[9].toString()).toInt()

        binding.monthChange.text = Months.get(ActiveMonth - 1)
        binding.yearChange.text = ActiveYear.toString()

        //Настройка списков и бд

        onChangeListener(myRef)
        initRcView()
        initCalendarRcView()
        updateCalendar()

        binding.yearChangeLeft.setOnClickListener {
            ActiveYear -= 1
            binding.yearChange.text = ActiveYear.toString()
            ActiveDay = 1
            updateCalendar()
        }
        binding.yearChangeRight.setOnClickListener {
            ActiveYear += 1
            binding.yearChange.text = ActiveYear.toString()
            ActiveDay = 1
            updateCalendar()
        }

        binding.monthChangeLeft.setOnClickListener {
            if (ActiveMonth == 1) {
                ActiveMonth = 13
                ActiveYear -= 1
                binding.yearChange.text = ActiveYear.toString()
            }
            ActiveMonth -= 1
            binding.monthChange.text = Months.get(ActiveMonth - 1)
            ActiveDay = 1
            updateCalendar()
        }

        binding.monthChangeRight.setOnClickListener {
            if (ActiveMonth == 12) {
                ActiveMonth = 0
                ActiveYear += 1
                binding.yearChange.text = ActiveYear.toString()
            }
            ActiveMonth += 1
            binding.monthChange.text = Months.get(ActiveMonth - 1)
            ActiveDay = 1
            updateCalendar()
        }


        //Добавление задачи по клику
        binding.tasksCreateTask.setOnClickListener {
            Task = binding.tasksNewtask.getText().toString().trim()

            if (Task == "") {
                Toast.makeText(requireContext(), "Сначала введите задачу", Toast.LENGTH_SHORT)
                    .show()
            }

            if (!TaskIsOnly(myRef)) {
                Toast.makeText(
                    requireContext(),
                    "Задача с таким именем уже существует",
                    Toast.LENGTH_SHORT
                ).show()
            }

            var d: String
            var m: String
            var y = ActiveYear

            if (ActiveDay < 10) {
                d = "0" + ActiveDay.toString()
            } else {
                d = ActiveDay.toString()
            }
            if (ActiveMonth < 10) {
                m = "0" + ActiveMonth.toString()
            } else {
                m = ActiveMonth.toString()
            }
            TaskDate = d + "." + m + "." + y

            if (Task != "" && TaskIsOnly(myRef)) {
                val currentUser = FirebaseAuth.getInstance().currentUser
                currentUser?.uid?.let { uid ->
                    val userReference =
                        FirebaseDatabase.getInstance().getReference("users").child(uid)
                    userReference.child("tasks").child(Task)
                        .setValue(Taska(Task, TaskDate))
                }

                updateCalendar()
                Toast.makeText(
                    requireContext(),
                    "Задача " + Task + " установлена до " + TaskDate,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return root
    }


    fun GetWeekDay(date: String): String {
        var day = (date[0] + date[1].toString()).toInt()
        var month = (date[3] + date[4].toString()).toInt()
        var year = (date[8].toString() + date[9].toString()).toInt()

        var yearCode = (6 + year + year / 4) % 7
        var monthCodes = arrayListOf(1, 4, 4, 0, 2, 5, 0, 3, 6, 1, 4, 6)
        if (year % 4 == 0) {
            monthCodes[0] = 0
            monthCodes[1] = 3
        }

        var daysOfWeek = listOf("Сб", "Вс", "Пн", "Вт", "Ср", "Чт", "Пт")

        return daysOfWeek.get((day + monthCodes.get(month - 1) + yearCode) % 7)
    }

    private fun initRcView() = with(binding) {
        adapter = TaskAdapter(this@TasksFragment)
        tasksTasklist.layoutManager = LinearLayoutManager(requireContext())
        tasksTasklist.adapter = adapter
    }

    private fun initCalendarRcView() = with(binding) {
        CalendarAdapter = TaskCalendarAdapter(this@TasksFragment)
        daysOfMonths.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        daysOfMonths.adapter = CalendarAdapter
    }

    override fun onClick(taska: Taska) {
        //Удаление элемента:
        val currentUserDel = FirebaseAuth.getInstance().currentUser
        currentUserDel?.uid?.let { uid ->
            val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
            taska.task?.let { userReference.child("tasks").child(taska.task!!).removeValue() }
        }

        if (!taska.done){
            taska.done = true

            Toast.makeText(
                requireContext(),
                "Задача " + taska.task + " выполнена",
                Toast.LENGTH_LONG
            ).show()
        }
        else{
            taska.done = false
        }

        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.uid?.let { uid ->
            val userReference =
                FirebaseDatabase.getInstance().getReference("users").child(uid)
            taska.task?.let {
                userReference.child("tasks").child(it)
                    .setValue(Taska(taska.task, taska.date, taska.done))
            }
        }

        onChangeListener(myRef)
    }

    override fun onClick(day: Day) {
        SelectedDay.active = false
        SelectedDay = day
        ActiveDay = day.date?.toInt()!!
        day.active = true
        CalendarAdapter.notifyDataSetChanged()
        onChangeListener(myRef)
    }

    private fun updateCalendar() {
        val list = ArrayList<Day>()

        var countOfDays: Int
        if (ActiveMonth == 2 && ActiveYear % 4 != 0) {
            countOfDays = 28
        } else if (ActiveMonth == 2 && ActiveYear % 4 == 0) {
            countOfDays = 29
        } else if (ActiveMonth == 4 || ActiveMonth == 6 || ActiveMonth == 9 || ActiveMonth == 11) {
            countOfDays = 30
        } else {
            countOfDays = 31
        }

        for (i in 1..countOfDays) {
            var d: String
            var m: String
            var y = ActiveYear

            var active = (i == ActiveDay)

            if (i < 10) {
                d = "0" + i.toString()
            } else {
                d = i.toString()
            }
            if (ActiveMonth < 10) {
                m = "0" + ActiveMonth.toString()
            } else {
                m = ActiveMonth.toString()
            }
            var date: String = d + "." + m + "." + y

            var day = Day((i).toString(), GetWeekDay(date), active)

            list.add(day)

            if (active)
                SelectedDay = day
        }
        CalendarAdapter.submitList(list)
        onChangeListener(myRef)
    }

    private fun TaskIsOnly(dRef: DatabaseReference): Boolean {
        var isOnly = true
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var snap = snapshot.child("tasks")
                        for (s in snap.children) {
                            val taska = s.getValue(Taska::class.java)
                            if (taska != null) {
                                if (Task.trim() == taska.task) {
                                    isOnly = false
                                    break
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }

        return isOnly
    }

    private fun onChangeListener(dRef: DatabaseReference) {

        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val list = ArrayList<Taska>()
                        var snap = snapshot.child("tasks")

                        var d: String
                        var m: String
                        var y = ActiveYear

                        if (ActiveDay < 10) {
                            d = "0" + ActiveDay.toString()
                        } else {
                            d = ActiveDay.toString()
                        }
                        if (ActiveMonth < 10) {
                            m = "0" + ActiveMonth.toString()
                        } else {
                            m = ActiveMonth.toString()
                        }
                        var activeDate = d + "." + m + "." + y

                        for (s in snap.children) {
                            val taska = s.getValue(Taska::class.java)

                            if (taska != null && (taska.date?.let {
                                    dateCompare(
                                        it,
                                        activeDate
                                    )
                                } == 0)) {
                                if (taska.done)
                                    list.add(taska)
                                else
                                    list.add(0, taska)
                            }
                        }
                        adapter.notifyDataSetChanged()
                        adapter.submitList(list)

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }

    private fun dateCompare(date0: String, date1: String): Int {
        //date0 < date1: return -1
        //date0 > date1: return 1
        //date0 == date1: return 0

        var day0 = (date0[0] + date0[1].toString()).toInt()
        var month0 = (date0[3] + date0[4].toString()).toInt()
        var year0 =
            (date0[6].toString() + date0[7].toString() + date0[8].toString() + date0[9].toString()).toInt()

        var day1 = (date1[0] + date1[1].toString()).toInt()
        var month1 = (date1[3] + date1[4].toString()).toInt()
        var year1 =
            (date1[6].toString() + date1[7].toString() + date1[8].toString() + date1[9].toString()).toInt()

        if (year0 < year1)
            return -1
        else if (year0 > year1)
            return 1
        else if (month0 < month1)
            return -1
        else if (month0 > month1)
            return 1
        else if (day0 < day1)
            return -1
        else if (day0 > day1)
            return 1
        else
            return 0
    }
}
