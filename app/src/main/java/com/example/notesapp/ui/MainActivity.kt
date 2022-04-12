package com.example.notesapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.example.notesapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(androidx.navigation.fragment.R.id.nav_host_fragment_container)
        return super.onSupportNavigateUp() || navController.navigateUp()
    }
}