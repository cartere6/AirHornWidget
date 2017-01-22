package com.flynnsam.airhornwidget.provider;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.widget.RemoteViews;

import com.flynnsam.airhornwidget.R;
import com.flynnsam.airhornwidget.mediaplayer.MediaPlayerProvider;

/**
 * The widget provider that registers the button's action and handles that action's reception.
 *
 * Created by Sam on 2017-01-20.
 */
public class AirHornAppWidgetProvider extends AppWidgetProvider {

    private static final String PLAY_ACTION = "com.flynnsam.airhornwidget.PlayAction";

    public static final String MAX_VOLUME_PREFERENCE_KEY = "com.flynnsam.airhornwidget.MaxVolumeOnPress";

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
            handleMaxVolumePref(context);
            MediaPlayerProvider.play(context);
        }
    }

    protected void handleMaxVolumePref(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(
                context.getResources().getString(R.string.preference_repo_name),
                Context.MODE_PRIVATE);

        boolean setMaxVol = prefs.getBoolean(
                context.getResources().getString(R.string.max_volume_preference_key), false);

        if (setMaxVol) {
            AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        }
    }
}
