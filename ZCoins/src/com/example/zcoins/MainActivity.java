package com.example.zcoins;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity 
{
	public static SharedPreferences prefs;
	public static Editor editor;
	public static String id = "";
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        prefs = getPreferences(MODE_PRIVATE);
        editor = prefs.edit();
        editor.clear();
        editor.commit();
        id = prefs.getString("id", "");
        setContentView(R.layout.activity_main);
    }
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void mineZCoins(View view)
    {
    	Intent intent = new Intent(this, Mine.class);
    	startActivity(intent);
    }
    
    public void friendsView(View view)
    {
    	Intent intent = new Intent(this, FriendsList.class);
    	startActivity(intent);
    }
}
