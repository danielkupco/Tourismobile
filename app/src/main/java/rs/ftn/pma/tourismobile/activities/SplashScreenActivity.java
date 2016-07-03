package rs.ftn.pma.tourismobile.activities;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import rs.ftn.pma.tourismobile.R;

/**
 * Created by Daniel Kupčo on 03.07.2016.
 */
public class SplashScreenActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000; // splash ce biti vidljiv minimum SPLASH_TIME_OUT milisekundi

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        boolean splashOn = SP.getBoolean(getString(R.string.pref_turn_splash_key), true);
        if(!splashOn) {
            startMainActivity();
            return;
        }

        String splashDuration = SP.getString(getString(R.string.pref_splash_duration_key),
                getString(R.string.pref_splash_duration_default));
        SPLASH_TIME_OUT = Integer.valueOf(splashDuration) * 1000; // ms

        // uradi inicijalizaciju u pozadinksom threadu
        new InitTask().execute();
    }

    private class InitTask extends AsyncTask<Void, Void, Void>
    {
        private long startTime;

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            sleep();
            return null;
        }

        private void sleep() {
            // sacekaj tako da splash bude vidljiv minimum SPLASH_TIME_OUT milisekundi
            long timeLeft = SPLASH_TIME_OUT - (System.currentTimeMillis() - startTime);
            if(timeLeft < 0)
                timeLeft = 0;
            SystemClock.sleep(timeLeft);

            startMainActivity();
        }
    }

    private void startMainActivity() {
        MainActivity_.intent(this).start();
        finish(); // da ne bi mogao da ode back na splash
    }

}
