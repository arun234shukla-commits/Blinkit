package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppViewModel
import com.example.ui.screens.*
import com.example.ui.theme.QuickBasketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuickBasketTheme {
                val viewModel: AppViewModel = viewModel()
                val currentRoute by viewModel.currentRoute.collectAsState()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AnimatedContent(
                        targetState = currentRoute,
                        label = "MainRouteTransition"
                    ) { route ->
                        when (route) {
                            "splash" -> SplashScreen(viewModel)
                            "onboarding" -> OnboardingScreen(viewModel)
                            "auth" -> AuthScreen(viewModel)
                            "main" -> MainCustomerScreen(viewModel)
                            "product_detail" -> ProductDetailScreen(viewModel)
                            "order_tracking" -> OrderTrackingScreen(viewModel)
                            "admin" -> AdminDashboardScreen(viewModel)
                            else -> SplashScreen(viewModel)
                        }
                    }
                }
            }
        }
    }
}
