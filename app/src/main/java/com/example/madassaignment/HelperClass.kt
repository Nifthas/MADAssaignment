package com.example.madassaignment

import com.google.firebase.database.IgnoreExtraProperties

import java.io.Serializable


data class HelperClass(
    var name: String? = "",
    var email: String? = "",
    var username: String? = "",
    var password: String? = ""
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "email" to email,
            "username" to username,
            "password" to password
        )
    }
}


