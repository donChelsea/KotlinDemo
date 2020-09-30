package com.katsidzira.kotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.katsidzira.kotlindemo.adapter.SubscriberAdapter
import com.katsidzira.kotlindemo.database.Subscriber
import com.katsidzira.kotlindemo.database.SubscriberDatabase
import com.katsidzira.kotlindemo.databinding.ActivityMainBinding
import com.katsidzira.kotlindemo.repository.SubscriberRepository
import com.katsidzira.kotlindemo.viewmodel.SubscriberViewModel
import com.katsidzira.kotlindemo.viewmodel.SubscriberViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)

        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)

        binding.viewModel = subscriberViewModel
        binding.lifecycleOwner = this

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.subscriberRecyclerview.layoutManager = LinearLayoutManager(this)

        displaySubscribersList()
    }

    // observe subscribers list and give to adapter with clicklistener for items in viewholder
    private fun displaySubscribersList() {
        subscriberViewModel.subscribers.observe(this, Observer {
            Log.d("main activity", it.toString())
            binding.subscriberRecyclerview.adapter = SubscriberAdapter(it.reversed(), {selectedItem: Subscriber -> listItemClicked(selectedItem)})
        })
    }

    private fun listItemClicked(subscriber: Subscriber) {
        Toast.makeText(this, "selected name is ${subscriber.name}", Toast.LENGTH_SHORT).show()
    }
}
