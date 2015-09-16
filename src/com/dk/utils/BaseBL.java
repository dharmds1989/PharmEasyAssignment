package com.dk.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class BaseBL extends AsyncTask<String, String, String>
{
	private DataListener listener;
	private Object data;

	public BaseBL(DataListener listener)
	{
		this.listener = listener;
	}

	public void setCallback(DataListener listener)
	{
		this.listener = listener;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			URI website = new URI(params[0]);
			HttpGet request = new HttpGet();
			request.setURI(website);
			HttpResponse response = client.execute(request);

			BufferedReader r = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuilder total = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				total.append(line);
			}
			
			ItemDataParser parser = new ItemDataParser(total.toString());
			parser.parse();
			data = parser.getData();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		if(listener != null) {
			listener.dataRetrieved(data);
		}
	}
}
