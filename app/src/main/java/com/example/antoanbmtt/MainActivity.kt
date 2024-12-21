package com.example.antoanbmtt

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.antoanbmtt.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var controller  : NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()
        setUpUIComponent()
    }
    private fun setUpUIComponent(){
        binding.apply{
            setSupportActionBar(topAppbar)
            appBarConfiguration = AppBarConfiguration(
                setOf(R.id.homeFragment,
                    R.id.shareFragment,
                    R.id.imageFragment,
                    R.id.cloudStorageFragment),
                drawerLayout
            )
            setupActionBarWithNavController(controller,appBarConfiguration)
            navigationView.setupWithNavController(controller)
            bottomNavView.setupWithNavController(controller)

            controller.addOnDestinationChangedListener { _, destination, _ ->
                if(destination.id in arrayOf(R.id.userInfoFragment,R.id.recycleBinFragment)){
                    bottomNavView.visibility = View.GONE
                }
                else
                    bottomNavView.visibility = View.VISIBLE
            }
        }
    }

    private fun setUpNavigation(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        controller = navHostFragment.navController
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_bar_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.searchView -> {
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return controller.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}