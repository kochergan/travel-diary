package com.traveldiary.bs;

import java.util.Hashtable;

import android.util.Log;

import com.traveldiary.bs.http.HttpData;
import com.traveldiary.bs.http.HttpRequest;
import com.traveldiary.bs.plaintext.PTParser;
import com.traveldiary.pd.Location;

public class HttpRequestPerformer {

	private static final String BASE_URL = "http://mobop.dexstudio.com.ua/index.php";
	private static final String PASSWORD = "XvvFZYui666OO9d76DD";

	public static Response postInfo(Location location) {
		Log.i("POST", "Postintg");
		Response response = null;
		try {
			Hashtable<String, String> params = setPostRequest(location);

			Log.i("POST", "Hash table build");
			HttpData httpData = HttpRequest.post(BASE_URL, params);
			Log.i("POST", "Response retrieved: " + httpData.content);
			response = PTParser.parsePost(httpData.content);
			Log.i("POST", "Response parsed");

		} catch (Exception e) {
			Log.e("Posting info", "Error: " + e.getMessage());
			for (int i = 0; i < e.getStackTrace().length; i++) {
				Log.e("Posting info",
						"Error Trace: " + e.getStackTrace()[i].toString());
			}
		}

		return response;
	}

	public static Response getInfo(Location location) {
		Response response = null;
		try {
			Hashtable<String, String> params = setGetRequest(location.getId());

			HttpData httpData = HttpRequest.post(BASE_URL, params);

			response = PTParser.parsePost(httpData.content);
		} catch (Exception e) {
			Log.e("Posting info", e.getMessage());
		}

		return response;
	}

	public static Response deleteInfo(Location location) {
		Response response = null;
		try {
			Hashtable<String, String> params = setDeleteRequest(location
					.getId());

			HttpData httpData = HttpRequest.post(BASE_URL, params);

			response = PTParser.parsePost(httpData.content);
		} catch (Exception e) {
			Log.e("Posting info", e.getMessage());
		}

		return response;
	}

	private static Hashtable<String, String> setPostRequest(Location location) {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("type", String.valueOf(RequestType.POST.ordinal()));
		params.put("password", PASSWORD);
		params.put("title", location.getTitle());
		params.put("text", location.getText());
		params.put("lat", String.valueOf(location.getLatitude()));
		params.put("long", String.valueOf(location.getLongitude()));
		params.put("date", String.valueOf(location.getTmstmp()));

		return params;
	}

	private static Hashtable<String, String> setGetRequest(long id) {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("type", String.valueOf(RequestType.GET.ordinal()));
		params.put("password", PASSWORD);
		params.put("id", String.valueOf(id));

		return params;
	}

	private static Hashtable<String, String> setDeleteRequest(long id) {
		Hashtable<String, String> params = new Hashtable<String, String>();
		params.put("type", String.valueOf(RequestType.DELETE.ordinal()));
		params.put("password", PASSWORD);
		params.put("id", String.valueOf(id));

		return params;
	}
}
