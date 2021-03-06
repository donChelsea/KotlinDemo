package com.katsidzira.kotlindemo.viewmodel

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katsidzira.kotlindemo.event.Event
import com.katsidzira.kotlindemo.database.Subscriber
import com.katsidzira.kotlindemo.repository.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable {

    val subscribers = repository.subscribers
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber

    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()
    @Bindable
    val clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear all"
    }

    fun insert(subscriber: Subscriber) = viewModelScope.launch {
        val newRowId: Long = repository.insert(subscriber)
        if (newRowId > -1) {
            statusMessage.value =
                Event("subscriber #${newRowId} inserted successfully")
        } else {
            statusMessage.value = Event("Error")
        }
    }

    fun update(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRows: Int = repository.update(subscriber)
        if (noOfRows > 0) {
            resetViews()
            statusMessage.value = Event("$noOfRows subscriber updated successfully")
        } else {
            statusMessage.value = Event("Error")
        }
    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRowsDeleted: Int = repository.delete(subscriber)
        if (noOfRowsDeleted > 0) {
            resetViews()
            statusMessage.value =
                Event("$noOfRowsDeleted subscriber deleted successfully")
        } else {
            statusMessage.value =
                Event("Error")
        }

    }

    fun clearAll() = viewModelScope.launch {
        val noOfRowsDeleted: Int = repository.deleteAll()
        if (noOfRowsDeleted > 0) {
            statusMessage.value =
                Event("$noOfRowsDeleted subscribers deleted successfully")
        } else {
            statusMessage.value =
                Event("Error")
        }
    }

    fun changeViewForSubscriber(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email

        isUpdateOrDelete = true

        subscriberToUpdateOrDelete = subscriber

        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    fun saveOrUpdate() {
        if (inputName.value == null || inputName.value == "") {
            statusMessage.value =
                Event("Please enter subscriber's name")
        } else if (inputEmail.value == null) {
            statusMessage.value =
                Event("Please enter subscriber's email")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value =
                Event("Please enter a valid email address")
        } else {
            if (isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!

                update(subscriberToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!

                insert(Subscriber(0, name, email))

                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    private fun resetViews() {
        inputName.value = null
        inputEmail.value = null

        isUpdateOrDelete = false

        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear all"
    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

}

/* viewModelScope.launch used to create a coroutine for this operation
   - will run in a background thread
*/
