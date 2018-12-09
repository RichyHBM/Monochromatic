package uk.co.richyhbm.monochromatic.Widgets

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import uk.co.richyhbm.monochromatic.R
import uk.co.richyhbm.monochromatic.Receivers.DisableMonochromeReceiver
import kotlin.random.Random


class DisableScreenOnWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        
        for (appWidgetId in appWidgetIds) {

            val disableIntent = Intent(context, DisableMonochromeReceiver::class.java)
            val pendingDisableIntent = PendingIntent.getBroadcast(context, Random.nextInt(), disableIntent, 0)

            val views = RemoteViews(context.packageName, R.layout.button_widget)
            views.setOnClickPendingIntent(R.id.button_widget_layout, pendingDisableIntent)

            views.setImageViewResource(R.id.button_widget_imageView, R.drawable.ic_invert_colors_off_black)
            views.setTextViewText(R.id.button_widget_textView, "Disable until screen off")

            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}