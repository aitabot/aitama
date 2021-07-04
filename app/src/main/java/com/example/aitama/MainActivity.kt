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
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.aitama.databinding.ActivityMainBinding
import com.example.aitama.fragments.PortfolioFragmentDirections
import com.example.aitama.util.AssetType
import com.example.aitama.util.TransactionType


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
                showDialog(intent, navController)
                // see commentary in onResume
                intent?.removeExtra("asset")
            }
        }
    }

    private fun showDialog(
        intent: Intent?,
        navController: NavController
    ) {
        val action = intent?.getStringExtra("action");
        val asset = intent?.getStringExtra("asset");
        val amount = intent?.getStringExtra("amount");
        val symbol = intent?.getStringExtra("symbol");
        val type = intent?.getStringExtra("type");
        Log.d("MainActivity:Intent", intent.toString())
        Log.d("MainActivity:Notification:Action", action.toString())
        Log.d("MainActivity:Notification:Asset", asset.toString())
        Log.d("MainActivity:Notification:Amount", amount.toString())
        Log.d("MainActivity:Notification:symbol", symbol.toString())
        Log.d("MainActivity:Notification:type", type.toString())
        if (action != null && type != null && symbol != null && asset != null) {
            AlertDialog
                .Builder(this@MainActivity)
                .setMessage("AI suggests to $action $amount pieces of $asset.")
                .setTitle("$action $asset")
                .setPositiveButton("OK") { _, _ ->
                    run {
                        navController.navigate(R.id.portfolioFragment) // navigate to the portfolio first to guarantee that the navigation action below is valid
                        navController.navigate(
                            PortfolioFragmentDirections.actionPortfolioFragmentToTransactionFragment(
                                symbol,
                                if (action.uppercase() == "BUY") TransactionType.BUY
                                else TransactionType.SELL,
                                asset,
                                if (type.uppercase() == "STOCK") AssetType.STOCK
                                else AssetType.CRYPTO
                            )
                        )
                    }
                }
                .setNegativeButton("Ignore") { _, _ -> }
                .create().show()
        }
    }

    override fun onPause() {
        super.onPause()
        Log.d("MainActivity:onPause", "")
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver)
    }

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity:onResume", "")
        val filter = IntentFilter(MyFirebaseMessagingService.INTENT_ACTION_SEND_MESSAGE)
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter)
        // asset is also used as a "flag" to know if the intent has already been shown.
        // if it has been shown already, remove it from the intent so the intent will not be shown again.
        if (intent.hasExtra("asset")) {
            showDialog(intent, this.findNavController(R.id.myNavHostFragment))
            intent.removeExtra("asset")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.myNavHostFragment)
        return NavigationUI.navigateUp(navController, drawerLayout)
    }


}