package com.example.app_android_2_version.ui.habitats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app_android_2_version.databinding.WeekDayItemBinding

class WeekDayAdapter(val listener: HabitatFragment) : ListAdapter<WeekDay, WeekDayAdapter.ItemHplder>(DayComporator()) {

    class ItemHplder(private val binding: WeekDayItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(weekDay: WeekDay, listener: Listener) = with(binding){

            weekDayListItemWeekDay.text = weekDay.weekDayName

            if (weekDay.active){
                weekDayActive.visibility = View.VISIBLE
                weekDayDone.visibility = View.INVISIBLE


            }
            else{
                weekDayActive.visibility = View.INVISIBLE

                if (weekDay.past){
                    weekDayDone.visibility = View.VISIBLE
                }
                else
                    weekDayDone.visibility = View.INVISIBLE


            }

            cardViewWeekDayItem.setOnClickListener{
                listener.onClick(weekDay)
            }
        }

        companion object{
            fun create(parent: ViewGroup): ItemHplder{
                return ItemHplder(WeekDayItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    interface Listener{
        fun onClick(weekDay: WeekDay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHplder {
        return ItemHplder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHplder, position: Int) {
        holder.bind(getItem(position), listener)
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
