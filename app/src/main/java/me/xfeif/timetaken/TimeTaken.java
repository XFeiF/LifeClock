package me.xfeif.timetaken;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link TimeTakenConfigureActivity TimeTakenConfigureActivity}
 */
public class TimeTaken extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, FlyingService.class));
    }
}

