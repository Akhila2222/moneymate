package uk.ac.tees.mad.moneymate.presentation.category

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uk.ac.tees.mad.moneymate.database.Category

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    navController: NavHostController, categoryViewModel: CategoryViewModel = hiltViewModel()
) {
    val categories by categoryViewModel.categories.collectAsState(initial = emptyList())
    var categoryName by remember { mutableStateOf("") }
    var selectedCategory: Category? by remember { mutableStateOf(null) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
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
        },
        topBar = {
            TopAppBar(title = {
                Text(text = "Manage Categories", fontSize = 24.sp, fontWeight = FontWeight.Medium)
            })
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Input for category name
            OutlinedTextField(
                value = categoryName,
                onValueChange = { categoryName = it },
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    coroutineScope.launch {
                        if (selectedCategory == null) {
                            categoryViewModel.addCategory(Category(name = categoryName))
                        } else {
                            categoryViewModel.updateCategory(selectedCategory!!.copy(name = categoryName))
                            selectedCategory = null
                        }
                        categoryName = ""
                    }
                }) {
                    Text(text = if (selectedCategory == null) "Add Category" else "Update Category")
                }

                if (selectedCategory != null) {

                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(12.dp))
            // List of categories
            Text(text = "Categories", fontSize = 18.sp, fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(8.dp))
            if (categories.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {

                    Text(text = "No added categories", style = MaterialTheme.typography.labelSmall)
                }
            }
            categories.forEach { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                        .clickable {
                            selectedCategory = category
                            categoryName = category.name
                        },
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = category.name,
                        fontSize = 16.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(start = 16.dp),
                        color = MaterialTheme.colorScheme.onSecondary

                    )
                    IconButton(onClick = {
                        coroutineScope.launch {
                            categoryViewModel.deleteCategory(category)
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }
            }
        }
    }
}
