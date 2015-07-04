package com.hassan.masla7ty.places;

import android.annotation.TargetApi;
import android.database.MatrixCursor;
import android.os.Build;

import org.json.JSONException;
import org.json.JSONObject;

public class PlaceDetailsJSONParser {
	
	/** Receives a JSONObject and returns a list */
	@TargetApi(Build.VERSION_CODES.KITKAT)
	public MatrixCursor parse(JSONObject jObject){
		
		Double lat = Double.valueOf(0);
		Double lng = Double.valueOf(0);
		
		MatrixCursor hm = null;
		
		try {
			lat = (Double)jObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lat");
			lng = (Double)jObject.getJSONObject("result").getJSONObject("geometry").getJSONObject("location").get("lng");
			hm = new MatrixCursor(new String[]{"lat", "lng"});
			
		} catch (JSONException e) {
			e.printStackTrace();			
		}catch(Exception e){			
			e.printStackTrace();
		}

		hm.newRow()
				.add("lat", Double.toString(lat))
				.add("lng", Double.toString(lng));

		return hm;
	}	
}