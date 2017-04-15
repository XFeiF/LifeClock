package me.xfeif.timetaken;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by XFeiF on 2017/4/2.
 */
public class LifeClock {

    private static String TEMPLATE = "yyyy-MM-dd";
    private static SimpleDateFormat sdf = new SimpleDateFormat(TEMPLATE, Locale.CHINA);


    private long getBirthdayInMilliseconds(String birthday){
        Date myBirthday = null;
        try {
            myBirthday = sdf.parse(birthday);
            //Log.d(TEMPLATE, myBirthday.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert myBirthday != null;
        return myBirthday.getTime();
    }

    /**
     * Implements a method thar returns the current time, in milliseconds
     * Used for testing
     */

    public interface GetTime{
        public long now();
    }

    /**
     * Default way to get time. Just use the system clock.
     */
    private GetTime SystemTime = new GetTime() {
        @Override
        public long now() {
            return System.currentTimeMillis();
        }
    };

    /**
     * What is the LifeClock doing?
     */
    private GetTime m_time;

    public LifeClock() {
        this.m_time = SystemTime;
    }

    public LifeClock(GetTime m_time) {
        this.m_time = m_time;
    }


    public long getElapsedTimeInMilliseconds(String birthday){
        return m_time.now() - getBirthdayInMilliseconds(birthday);
    }

    public String getElapsedTimeInYear(String birthday){
        DecimalFormat df = new DecimalFormat( "0.0000000000");

        return df.format(getElapsedTimeInMilliseconds(birthday)*1.0/1000/60/60/24/365);
    }

}
