package com.example.antoanbmtt

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    private val option = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val controller = navHostFragment.navController
        val navGraph = controller.navInflater.inflate(R.navigation.nav_graph)
        if(option == 1){
            navGraph.setStartDestination(R.id.homeFragment)
        }
        controller.graph = navGraph
    }
}