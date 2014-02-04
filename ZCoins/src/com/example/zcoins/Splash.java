package com.example.zcoins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class Splash extends Activity {

    private final int SPLASH_DISPLAY_LENGHT = 2000;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        prefs = getPreferences(MODE_PRIVATE);
        prefs.edit().clear().commit();

        if (prefs.getBoolean("logged_in", false)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                    Intent mainIntent = new Intent(Splash.this, MainActivity.class);
                    Splash.this.startActivity(mainIntent);
                    Splash.this.finish();
                }
            }, SPLASH_DISPLAY_LENGHT);
        }else{
            Button login = (Button) findViewById(R.id.loginbtn);
            login.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    public void login(View view){
        editor = prefs.edit();
        TelephonyManager tMgr = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        editor.putBoolean("logged_in", true);
        editor.putString("id", mPhoneNumber);
        MainActivity.id = mPhoneNumber;
        editor.putInt("balance", 0);
        editor.commit();
        Intent mainIntent = new Intent(this, MainActivity.class);
        this.startActivity(mainIntent);
        this.finish();
    }

}
