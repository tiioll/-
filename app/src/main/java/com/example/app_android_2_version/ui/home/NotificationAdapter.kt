package com.example.app_android_2_version.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.app_android_2_version.NotificationsActivity
import com.example.app_android_2_version.databinding.NotificationItemBinding

class NotificationAdapter(private val listener: NotificationsActivity) : ListAdapter<Notification, NotificationAdapter.ItemHolder>(ItemComporator()) {

    class ItemHolder(private val binding: NotificationItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(notification: Notification, listener: Listener) = with(binding){

            notificationListItemText.text = notification.Text

            notificationDate.text = notification.Date
            notificationTime.text = notification.Time

            if (!notification.New){
                notificationNew.visibility = View.INVISIBLE
            }
        }

        companion object{
            fun create(parent: ViewGroup): ItemHolder{
                return ItemHolder(NotificationItemBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false))
            }
        }
    }

    interface Listener{
        fun onClick(notification: Notification)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }
}

class ItemComporator : DiffUtil.ItemCallback<Notification>(){
    override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
        return oldItem == newItem
    }
}