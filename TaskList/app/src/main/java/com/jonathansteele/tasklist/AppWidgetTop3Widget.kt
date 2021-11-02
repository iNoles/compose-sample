package com.jonathansteele.tasklist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.LocalContext
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.background
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.TextStyle
import com.jonathansteele.tasklist.data.AppDatabase

class AppWidgetTop3Widget: GlanceAppWidget() {
    @Composable
    override fun Content() {
        val context = LocalContext.current
        val notes = AppDatabase.getInstance(context).taskDao().getTopTaskNames(3)
            .collectAsState(initial = emptyList()).value
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(color = Color.White)
                .padding(8.dp)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = GlanceModifier.fillMaxWidth(),
                style = TextStyle(fontWeight = FontWeight.Bold),
            )
            notes.firstOrNull()?.let {
                Text(text = it)
            } ?: Text(text = "")

            notes.getOrNull(1)?.let {
                Text(text = it)
            } ?: Text(text = "")

            notes.getOrNull(2)?.let {
                Text(text = it)
            } ?: Text(text = "")
        }
    }
}