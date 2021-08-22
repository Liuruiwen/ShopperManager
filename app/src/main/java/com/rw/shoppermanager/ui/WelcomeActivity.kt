package com.rw.shoppermanager.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rw.shoppermanager.R
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        iv_bg.setImageResource(R.drawable.welcome_bg)

    }
}