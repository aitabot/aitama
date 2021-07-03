package com.example.aitama

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.aitama.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var receiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /* Sets up data binding */
        val binding =
            DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        /* Sets up the Navigation Drawer, and the up-button */
        drawerLayout = binding.drawerLayout
        val navController = this.findNavController(R.id.myNavHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val action = intent?.getStringExtra("action");
                val asset = intent?.getStringExtra("asset");
                val amount = intent?.getStringExtra("amount");
                Log.d("MainActivity:Intent", intent.toString())
                Log.d("MainActivity:Notification:Action", action.toString())
                Log.d("MainActivity:Notification:Asset", asset.toString())
                Log.d("MainActivity:Notification:Amount", amount.toString())
                AlertDialog
                    .Builder(this@MainActivity)
                    .setMessage("AI suggests to $action $amount pieces of $asset.")
                    .setTitle("$action $asset")
                    .setPositiveButton("OK") { _, _ ->
                        run {
                            navController.navigate(R.id.action_portfolioFragment_to_detailFragment)
                        }
                    }
                    .setNegativeButton("Ignore") { _, _ -> }
                    .create().show()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity", "onPause")
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onResume() {
        super.onResume()
        val filter = IntentFilter(MyFirebaseMessagingService.INTENT_ACTION_SEND_MESSAGE)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }


}