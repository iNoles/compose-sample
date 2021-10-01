/*
 * Copyright 2021 Jonathan Steele
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
package com.jonathansteele.tasklist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Checkbox
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import com.jonathansteele.tasklist.data.AppDatabase
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePagerTabs(navController: NavHostController) {
    val pages = populateList()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Task List") },
                actions = {
                    IconButton(
                        onClick = {
                            navController.navigate("add") {
                                popUpTo("home")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = "Add Tasks"
                        )
                    }
                }
            )
        }
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            val pagerState = rememberPagerState()
            val coroutineScope = rememberCoroutineScope()
            TabRow(
                // Our selected tab is our current page
                selectedTabIndex = pagerState.currentPage,
                // Override the indicator, using the provided pagerTabIndicatorOffset modifier
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions)
                    )
                }
            ) {
                // Add tabs for all of our pages
                pages.forEachIndexed { index, list ->
                    Tab(
                        text = { Text(text = list.name) },
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch { pagerState.animateScrollToPage(index) }
                        }
                    )
                }
            }
            HorizontalPager(state = pagerState, count = 2) { page ->
                PagerContent(listId = page + 1, navController)
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PagerContent(listId: Int, navController: NavHostController) {
    val context = LocalContext.current
    val taskDao = AppDatabase.getInstance(context).taskDao()
    val tasks = taskDao.getTasks(listId)
    val taskState = tasks.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    LazyColumn {
        items(taskState.value) { task ->
            ListItem(
                modifier = Modifier.clickable
                {
                    navController.navigate("edit/${task.listId}")
                },
                text = { Text(text = task.name) },
                secondaryText = { Text(text = task.notes) },
                trailing = {
                    Checkbox(
                        checked = task.getCompletedDateMillis() > 0,
                        onCheckedChange = {
                            if (it) {
                                task.completedDate = System.currentTimeMillis().toString()
                            } else {
                                task.completedDate = 0.toString()
                            }
                            coroutineScope.launch { taskDao.updateTask(task) }
                        }
                    )
                }
            )
            Divider()
        }
    }
}
