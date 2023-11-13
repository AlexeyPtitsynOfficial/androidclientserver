package com.jkhrs.jkhrsoi.util

public class APIError {
    private val statusCode : Int = 0
    private val message: String = ""

    public fun status(): Int {
        return statusCode
    }

    public fun getMessage(): String {
        return message
    }
}