package com.example.app_android_2_version.ui.habitats

class Habit(
    var habit: String? = null,
    var activeDay: Int? = 0,
    var doneList: ArrayList<Boolean> = arrayListOf(false, false, false, false, false, false, false),
    var delete: Boolean = false
    )