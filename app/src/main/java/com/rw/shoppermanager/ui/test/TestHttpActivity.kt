package com.rw.shoppermanager.ui.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.rw.shoppermanager.R
import kotlinx.android.synthetic.main.activity_test_http.*


class TestHttpActivity : AppCompatActivity() {


    private val model:TestModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(application).create(TestModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_http)
        btn.setOnClickListener {
            model.getLogin( mapOf("username" to "Ruiwen","password" to "123456"))

        }
        model.result.observe(this, Observer {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })
    }



}