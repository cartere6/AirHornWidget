package com.flynnsam.airhornwidget.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.RemoteViews;

import com.flynnsam.airhornwidget.R;
import com.flynnsam.airhornwidget.mediaplayer.MediaPlayerProvider;

/**
 * Created by Sam on 2017-01-20.
 */

public class AirHornAppWidgetProvider extends AppWidgetProvider {

    private static final String PLAY_ACTION = "com.flynnsam.airhornwidget.PlayAction";

    @Override
    public void onUpdate(Context context, AppWidgetManager widgetManager, int[] appWidgetIds) {

        for (int widgetId : appWidgetIds) {

            int buttonID = R.id.airhornblaster;
            int layoutID = R.layout.widgetbutton;

            Intent playAirHornIntent = new Intent(context, this.getClass());
            playAirHornIntent.setAction(PLAY_ACTION);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, 0, playAirHornIntent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), layoutID);
            views.setOnClickPendingIntent(buttonID, pIntent);

            widgetManager.updateAppWidget(widgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (PLAY_ACTION.equals(intent.getAction())) {
            MediaPlayerProvider.play(context);
        }
    }
}
