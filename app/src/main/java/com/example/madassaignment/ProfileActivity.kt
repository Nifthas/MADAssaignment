package com.example.madassaignment

import android.accessibilityservice.GestureDescription
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase



class ProfileActivity : AppCompatActivity() {
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileUsername: TextView
    private lateinit var profilePassword: TextView
    private lateinit var titleName: TextView
    private lateinit var titleUsername: TextView
    private lateinit var editProfile: Button
    private lateinit var logoutButton: Button
    private lateinit var deleteButton: Button
    private lateinit var database: DatabaseReference

    //new
    private lateinit var linearLayout: ProgressBar
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var firebaseUser: FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        profileName = findViewById(R.id.profileName)
        profileEmail = findViewById(R.id.profileEmail)
        profileUsername = findViewById(R.id.profileUsername)
        profilePassword = findViewById(R.id.profilePassword)
        titleName = findViewById(R.id.titleName)
        titleUsername = findViewById(R.id.titleUsername)
        editProfile = findViewById(R.id.editButton)
        logoutButton = findViewById(R.id.logoutButton)
        deleteButton = findViewById(R.id.deleteButton)


        showAllUserData()

        editProfile.setOnClickListener {
            passUserData()
        }


        //delete user profile

        deleteButton.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this)

            dialogBuilder.setTitle("Are you sure?")
            dialogBuilder.setMessage("Deleting your account cannot be undone.")

            dialogBuilder.setPositiveButton("Delete") { dialog, which ->
                val currentUser = FirebaseAuth.getInstance().currentUser
                val userRef = FirebaseDatabase.getInstance().reference.child("users").child(currentUser!!.uid)

                // Remove the user data from the database
                userRef.removeValue()

                // Delete the user from the authentication system
                currentUser.delete().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // User deleted successfully
                        Toast.makeText(this, "Account deleted", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                    } else {
                        // Failed to delete user
                        Toast.makeText(this, task.exception!!.message, Toast.LENGTH_LONG).show()
                    }
                }
            }

            dialogBuilder.setNegativeButton("Cancel") { dialog, which ->
                // Do nothing
            }

            val dialog = dialogBuilder.create()
            dialog.show()
        }









        logoutButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        database = FirebaseDatabase.getInstance().reference

        /*showAllUserData()

        editProfile.setOnClickListener {
            passUserData()
        }*/



    }

    private fun showAllUserData() {
        val intent = intent
        val nameUser = intent.getStringExtra("name")
        val emailUser = intent.getStringExtra("email")
        val usernameUser = intent.getStringExtra("username")
        val passwordUser = intent.getStringExtra("password")

        titleName.text = nameUser
        titleUsername.text = usernameUser
        profileName.text = nameUser
        profileEmail.text = emailUser
        profileUsername.text = usernameUser
        profilePassword.text = passwordUser
    }

    private fun passUserData() {
        val userUsername = profileUsername.text.toString().trim()
        val checkUserDatabase = database.child("users").orderByChild("username").equalTo(userUsername)

        checkUserDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (userSnapshot in dataSnapshot.children) {
                        val nameFromDB = userSnapshot.child("name").getValue(String::class.java)
                        val emailFromDB = userSnapshot.child("email").getValue(String::class.java)
                        val usernameFromDB = userSnapshot.child("username").getValue(String::class.java)
                        val passwordFromDB = userSnapshot.child("password").getValue(String::class.java)

                        val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
                        intent.putExtra("name", nameFromDB)
                        intent.putExtra("email", emailFromDB)
                        intent.putExtra("username", usernameFromDB)
                        intent.putExtra("password", passwordFromDB)
                        startActivity(intent)
                        break
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }


}




