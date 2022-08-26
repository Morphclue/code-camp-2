package org.feature.fox.coffee_counter.util

/**
 * TODO: Add description.
 *
 * @param T
 * @property status
 * @property data
 * @property message
 */
data class Resource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }
}

/**
 * Enum to represent status of a resource
 */
enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}
