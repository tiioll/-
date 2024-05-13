package com.example.app_android_2_version.ui.tasks

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.app_android_2_version.databinding.DayListItemBinding
import com.example.app_android_2_version.databinding.TaskListItemBinding

class TaskCalendarAdapter(val listener: Listener) : ListAdapter<Day, TaskCalendarAdapter.ItemHplder>(MonthsComporator()) {

    class ItemHplder(private val binding: DayListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(day: Day, listener: Listener) = with(binding){

            if (day.active){
                cardViewDay.visibility = View.INVISIBLE
                cardViewActiveDay.visibility = View.VISIBLE

                dayListItemWeekDay.text = day.weekDay
                dayListItemActiveDay.text = day.date.toString()

                cardViewActiveDay.setOnClickListener{
                    listener.onClick(day)
                }
            }
            else{
                cardViewDay.visibility = View.VISIBLE
                cardViewActiveDay.visibility = View.INVISIBLE

                dayListItemDay.text = day.date.toString()

                cardViewDay.setOnClickListener{
                    listener.onClick(day)
                }
            }


        }

        companion object{
            fun create(parent: ViewGroup): ItemHplder{
                return ItemHplder(DayListItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    interface Listener{
        fun onClick(day: Day)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHplder {
        return ItemHplder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHplder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class MonthsComporator : DiffUtil.ItemCallback<Day>(){
    override fun areItemsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Day, newItem: Day): Boolean {
        return oldItem == newItem
    }
}