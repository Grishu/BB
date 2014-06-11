package com.penq.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.ParseException;
import android.util.Log;


/*
This code is a property of PenQ Labs Inc 
by using this you agree to follow the terms and conditions set by PenQ Labs from time to time
*/
public class BitmapUtils {
	
	public String multipartRequest(String urlTo, String post, ByteArrayInputStream filepath, String filefield, Activity a) throws ParseException, IOException {
	  	
		HttpURLConnection connection = null;
			DataOutputStream outputStream = null;
			InputStream inputStream = null;
			
			String twoHyphens = "--";
			String boundary =  "*****"+Long.toString(System.currentTimeMillis())+"*****";
			String lineEnd = "\r\n";
			
			String result = "";
			
			int bytesRead, bytesAvailable, bufferSize;
			byte[] buffer;
			int maxBufferSize = 1*1024*1024;
			
		/*	String[] q = filepath.split("/");
			int idx = q.length - 1;*/
			
			try {
				
				ByteArrayInputStream fileInputStream =filepath;
				/*
				if (!filepath.equals("")) {
					File file = new File(filepath);
					 fileInputStream = new FileInputStream(file);//openFileInput(file); //a.getAssets().open("ic_launcher.png");
					
				} else {
					 fileInputStream =a.getAssets().open("ic_launcher.png");
					
				}*/
				
				URL url = new URL(urlTo);
				connection = (HttpURLConnection) url.openConnection();
				
				connection.setDoInput(true);
				connection.setDoOutput(true);
				
				connection.setUseCaches(false);
				
				
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestProperty("User-Agent", "PenQ Labs Android Multipart HTTP Client 1.0");
				connection.setRequestProperty("Content-Type", "multipart/form-data; boundary="+boundary);
				
				outputStream = new DataOutputStream(connection.getOutputStream());
				outputStream.writeBytes(twoHyphens + boundary + lineEnd);
				outputStream.writeBytes("Content-Disposition: form-data; name=\"" + filefield + "\"; filename=\"" + "nevig.png"/*q[idx]*/ +"\"" + lineEnd);
				outputStream.writeBytes("Content-Type: image/jpeg" + lineEnd);
				outputStream.writeBytes("Content-Transfer-Encoding: binary" + lineEnd);
				outputStream.writeBytes(lineEnd);
				
				bytesAvailable = fileInputStream.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];
				
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				while(bytesRead > 0) {
					outputStream.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);
				}
				
				outputStream.writeBytes(lineEnd);
				
				// Upload POST Data
				String[] posts = post.split("&amp;");
				int max = posts.length;
				for(int i=0; i<max;i++) {
					outputStream.writeBytes(twoHyphens + boundary + lineEnd);
					String[] kv = posts[i].split("=");
					outputStream.writeBytes("Content-Disposition: form-data; name=\"" + kv[0] + "\"" + lineEnd);
					outputStream.writeBytes("Content-Type: text/plain"+lineEnd);
					outputStream.writeBytes(lineEnd);
					outputStream.writeBytes(kv[1]);
					outputStream.writeBytes(lineEnd);
				}
				
				outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
				
				inputStream = connection.getInputStream();
				result = this.convertStreamToString(inputStream);
				
				fileInputStream.close();
				inputStream.close();
				outputStream.flush();
				outputStream.close();
				
				return result;
			} catch(Exception e) {
				Log.e("MultipartRequest","Multipart Form Upload Error");
				e.printStackTrace();
				return "error";
			}
		}
	 
		private String convertStreamToString(InputStream is) {
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			StringBuilder sb = new StringBuilder();
	 
			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return sb.toString();
		}

}
