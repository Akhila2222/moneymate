package uk.ac.tees.mad.moneymate.presentation.dashboard

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController

@Composable
fun DashboardScreen(
    navController: NavHostController,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val dashboardState by viewModel.dashboardState.collectAsState()
    val errorMessage by viewModel.error.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Dashboard", fontSize = 24.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Total Income and Expense Summary
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Income: $${dashboardState.totalIncome}",
                            color = Color.Green,
                            fontSize = 18.sp
                        )
                        Text(
                            text = "Total Expense: $${dashboardState.totalExpense}",
                            color = Color.Red,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pie Chart
                    Text(
                        text = "Breakdown by Category",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CategoryPieChart(categoryData = dashboardState.categoryData)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Savings Goal Progress
                    Text(
                        text = "Savings Goal Progress",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { dashboardState.savingsProgress.toFloat() },
                        modifier = Modifier.fillMaxWidth(),
                        color = if (dashboardState.savingsProgress >= 1.0) Color.Green else Color.Blue,
                    )
                    Text(
                        text = "${(dashboardState.savingsProgress * 100).toInt()}% of Savings Goal",
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

            }
        }
    }

}


@Composable
fun CategoryPieChart(categoryData: Map<String, Double>) {
    val total = categoryData.values.sum()

    if (total == 0.0) {
        Text(text = "No Data Available")
        return
    }

    val chartColors = listOf(
        Color(0xFF6200EE), Color(0xFF03DAC5), Color(0xFFBB86FC), Color(0xFFFF5722),
        Color(0xFFFFC107), Color(0xFF4CAF50), Color(0xFFFFEB3B)
    )

    val proportions = categoryData.map { (_, amount) -> amount / total }
    val sweepAngles = proportions.map { 360f * it }

    Canvas(
        modifier = Modifier
            .size(200.dp)
            .padding(16.dp)
    ) {
        var startAngle = 0f
        sweepAngles.forEachIndexed { index, sweepAngle ->
            drawArc(
                color = chartColors[index % chartColors.size],
                startAngle = startAngle,
                sweepAngle = sweepAngle.toFloat(),
                useCenter = true
            )
            startAngle += sweepAngle.toFloat()
        }
    }

    // Category labels
    Column {
        categoryData.forEach { (category, amount) ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Canvas(modifier = Modifier.size(16.dp)) {
                    drawCircle(color = chartColors[categoryData.keys.indexOf(category) % chartColors.size])
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "$category: €$amount")
            }
        }
    }
}