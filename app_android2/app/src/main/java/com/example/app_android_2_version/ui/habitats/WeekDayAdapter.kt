package com.example.app_android_2_version.ui.habitats

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.app_android_2_version.databinding.WeekDayItemBinding
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WeekDayAdapter() : ListAdapter<WeekDay, WeekDayAdapter.ItemHplder>(DayComporator()) {
    class ItemHplder(private val binding: WeekDayItemBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(day: WeekDay) = with(binding){

            weekDayListItemWeekDay.text = day.weekDayName

            if (day.active){
                weekDayActive.visibility = View.VISIBLE
                weekDayDone.visibility = View.INVISIBLE


            }
            else{
                weekDayActive.visibility = View.INVISIBLE

                if (day.past){
                    weekDayDone.visibility = View.VISIBLE
                }
                else
                    weekDayDone.visibility = View.INVISIBLE


            }
        }

        companion object{
            fun create(parent: ViewGroup): ItemHplder{
                return ItemHplder(WeekDayItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHplder {
        return ItemHplder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHplder, position: Int) {
        holder.bind(getItem(position))
    }
}

class DayComporator : DiffUtil.ItemCallback<WeekDay>(){
    override fun areItemsTheSame(oldItem: WeekDay, newItem: WeekDay): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: WeekDay, newItem: WeekDay): Boolean {
        return oldItem == newItem
    }
}
