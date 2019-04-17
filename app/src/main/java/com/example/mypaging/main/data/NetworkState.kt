package com.example.mypaging.main.data

@Suppress("DataClassPrivateConstructor")
data class NetworkState private constructor(
    val status: Status,
    val msg: String? = null) {

    enum class Status {
        RUNNING,
        SUCCESS,
        FAILED
    }

    companion object {
        val LOADED = NetworkState(Status.SUCCESS)
        val LOADING = NetworkState(Status.RUNNING)
        fun error(msg: String?) =
            NetworkState(Status.FAILED, msg)
    }
}