package com.zeal.peak.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.zeal.peekaboo.PeakAboo;

public class CommonUtils {

	private static Context m_context;
	static Bitmap m_bitmap;

	/*
	 * Call the Webservice read the Json response and return the response in
	 * string.
	 */
	public static String parseJSON(String p_url) {
		JSONObject jsonObject = null;
		String json = null;
		try {
			// Create a new HTTP Client
			DefaultHttpClient defaultClient = new DefaultHttpClient();
			// Setup the get request
			HttpGet httpGetRequest = new HttpGet(PeakAboo.BaseUrl + p_url);
			System.out.println("Request URL--->" + PeakAboo.BaseUrl + p_url);
			// Execute the request in the client
			HttpResponse httpResponse = defaultClient.execute(httpGetRequest);
			// Grab the response
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					httpResponse.getEntity().getContent(), "UTF-8"));
			json = reader.readLine();
			System.err.println("JSON Response--->" + json);
			// Instantiate a JSON object from the request response
			jsonObject = new JSONObject(json);

		} catch (Exception e) {
			// In your production code handle any errors and catch the
			// individual exceptions
			e.printStackTrace();
		}
		return json;
	}

	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = 12;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	public static boolean IsNetConnected() {
		boolean NetConnected = false;

		try {
			ConnectivityManager connectivity = (ConnectivityManager) m_context
					.getSystemService(m_context.CONNECTIVITY_SERVICE);

			if (connectivity == null) {
				NetConnected = false;
			} else {
				NetworkInfo[] info = connectivity.getAllNetworkInfo();

				if (info != null) {
					for (int i = 0; i < info.length; i++) {
						if (info[i].getState() == NetworkInfo.State.CONNECTED) {
							NetConnected = true;
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			NetConnected = false;
		}

		return NetConnected;
	}

	public static boolean checkConnection(Context context) {
		final ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo == null || !networkInfo.isConnectedOrConnecting()) {
			Toast.makeText(context, "No Network available.", Toast.LENGTH_LONG).show();
//			Log.e("TAG", "checkConnection - no connection found");
			return false;
		} else
			return true;
	}

	public static Bitmap loadImage(String image_location) {

		URL imageURL = null;

		try {
			imageURL = new URL(image_location);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			HttpURLConnection connection = (HttpURLConnection) imageURL
					.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream inputStream = connection.getInputStream();

			m_bitmap = BitmapFactory.decodeStream(inputStream);// Convert to
																// bitmap

			// image_view.setImageBitmap(m_bitmap);
		} catch (IOException e) {

			e.printStackTrace();
		}
		return m_bitmap;
	}

	public static void setStringSharedPref(Context mContext, String spKey,
			String value) {
		SharedPreferences m_sharePref = mContext.getSharedPreferences(
				"PEAKABOO_PREFS", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = m_sharePref.edit();
		editor.putString(spKey, value);
		editor.commit();
	}

	public static String getStringSharedPref(Context mContext, String spKey,
			String value) {
		System.out.println("sr key " + spKey + ", sr value : " + value);

		SharedPreferences SP_ECARD = mContext.getSharedPreferences(
				"PEAKABOO_PREFS", Context.MODE_PRIVATE);
		String spValue = SP_ECARD.getString(spKey, value);
		return spValue;

	}

	public static void setBooleanSharedPref(Context p_context, String p_key,
			boolean p_value) {
		SharedPreferences m_sharePref = p_context.getSharedPreferences(
				"PEAKABOO_PREFS", Context.MODE_WORLD_WRITEABLE);
		SharedPreferences.Editor editor = m_sharePref.edit();
		editor.putBoolean(p_key, p_value);
		editor.commit();
	}

	public static boolean getBooleanSharedPref(Context p_context, String p_key,
			boolean p_value) {

		SharedPreferences SP_ECARD = p_context.getSharedPreferences(
				"PEAKABOO_PREFS", Context.MODE_PRIVATE);
		boolean spValue = SP_ECARD.getBoolean(p_key, p_value);
		return spValue;
	}
}
