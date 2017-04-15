package me.xfeif.timetaken;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class MainActivity extends Activity {
    public static final String DATE_PREFERENCE = "me.xfeif.timetaken.LifeClock_Birthday";
    public static final String BIRTHDAY_YEAR = "LifeClock_year";
    public static final String BIRTHDAY_MONTH = "LifeClock_month";
    public static final String BIRTHDAY_DAY = "LifeClock_day";

    private SharedPreferences sharedPreferences;
    private DatePicker birthdayPicker;
    private int year, month, day;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences =  getSharedPreferences(DATE_PREFERENCE, Activity.MODE_PRIVATE);
        birthdayPicker = (DatePicker) findViewById(R.id.birthdayPicker);
        saveBtn = (Button) findViewById(R.id.saveButton);

        loadBirthdayPref();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBirthday();
                makeToast(getResources().getString(R.string.go_to_add));
            }
        });

    }

    private void getBirthday(){
        year = birthdayPicker.getYear();
        month = birthdayPicker.getMonth() + 1;
        day = birthdayPicker.getDayOfMonth();
    }

    private void saveBirthday(){
        getBirthday();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(BIRTHDAY_YEAR, year);
        editor.putInt(BIRTHDAY_MONTH, month);
        editor.putInt(BIRTHDAY_DAY, day);
        editor.commit();
    }

    private void makeToast(String msg){
        Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
    }

    private void loadBirthdayPref(){
        year = sharedPreferences.getInt(BIRTHDAY_YEAR,-1);
        month = sharedPreferences.getInt(BIRTHDAY_MONTH, -1) - 1;
        day = sharedPreferences.getInt(BIRTHDAY_DAY, -1);
        if (year > 0 && month > 0 && day > 0 )
            birthdayPicker.updateDate(year,month,day);
    }
}
