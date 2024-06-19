package com.example.app_android_2_version.ui.habitats

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.app_android_2_version.databinding.FragmentHabitatBinding
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

class HabitatFragment : Fragment(), HabitatsAdapter.Listener, WeekDayAdapter.Listener {

    private var _binding: FragmentHabitatBinding? = null
    private val binding get() = _binding!!

    private lateinit var weekDayAdapter: WeekDayAdapter
    private lateinit var habitAdapter: HabitatsAdapter

    val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("dd.MM.yyyy")
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    var NowDay: String = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        LocalDate.now().format(formatter)
    } else {
        TODO("VERSION.SDK_INT < O")
    }

    val database = Firebase.database
    val myRef = database.getReference("habitats")

    var daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")
    var ActiveWeekDay: Int = daysOfWeek.indexOf(GetWeekDay(NowDay))
    var HabitName: String = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHabitatBinding.inflate(inflater, container, false)
        val root: View = binding.root

        initWeekRcView()
        initHabitRcView()

        updateCalendar()
        onChangeListener(myRef)


        binding.habitatsCreateHabit.setOnClickListener {
            HabitName = binding.habitat5.getText().toString().trim()


            if (HabitName == "")
                Toast.makeText(
                    requireContext(),
                    "Сначала введите привычку",
                    Toast.LENGTH_SHORT
                ).show()


            else if (!HabitIsOnly(myRef))
                Toast.makeText(
                    requireContext(),
                    "Такая привычка уже есть",
                    Toast.LENGTH_SHORT
                ).show()


            else {
                val currentUser = FirebaseAuth.getInstance().currentUser
                currentUser?.uid?.let { uid ->
                    val userReference =
                        FirebaseDatabase.getInstance().getReference("users").child(uid)
                    userReference.child("habitats").child(HabitName)
                        .setValue(Habit(HabitName, ActiveWeekDay))
                }

                updateCalendar()
                onChangeListener(myRef)
                Toast.makeText(
                    requireContext(),
                    "Привычка " + HabitName + " установлена",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return root
    }


    override fun onClick(weekDay: WeekDay) {
        ActiveWeekDay = weekDay.weekDayNumber!!
        updateCalendar()
    }


    override fun onClick(habit: Habit) {
        if (daysOfWeek.indexOf(GetWeekDay(NowDay)) != ActiveWeekDay){
            if (daysOfWeek.indexOf(GetWeekDay(NowDay)) < ActiveWeekDay) {
                Toast.makeText(
                    requireContext(),
                    "Этот день уже прошёл",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Этот день ещё не наступил",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        else {
//Удаление элемента:
            val currentUserDel = FirebaseAuth.getInstance().currentUser
            currentUserDel?.uid?.let { uid ->
                val userReference = FirebaseDatabase.getInstance().getReference("users").child(uid)
                habit.habit?.let {
                    userReference.child("habitats").child(habit.habit!!).removeValue()
                }
            }

            if (habit.delete){
                onChangeListener(myRef)
                return
            }


            if (ActiveWeekDay == daysOfWeek.indexOf(GetWeekDay(NowDay)))
                habit.doneList[ActiveWeekDay] = !habit.doneList[ActiveWeekDay]

            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.uid?.let { uid ->
                val userReference =
                    FirebaseDatabase.getInstance().getReference("users").child(uid)
                habit.habit?.let {
                    userReference.child("habitats").child(it)
                        .setValue(Habit(habit.habit, ActiveWeekDay, habit.doneList))
                }
            }

            onChangeListener(myRef)
        }

    }

    private fun HabitIsOnly(dRef: DatabaseReference): Boolean {
        var isOnly = true
        val currentUserUid = FirebaseAuth.getInstance().currentUser?.uid
        currentUserUid?.let { uid ->
            FirebaseDatabase.getInstance().getReference("users").child(uid)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        var snap = snapshot.child("habitats")
                        for (s in snap.children) {
                            val habit = s.getValue(Habit::class.java)
                            if (habit != null) {
                                if (HabitName!!.trim() == habit.habit) {
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
                        val list = ArrayList<Habit>()
                        var snap = snapshot.child("habitats")

                        for (s in snap.children) {
                            val habit = s.getValue(Habit::class.java)
                            if (habit != null) {
                                habit.activeDay = ActiveWeekDay
                            }

                            if (habit != null) {
                                if (habit.doneList[ActiveWeekDay])
                                    list.add(habit)
                                else
                                    list.add(0, habit)
                            }
                        }
                        habitAdapter.notifyDataSetChanged()
                        habitAdapter.submitList(list)

                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })
        }
    }

    private fun updateCalendar() {
        val list = ArrayList<WeekDay>()
        var daysOfWeek = listOf("Пн", "Вт", "Ср", "Чт", "Пт", "Сб", "Вс")

        for (i in 0..6) {
            list.add(WeekDay((i == ActiveWeekDay), (i < ActiveWeekDay), i, daysOfWeek[i]))
        }
        weekDayAdapter.submitList(list)
        onChangeListener(myRef)
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

    private fun initWeekRcView() = with(binding) {
        weekDayAdapter = WeekDayAdapter(this@HabitatFragment)
        weekDays.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        weekDays.adapter = weekDayAdapter
    }

    private fun initHabitRcView() = with(binding) {
        habitAdapter = HabitatsAdapter(this@HabitatFragment)
        habitats.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        habitats.adapter = habitAdapter
    }
}