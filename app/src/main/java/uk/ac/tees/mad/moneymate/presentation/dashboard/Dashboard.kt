package uk.ac.tees.mad.moneymate.presentation.dashboard

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import uk.ac.tees.mad.moneymate.database.Expense

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
            FloatingActionButton(onClick = { navController.navigate("entry_screen/-1") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        },
        bottomBar = {
            BottomAppBar {
                NavigationBar {
                    NavigationBarItem(
                        selected = navController.currentBackStackEntry?.destination?.route == "dashboard_screen",
                        onClick = { navController.navigate("dashboard_screen") },
                        icon = {
                            Icon(imageVector = Icons.Default.Dashboard, contentDescription = null)
                        },
                        label = {
                            Text(text = "Dashboard")
                        }
                    )
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
                    NavigationBarItem(
                        selected = navController.currentBackStackEntry?.destination?.route == "profile_screen",
                        onClick = { navController.navigate("profile_screen") },
                        icon = {
                            Icon(imageVector = Icons.Default.Person, contentDescription = null)
                        },
                        label = {
                            Text(text = "Profile")
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
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
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

//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        horizontalArrangement = Arrangement.SpaceBetween
//                    ) {
//
//                        // Income Breakdown
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//                            Text(
//                                text = "Income Breakdown",
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Medium
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            CategoryPieChart(
//                                categoryData = dashboardState.incomeCategoryData,
//                                isIncome = true
//                            )
//                        }
//                        Spacer(modifier = Modifier.height(24.dp))
//
//                        // Expense Breakdown
//                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//
//                            Text(
//                                text = "Expense Breakdown",
//                                fontSize = 20.sp,
//                                fontWeight = FontWeight.Medium
//                            )
//                            Spacer(modifier = Modifier.height(8.dp))
//                            CategoryPieChart(
//                                categoryData = dashboardState.expenseCategoryData,
//                                isIncome = false
//                            )
//                        }
//                    }
                    ExpenseIncomeChartView(dashboardState = dashboardState)
                    Spacer(modifier = Modifier.height(24.dp))

                    // Recent Transactions
                    Text(
                        text = "Recent Transactions",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    dashboardState.recentTransactions.forEach { expense ->
                        TransactionItem(
                            expense = expense,
                            onEditClick = { navController.navigate("entry_screen/${expense.id}") },
                            onDeleteClick = { viewModel.deleteExpense(expense) }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

            }
        }
    }

}

@Composable
fun ExpenseIncomeChartView(
    dashboardState: DashboardState,
) {
    var showIncomeChart by remember { mutableStateOf(true) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        // Toggle between Income and Expense
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Column(modifier = Modifier.width(IntrinsicSize.Max)) {

                TextButton(
                    onClick = { showIncomeChart = true },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (showIncomeChart) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Text(text = "Income", fontSize = 16.sp)
                }

                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(
                            if (showIncomeChart)
                                MaterialTheme.colorScheme.primary else Color.Transparent
                        )
                )

            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.width(IntrinsicSize.Max)) {

                TextButton(
                    onClick = { showIncomeChart = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = if (!showIncomeChart) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Text(text = "Expense", fontSize = 16.sp)
                }

                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .fillMaxWidth()
                        .background(
                            if (!showIncomeChart)
                                MaterialTheme.colorScheme.primary else Color.Transparent
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CategoryPieChart(
                categoryData = if (showIncomeChart) dashboardState.incomeCategoryData else dashboardState.expenseCategoryData,
                isIncome = showIncomeChart
            )
        }

    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryPieChart(categoryData: Map<String, Double>, isIncome: Boolean) {
    val total = categoryData.values.sum()

    if (total == 0.0) {
        Text(text = "No Data Available")
        return
    }

    val chartColors = if (isIncome) {
        listOf(
            Color(0xFF4CAF50),
            Color(0xFF8BC34A),
            Color(0xFFCDDC39),
            Color(0xFFFFEB3B),
            Color(0xFFFFC107)
        )
    } else {
        listOf(
            Color(0xFFF44336),
            Color(0xFFE91E63),
            Color(0xFF9C27B0),
            Color(0xFF673AB7),
            Color(0xFF3F51B5)
        )
    }

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
    FlowRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        categoryData.forEach { (category, amount) ->
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Canvas(modifier = Modifier.size(16.dp)) {
                    drawCircle(color = chartColors[categoryData.keys.indexOf(category) % chartColors.size])
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "$category: $$amount")
            }
        }
    }
}

@Composable
fun TransactionItem(
    expense: Expense,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = expense.category, fontWeight = FontWeight.Bold)
                Text(text = expense.description, fontSize = 14.sp, color = Color.Gray)
                Text(
                    text = "$${expense.amount}",
                    color = if (expense.isIncome) Color.Green else Color.Red
                )
            }
            Row {
                IconButton(onClick = onEditClick) {
                    Icon(Icons.Default.Edit, contentDescription = "Edit")
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                }
            }
        }
    }
}