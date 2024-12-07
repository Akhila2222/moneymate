package uk.ac.tees.mad.moneymate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.moneymate.presentation.auth.AuthScreen
import uk.ac.tees.mad.moneymate.presentation.category.CategoryScreen
import uk.ac.tees.mad.moneymate.presentation.dashboard.DashboardScreen
import uk.ac.tees.mad.moneymate.presentation.expenseentry.ExpenseEntryScreen
import uk.ac.tees.mad.moneymate.presentation.profile.ProfileScreen
import uk.ac.tees.mad.moneymate.presentation.splash.SplashScreen
import uk.ac.tees.mad.moneymate.ui.theme.MoneyMateTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoneyMateTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = "splash_screen") {
                    composable("splash_screen") {
                        SplashScreen(navController)
                    }
                    composable("login_screen") {
                        AuthScreen(navController)
                    }
                    composable("dashboard_screen") {
                        DashboardScreen(navController)
                    }
                    composable("entry_screen/{expenseId}") {
                        val expenseId = it.arguments?.getString("expenseId")?.toLongOrNull()
                        ExpenseEntryScreen(navController, expenseId)
                    }
                    composable("category_screen") {
                        CategoryScreen(navController)
                    }
                    composable("profile_screen") {
                        ProfileScreen(navController)
                    }
                }
            }
        }
    }
}