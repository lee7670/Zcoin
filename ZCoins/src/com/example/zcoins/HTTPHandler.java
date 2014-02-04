package com.example.zcoins;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

public class HTTPHandler {

	public static void updatebalance(String id, int amount) {



		try {
			URL url = new URL("http://54.201.221.41/" + id);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			urlConnection.setDoOutput(true);
			urlConnection.setChunkedStreamingMode(0);
			urlConnection.setRequestMethod("PUT");

			String s = "data=" + amount;

			BufferedOutputStream out = new BufferedOutputStream(
					urlConnection.getOutputStream());
			PrintWriter o = new PrintWriter(out);
			o.write(s);
			urlConnection.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getbalance(String id) {
		try {
			URL url = new URL("http://54.201.221.41/" + id);
			HttpURLConnection urlConnection = (HttpURLConnection) url
					.openConnection();
			InputStream in = new BufferedInputStream(
					urlConnection.getInputStream());
			BufferedReader n = new BufferedReader(new InputStreamReader(in));
			String l;
			String s = "";
			while ((l = n.readLine()) != null) {
				s += l;
			}
			Log.d(s, "string before json: ");
			JSONObject jsonObject = new JSONObject(s);

			JSONArray jArray = jsonObject.getJSONArray("list");
			Log.d(jArray.toString(), "json");
			return jArray.optInt(0);
		} catch (Exception e) {
		}
		return -1;
	}

	public static void login(String id) {
		if (getbalance(id) == -1) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost("http://54.201.221.41/" + id);
			try {
				// Add your data
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
				nameValuePairs.add(new BasicNameValuePair("data", "0"));
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

				HttpResponse response = httpclient.execute(httppost);

			} catch (Exception e) {
			}

		}
	}
}
