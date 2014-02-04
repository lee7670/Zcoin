package com.example.zcoins;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

public class Mineservice extends IntentService {

	public Mineservice() {
	      super("Mineservice");
	  }
	@Override
	protected void onHandleIntent(Intent arg0) {
		
		Intent coinget=new Intent();
		Bundle b=new Bundle();
		b.putInt("coins", 3);
		coinget.putExtra("coin", b);
		coinget.setAction("com.example.zcoins.coinBroadcast");
		sendBroadcast(coinget);
	}

}
