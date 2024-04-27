package com.example.app_android_2_version.ui.tasks

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.app_android_2_version.databinding.TaskListItemBinding
import com.google.android.gms.tasks.Task

class TaskAdapter(val listener: Listener) : ListAdapter<Taska, TaskAdapter.ItemHplder>(ItemComporator()) {

    class ItemHplder(private val binding: TaskListItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(taska: Taska, listener: Listener) = with(binding){
            taskListItemTask.text = taska.task
            taskListItemDate.text = taska.date
            taskListItemWeekday.text = taska.weekDay

            val buttonTaskDelete = taskDelete

            buttonTaskDelete.setOnClickListener{
                listener.onClick(taska)
            }
        }

        companion object{
            fun create(parent: ViewGroup): ItemHplder{
                return ItemHplder(TaskListItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    interface Listener{
        fun onClick(taska: Taska)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHplder {
        return ItemHplder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHplder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class ItemComporator : DiffUtil.ItemCallback<Taska>(){
    override fun areItemsTheSame(oldItem: Taska, newItem: Taska): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Taska, newItem: Taska): Boolean {
        return oldItem == newItem
    }
}