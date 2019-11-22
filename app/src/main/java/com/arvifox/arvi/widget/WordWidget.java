package com.arvifox.arvi.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.text.format.Time;
import android.widget.RemoteViews;

import com.arvifox.arvi.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // To prevent any ANR timeouts, we perform the update in a service
        context.startService(new Intent(context, UpdateService.class));
    }

    public static class UpdateService extends Service {
        @Override
        public void onStart(Intent intent, int startId) {
            // Build the widget update for today
            RemoteViews updateViews = buildUpdate(this);
            // Push update for this widget to the home screen
            ComponentName thisWidget = new ComponentName(this, WordWidget.class);
            AppWidgetManager manager = AppWidgetManager.getInstance(this);
            manager.updateAppWidget(thisWidget, updateViews);
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        private static final String WOTD_PATTERN =
                "(?s)\\{\\{wotd\\|(.+?)\\|(.+?)\\|([^#\\|]+).*?\\}\\}";

        public RemoteViews buildUpdate(Context context) {
            // Pick out month names from resources
            Resources res = context.getResources();
            String[] monthNames = res.getStringArray(R.array.strarra);
            // Find current month and day
            Time today = new Time();
            today.setToNow();
            String pageName = "asasas";
            String pageContent = null;
            pageContent = "ererere";
            RemoteViews views = null;
            Matcher matcher = Pattern.compile(WOTD_PATTERN).matcher(pageContent);
            if (matcher.find()) {
                // Build an update that holds the updated widget contents
                views = new RemoteViews(context.getPackageName(), R.layout.widget_test);
                String wordTitle = matcher.group(1);
//                views.setTextViewText(R.id.word_title, wordTitle);
//                views.setTextViewText(R.id.word_type, matcher.group(2));
//                views.setTextViewText(R.id.definition, matcher.group(3).trim());
                String definePage = "qwqwqw";
                Intent defineIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(definePage));
                PendingIntent pendingIntent = PendingIntent.getActivity(context,
                        0 /* no requestCode */, defineIntent, 0 /* no flags */);
//                views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            } else {
                // Didn't find word of day, so show error message
                views = new RemoteViews(context.getPackageName(), R.layout.widget_test);
                views.setTextViewText(R.id.message, "rrrr");
            }
            return views;
        }
    }
}