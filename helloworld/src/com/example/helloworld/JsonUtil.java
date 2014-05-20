package com.example.helloworld;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtil {

	public static String convertJson(String json) {
		try {
		JSONObject jObj = new JSONObject(json);	
		JSONObject politics = jObj.getJSONObject("Politics");
		JSONObject country = politics.getJSONObject("Country");
		JSONObject iain = country.getJSONObject("Iain Botha");
		String question = iain.getString("Question");
		
		JSONArray votes = iain.getJSONArray("Votes");
			JSONObject obj = votes.getJSONObject(0);
			String total = obj.getString("total");
		return total;
		}
		
		catch(JSONException ex){ 
			
			StringWriter errors = new StringWriter();
			ex.printStackTrace(new PrintWriter(errors));
			return errors.toString();
		}

	}
}
