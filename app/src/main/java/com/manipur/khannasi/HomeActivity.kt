package com.manipur.khannasi

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.manipur.khannasi.fragment.SignInFragment
import com.manipur.khannasi.util.LoggedInCheck

class HomeActivity : AppCompatActivity() {

    private lateinit var loggedInCheck: LoggedInCheck

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)


        loggedInCheck = LoggedInCheck(this)
        if (loggedInCheck.isLoggedIn()) {
            // User is logged in, start LoggedInActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Finish HomeActivity so user can't go back to it with the back button
        } else {
            // User is not logged in, load LoginFragment
            if (savedInstanceState == null) {
                supportFragmentManager.beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container, SignInFragment(), "SignInFragment")
                    .commit()
            }
        }
    }
}