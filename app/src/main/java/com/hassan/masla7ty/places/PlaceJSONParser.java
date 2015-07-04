package com.hassan.masla7ty.places;

import android.annotation.TargetApi;
import android.database.MatrixCursor;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlaceJSONParser {

	private Object description;
	private String id="";
	private String reference="";

	@TargetApi(Build.VERSION_CODES.KITKAT)
	public MatrixCursor parse(JSONObject jObject) {

		JSONArray jPlaces = null;
		MatrixCursor mc = null;
		try {
			/** Retrieves all the elements in the 'places' array */
			jPlaces = jObject.getJSONArray("users");
			mc = new MatrixCursor(new String[]{"description", "_id", "reference"});
			for (int i = 0; i < jPlaces.length(); i++) {
				JSONObject jPlace = jPlaces.getJSONObject(i);
				// extract the properties from the JSONObject and use it with the addRow() method below
				description = jPlace.getString("description");
				id = jPlace.getString("id");
				reference = jPlace.getString("reference");
				mc.newRow()
						.add("description", description)
						.add("id", id)
						.add("reference", reference);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return mc;
	}
	
	/** Receives a JSONObject and returns a list 
	public List<HashMap<String,String>> parse(JSONObject jObject){
		
		/*JSONArray jPlaces = null;
		try {			
			jPlaces = jObject.getJSONArray("predictions");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		/** Invoking getPlaces with the array of json object
		 * where each json object represent a place
		return getPlaces(jPlaces);
	}*/
	
	
	/*private List<HashMap<String, String>> getPlaces(JSONArray jPlaces){
		int placesCount = jPlaces.length();
		List<HashMap<String, String>> placesList = new ArrayList<HashMap<String,String>>();
		HashMap<String, String> place = null;	

		for(int i=0; i<placesCount;i++){
			try {
				place = getPlace((JSONObject)jPlaces.get(i));
				placesList.add(place);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
		return placesList;
	}

	private HashMap<String, String> getPlace(JSONObject jPlace){

		HashMap<String, String> place = new HashMap<String, String>();
		
		String id="";
		String reference="";
		String description="";		
		
		try {
			
			description = jPlace.getString("description");			
			id = jPlace.getString("id");
			reference = jPlace.getString("reference");
			
			place.put("description", description);
			place.put("_id",id);
			place.put("reference",reference);
			
		} catch (JSONException e) {			
			e.printStackTrace();
		}		
		return place;
	}*/
}
