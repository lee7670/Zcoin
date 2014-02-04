package com.example.zcoins;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class coinBroadcast extends BroadcastReceiver
{
	@Override
	public void onReceive(Context context, Intent intent)
	{
	int i=intent.getBundleExtra("coin").getInt("coins");
	Mine.addCoins(i);
	}
	
}