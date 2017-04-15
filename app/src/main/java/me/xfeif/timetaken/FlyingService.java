package me.xfeif.timetaken;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

/**
 * Created by XFeiF on 2017/4/15.
 */
public class FlyingService extends Service{
    private static final int ALARM_DURATION = 2*60*1000;
    private static final int UPDATE_DURATION = 1;
    private static final int UPDATE_MESSAGE = 1000;
    private static final LifeClock mClock = new LifeClock();

    private UpdateHandler updateHandler;

    private static String mElapsedTime;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 每个ALARM_DURATION 自启一次
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(getBaseContext(), FlyingService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getBaseContext(), 0,
                alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + ALARM_DURATION, pendingIntent);

        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        updateHandler = new UpdateHandler();
        Message message = updateHandler.obtainMessage();
        message.what = UPDATE_MESSAGE;
        updateHandler.sendMessageDelayed(message, UPDATE_DURATION);
    }

    private void updateWidget(){
        // 更新Widget
        Context context = getApplicationContext();
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.time_taken);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);

        mElapsedTime = mClock.getElapsedTimeInYear();
        remoteViews.setTextViewText(R.id.appwidget_text, mElapsedTime);

        appWidgetManager.updateAppWidget(new ComponentName(context, TimeTaken.class), remoteViews);

        // 发送下次更新的消息
        Message message = updateHandler.obtainMessage();
        message.what = UPDATE_MESSAGE;
        updateHandler.sendMessageDelayed(message, UPDATE_DURATION);
    }

    protected final class UpdateHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case UPDATE_MESSAGE:
                    updateWidget();
                    break;
                default:break;
            }
        }
    }
}
