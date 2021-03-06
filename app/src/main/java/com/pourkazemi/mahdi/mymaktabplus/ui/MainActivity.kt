package com.pourkazemi.mahdi.mymaktabplus.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.NavHostFragment
import com.pourkazemi.mahdi.mymaktabplus.R
import com.pourkazemi.mahdi.mymaktabplus.data.localdetabase.data_store.PreferencesInfo
import com.pourkazemi.mahdi.mymaktabplus.util.logger
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val navController by lazy {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.FCcontainer) as NavHostFragment
        navHostFragment.navController
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preferencesInit()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.setting -> {
                navController.navigate(R.id.settingFragment)
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun preferencesInit() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val info = viewModel.preferences.first()
                logger("theme: ${info.theme} , lang: ${info.lang}")

                val mode = info.theme.mode
                val currentMode = AppCompatDelegate.getDefaultNightMode()
                Log.d("ahmadabadi", "preferencesInit: " + currentMode.toString())
                if (currentMode != mode) {
                    AppCompatDelegate.setDefaultNightMode(mode)
                }
            }
        }
        // job.join()
    }
}
