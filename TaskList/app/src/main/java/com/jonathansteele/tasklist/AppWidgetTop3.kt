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

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.getActivity
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.jonathansteele.tasklist.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AppWidgetTop3 : AppWidgetProvider() {
    private val job = SupervisorJob()
    private val coroutineScope = CoroutineScope(Dispatchers.IO + job)

    @OptIn(InternalCoroutinesApi::class)
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)

        if (context == null) {
            return
        }

        coroutineScope.launch {
            AppDatabase.getInstance(context).taskDao().getTopTaskNames(3).collect { notes ->
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val man = AppWidgetManager.getInstance(context)
                val ids = man.getAppWidgetIds(ComponentName(context, AppWidgetTop3::class.java))
                for (appWidgetId in ids) {
                    updateAppWidget(context, appWidgetManager, appWidgetId, notes)
                }
            }
        }
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        job.cancel()
    }

    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
        notes: List<String>
    ) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.app_widget)

        notes.firstOrNull()?.let {
            views.setTextViewText(R.id.task1TextView, it)
        } ?: views.setTextViewText(R.id.task1TextView, "")

        notes.getOrNull(1)?.let {
            views.setTextViewText(R.id.task2TextView, it)
        } ?: views.setTextViewText(R.id.task2TextView, "")

        notes.getOrNull(2)?.let {
            views.setTextViewText(R.id.task3TextView, it)
        } ?: views.setTextViewText(R.id.task3TextView, "")

        views.setOnClickPendingIntent(R.id.widget_layout, getPendingIntentActivity(context))

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntentActivity(context: Context): PendingIntent {
        // Construct an Intent which is pointing this class.
        val intent = Intent(context, MainActivity::class.java)
        // And this time we are sending a broadcast with getBroadcast
        return getActivity(context, 0, intent, FLAG_IMMUTABLE)
    }
}
