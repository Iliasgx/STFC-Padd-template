package com.iliasg.startrekfleetcommand.stfcpadd.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkManager
import androidx.work.await
import com.iliasg.startrekfleetcommand.core.R.style.Theme_STFCPadd
import com.iliasg.startrekfleetcommand.stfcpadd.BuildConfig
import com.iliasg.startrekfleetcommand.stfcpadd.R
import com.iliasg.startrekfleetcommand.stfcpadd.databinding.ActivityMainBinding
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.utils.Constants
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.workers.ConfigureWorker
import com.iliasg.startrekfleetcommand.stfcpadd.sync.domain.workers.SyncWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

@ExperimentalStdlibApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(Theme_STFCPadd)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpNavigation()

        // Prevent running api calls during debug version.
        if (!BuildConfig.DEBUG) {
            checkHasWork()
            setUpWorkers()
        }
    }

    private fun setUpNavigation() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.navigation_home/*,
                R.id.navigation_dashboard,
                R.id.navigation_library*/
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun checkHasWork() {
        CoroutineScope(Dispatchers.Default).launch {
            // Check if the update worker already has been enqueued.
            val workInfo = WorkManager.getInstance(applicationContext)
                .getWorkInfosForUniqueWork(Constants.WORKER_ID)
                .await()

            if (workInfo.isEmpty()) { // No work enqueued yet
                if (!hasNetwork()) { // Not connected to WiFi
                    Timber.i("Not connected to WiFi")
                    // Delay for 2 seconds to give users a chance to notice the toast when the
                    // app just launched.
                    delay(2000L)
                    runOnUiThread {
                        Toast.makeText(
                            this@MainActivity,
                            getString(R.string.no_network),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun hasNetwork(): Boolean {
        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Version 23+ holds network info as capabilities.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager.activeNetwork ?: return false
            val activeNetwork = manager.getNetworkCapabilities(network) ?: return false

            return activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)

        } else {
            // Use activeNetwork for lower versions. Only deprecated for Java.
            @Suppress("DEPRECATION")
            return manager.activeNetworkInfo?.isConnected ?: false
        }
    }

    private fun setUpWorkers() {
        WorkManager.getInstance(applicationContext)
            .beginUniqueWork(Constants.WORKER_ID, ExistingWorkPolicy.KEEP, ConfigureWorker.create)
            .then(SyncWorker.create)
            .enqueue()
    }
}