package com.example.zcoins;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.os.Bundle;
import android.os.PowerManager;
import android.os.SystemClock;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Chronometer;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class Mine extends Activity implements SensorEventListener {
	private Chronometer timer;
	private static int coins = 0;
	public static SensorManager mSensorManager;
	public static boolean mInitialized;
	public static Sensor mAccelerometer;
	private float mLastX, mLastY, mLastZ;
	private final float NOISE = (float) 2.0;
	public static ArrayList<Float> calibrator = new ArrayList<Float>();
	public static boolean movingUp = true;
	public static float threshold = 0;
	public static boolean alreadyNoted = false;
	public static TextView coinBalanceText;
	private PowerManager.WakeLock mWakeLock;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mine);
		// Show the Up button in the action bar.
		setupActionBar();
		timer = (Chronometer) findViewById(R.id.chronometer1);
		timer.setBase(SystemClock.elapsedRealtime());
		timer.start();
        coins = MainActivity.prefs.getInt("balance", 0);
		coinBalanceText = (TextView) findViewById(R.id.coin_count);
		coinBalanceText.setText("Z-Coins:" + coins);
		//TextView myTextView1 = (TextView) findViewById(R.id.lastcoin);
		//myTextView1.setText("Coin found " + timesince + "s ago.");
		PowerManager p=(PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock=p.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "Mine");
		mWakeLock.acquire();
		mInitialized = false;

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		SelectionSorter.makeList();
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	protected void onResume() {
		super.onResume();
		coinBroadcast c = new coinBroadcast();
		TextView myTextView = (TextView) findViewById(R.id.coin_count);
		myTextView.setText("Z-Coins:" + coins);
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_NORMAL);
		mWakeLock.acquire();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mine, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public static void addCoins(int i) {
		coins += i;
		coinBalanceText.setText("Z-Coins:" + coins);
        new DownloadWebpageTask().execute("http://54.201.221.41/" + MainActivity.id, "PUT", "data=" + coins);
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	public void adjustGait(float average) {
		float minimumRate = 0.8f;
		threshold = average * minimumRate;
	}

	public void startAnalysis(float[] graph) {
		if (calibrator.size() < 10) {
			int sum1 = 0;
			for (float value : graph) {
				sum1 += (float) (value * value);
			}
			float magnitude = (float) Math.sqrt(sum1);
			calibrator.add(magnitude);
		} else if (threshold == 0) {
			float sum = 0;
			for (float v : calibrator) {
				sum += v;
			}
			adjustGait(sum / calibrator.size());
		}
	}

	public void detectStep(float[] graph) {
		if (calibrator.size() >= 10) {
			float x = graph[0];
			float y = graph[1];
			float z = graph[2];
			float magnitude = (float) Math.sqrt(x * x + y * y + z * z);
			if (magnitude > threshold && !alreadyNoted) {
				alreadyNoted = true;
				SelectionSorter.iterate();
			} else if (magnitude < threshold && alreadyNoted) {
				alreadyNoted = false;
			}
		}
	}

	public void onSensorChanged(SensorEvent event) {
		
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		} else {
			float deltaX = Math.abs(mLastX - x);
			float deltaY = Math.abs(mLastY - y);
			float deltaZ = Math.abs(mLastZ - z);
			if (deltaX < NOISE){
				deltaX = (float) 0.0;
			}
			if (deltaY < NOISE){
				deltaY = (float) 0.0;
			}
			if (deltaZ < NOISE){
				deltaZ = (float) 0.0;
			}
			mLastX = x;
			mLastY = y;
			mLastZ = z;
		}
		float[] graph = new float[3];
		graph[0] = mLastX;
		graph[1] = mLastY;
		graph[2] = mLastZ;
		startAnalysis(graph);
		detectStep(graph);
	}
	@Override
	protected void onDestroy()
	{
	
		super.onDestroy();
	}
}
