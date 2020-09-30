package com.katsidzira.kotlindemo

// standard code

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        private set // explicitly state to allow external read but not write

    /**
     * if not already handled, changes to true and returns the content
     * if handled, prevents its use again by returning null
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Returns the content, even if it's already been handled.
     */
    fun peekContent(): T = content
}