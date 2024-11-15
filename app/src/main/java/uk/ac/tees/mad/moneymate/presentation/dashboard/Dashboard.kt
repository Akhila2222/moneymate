package uk.ac.tees.mad.moneymate.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun DashboardScreen(navController: NavHostController) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("entry_screen") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        bottomBar = {
            BottomAppBar {
                NavigationBar {
                    NavigationBarItem(
                        selected = navController.currentBackStackEntry?.destination?.route == "category_screen",
                        onClick = { navController.navigate("category_screen") },
                        icon = {
                            Icon(imageVector = Icons.Default.Category, contentDescription = null)
                        },
                        label = {
                            Text(text = "Categories")
                        }
                    )
                }
            }
        }
    ) {
        Column(Modifier.padding(it)) {

        }
    }

}