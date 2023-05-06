package com.example.madassaignment

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {
    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    private lateinit var saveButton: Button
    private lateinit var logoutButton: Button
    private lateinit var nameUser: String
    private lateinit var emailUser: String
    private lateinit var usernameUser: String
    private lateinit var passwordUser: String
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        reference = FirebaseDatabase.getInstance().getReference("users")
        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        saveButton = findViewById(R.id.saveButton)
        logoutButton = findViewById(R.id.logoutButton)

        showData()

        saveButton.setOnClickListener {
            if (isNameChanged() || isPasswordChanged() || isEmailChanged()) {
                Toast.makeText(this@EditProfileActivity, "Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@EditProfileActivity, "No Changes Found", Toast.LENGTH_SHORT).show()
            }
        }

        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun isNameChanged(): Boolean {
        val newName = editName.text.toString()
        if (nameUser != newName) {
            reference.child(usernameUser).child("name").setValue(newName)
            nameUser = newName
            return true
        }
        return false
    }

    private fun isEmailChanged(): Boolean {
        val newEmail = editEmail.text.toString()
        if (emailUser != newEmail) {
            reference.child(usernameUser).child("email").setValue(newEmail)
            emailUser = newEmail
            return true
        }
        return false
    }

    private fun isPasswordChanged(): Boolean {
        val newPassword = editPassword.text.toString()
        if (passwordUser != newPassword) {
            reference.child(usernameUser).child("password").setValue(newPassword)
            passwordUser = newPassword
            return true
        }
        return false
    }

    private fun showData() {
        val intent = intent
        nameUser = intent.getStringExtra("name").toString()
        emailUser = intent.getStringExtra("email").toString()
        usernameUser = intent.getStringExtra("username").toString()
        passwordUser = intent.getStringExtra("password").toString()

        editName.setText(nameUser)
        editEmail.setText(emailUser)
        editUsername.setText(usernameUser)
        editPassword.setText(passwordUser)
    }
}