package com.example.app_android_2_version.ui.habitats

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.app_android_2_version.databinding.HabitListItemBinding

class HabitatsAdapter(val listener: HabitatFragment) : ListAdapter<Habit, HabitatsAdapter.ItemHplder>(HabitComporator()) {

    class ItemHplder(private val binding: HabitListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(habit: Habit, listener: Listener) = with(binding){
            habitListItemHabit.text = habit.habit

            habitDel.setOnClickListener {
                habit.delete = true
                listener.onClick(habit)
            }

            if (habit.doneList[habit.activeDay!!]){
                habitDone.visibility = View.INVISIBLE

                setDone.setOnClickListener{
                    listener.onClick(habit)
                }
            }
            else{
                habitDone.visibility = View.VISIBLE

                setDone.setOnClickListener{
                    listener.onClick(habit)
                }
            }
        }

        companion object{
            fun create(parent: ViewGroup): ItemHplder{
                return ItemHplder(HabitListItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    interface Listener{
        fun onClick(habit: Habit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHplder {
        return ItemHplder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHplder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class HabitComporator : DiffUtil.ItemCallback<Habit>(){
    override fun areItemsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Habit, newItem: Habit): Boolean {
        return oldItem == newItem
    }
}
