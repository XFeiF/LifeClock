package me.xfeif.timetaken;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RemoteViews;

import static me.xfeif.timetaken.MainActivity.BIRTHDAY_DAY;
import static me.xfeif.timetaken.MainActivity.BIRTHDAY_MONTH;
import static me.xfeif.timetaken.MainActivity.BIRTHDAY_YEAR;
import static me.xfeif.timetaken.MainActivity.DATE_PREFERENCE;

/**
 * The configuration screen for the {@link TimeTaken TimeTaken} AppWidget.
 */
public class TimeTakenConfigureActivity extends Activity {

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    private DatePicker birthdayPicker;
    private int year, month, day;
    private Button saveBtn;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = TimeTakenConfigureActivity.this;

            // When the button is clicked, store your birthday locally
            getBirthday();
            saveBirthday(context);

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews views = new RemoteViews(context.getPackageName(),R.layout.time_taken);
            appWidgetManager.updateAppWidget(mAppWidgetId, views);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public TimeTakenConfigureActivity() {
        super();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.time_taken_configure);
        birthdayPicker = (DatePicker) findViewById(R.id.birthdayPicker);
        saveBtn = (Button) findViewById(R.id.saveButton);
        saveBtn.setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        loadBirthdayPref(TimeTakenConfigureActivity.this);
    }

    private void getBirthday(){
        year = birthdayPicker.getYear();
        month = birthdayPicker.getMonth() + 1;
        day = birthdayPicker.getDayOfMonth();
    }

    private void saveBirthday(Context context){
        getBirthday();
        SharedPreferences.Editor editor = context.getSharedPreferences(DATE_PREFERENCE,
                                                Activity.MODE_PRIVATE).edit();
        editor.putInt(BIRTHDAY_YEAR, year);
        editor.putInt(BIRTHDAY_MONTH, month);
        editor.putInt(BIRTHDAY_DAY, day);
        editor.apply();
    }

    private void loadBirthdayPref(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(DATE_PREFERENCE, Activity.MODE_PRIVATE);
        year = sharedPreferences.getInt(BIRTHDAY_YEAR,-1);
        month = sharedPreferences.getInt(BIRTHDAY_MONTH, -1) - 1;
        day = sharedPreferences.getInt(BIRTHDAY_DAY, -1);
        if (year > 0 && month > 0 && day > 0 )
            birthdayPicker.updateDate(year,month,day);
    }
}

