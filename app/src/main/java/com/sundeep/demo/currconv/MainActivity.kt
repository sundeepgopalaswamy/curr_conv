package com.sundeep.demo.currconv

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.sundeep.demo.currconv.databinding.ActivityMainBinding
import com.sundeep.demo.currconv.ui.compose.MainCompose
import com.sundeep.demo.currconv.ui.theme.CurrencyConverterTheme
import com.sundeep.demo.currconv.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        const val USE_COMPOSE = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (USE_COMPOSE) {
            useComposeUI()
        } else {
            useViewUI()
        }
    }

    private fun useViewUI() {
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    private fun useComposeUI() {
        val mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setContent {
            CurrencyConverterTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                ) { innerPadding ->
                    val windowSizeClass = calculateWindowSizeClass(activity = this)
                    MainCompose().MainScreen(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = mainViewModel,
                        windowSize = windowSizeClass
                    )
                }
            }
        }
    }
}