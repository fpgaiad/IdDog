package br.com.iddog.ui

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.iddog.R
import br.com.iddog.util.UserHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val storage = getSharedPreferences("User", Context.MODE_PRIVATE)
        UserHelper.setStorage(storage)
    }
}