package com.example.myvideocallapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<TextInputEditText>(R.id.urlInputEditText).setText("https://aniket.app.100ms.live/meeting/snippy-purple-mouse")

        val vm : LoginViewModel by viewModels()

        vm.authToken.observe(this, { authToken ->
            launchVideoRoomActivity(authToken)
        })

        vm.error.observe(this, { error -> showError(error) })

        findViewById<Button>(R.id.authenticateButton).setOnClickListener {
            val meetingLink = findViewById<TextInputEditText>(R.id.urlInputEditText).text.toString()

            vm.authenticate(meetingLink)
        }
    }

    private fun launchVideoRoomActivity(authToken : String?) {
        if(authToken != null) {
            // Launch the video room
            startActivity(Intent(this, VideoCallActivity::class.java).apply {
                putExtra(BUNDLE_AUTH_TOKEN, authToken)
                val name = findViewById<TextInputEditText>(R.id.nameInputEditText).text.toString()
                putExtra(BUNDLE_NAME, name.ifBlank { "Android User" })
            })
        }
    }

    private fun showError(error : String?) {
        if(error != null ) {
            Toast.makeText(this, error, Toast.LENGTH_LONG).show()
        }
    }
}