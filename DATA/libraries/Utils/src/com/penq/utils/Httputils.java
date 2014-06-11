package com.penq.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.util.Log;


public class Httputils {
	//PeakAboo p=new PeakAboo();
	
	
	public String Getrequest(String url) throws ClientProtocolException, IOException {
		Log.e("liscence:", "This code is a property of PenQ Labs Inc /n by using this you agree to follow the terms and conditions set by PenQ Labs from time to time");
		
		HttpClient client=new DefaultHttpClient();
		HttpGet httpget=new HttpGet("http://192.168.1.196/webservice2/"+url);
		
		
		HttpResponse response=client.execute(httpget);
		//int status=Integer.parseInt(String.valueOf(response.getStatusLine()));
		//if (status==200) {
		if (response.getStatusLine().getStatusCode()==HttpStatus.SC_OK) {
			
		
		
		InputStream in=	response.getEntity().getContent();
		StringBuilder sb=new StringBuilder();
		String line="";
		BufferedReader bf=new BufferedReader(new InputStreamReader(in));
		while ((line=bf.readLine())!=null) {
			sb.append(line);
		}
		return sb.toString();
		} else {
			return "{\"Status\":false,\"result\":\"Server Error\"}";
		}
		
		
	}
	

	
	

}
