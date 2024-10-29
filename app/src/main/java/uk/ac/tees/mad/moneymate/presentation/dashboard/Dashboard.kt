package uk.ac.tees.mad.moneymate.presentation.dashboard

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

@Composable
fun DashboardScreen(navController: NavHostController) {
    Column {


        Text(text = " ")
        FloatingActionButton(onClick = { navController.navigate("entry_screen") }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
        Button(onClick = { navController.navigate("category_screen") }) {
            Text(text = "Categories") // for now only
        }
    }

}