package com.katsidzira.kotlindemo.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.katsidzira.kotlindemo.R
import com.katsidzira.kotlindemo.database.Subscriber
import com.katsidzira.kotlindemo.databinding.SubscriberItemViewBinding

class SubscriberAdapter(
    private val subscribers: List<Subscriber>,
    private val clickListener: (Subscriber) -> Unit
) : RecyclerView.Adapter<SubscriberViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: SubscriberItemViewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.subscriber_item_view, parent, false)
        return SubscriberViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return subscribers.size
    }

    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        holder.bind(subscribers[position], clickListener)
    }
}

class SubscriberViewHolder(val binding: SubscriberItemViewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit) {
        binding.textviewName.text = subscriber.name
        binding.textviewEmail.text = subscriber.email
        binding.layoutListItem.setOnClickListener {
            clickListener(subscriber)
        }
    }
}