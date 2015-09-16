package com.dk.utils;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.dk.dao.ItemDataDAO;

import android.util.Log;

public class ItemDataParser {
	private String response = "";
	private Vector<ItemDataDAO> vecData;
	
	public ItemDataParser(String response)
	{
		this.response = response;
	}
	
	public void parse()
	{
		if(response != null && !response.equalsIgnoreCase("")) {
			Log.d("Response", response);
			
			vecData = new Vector<ItemDataDAO>();
			
			try {
				JSONObject objJSON = new JSONObject(response);
				if(objJSON != null && objJSON.has("result")) {
					JSONArray arrJSON = objJSON.getJSONArray("result");
					if(arrJSON != null && arrJSON.length() > 0) {
						for (int i = 0; i < arrJSON.length(); i++)
						{
							try {
								JSONObject objJSON1 = arrJSON.getJSONObject(i);
								if(objJSON1 != null) {
									ItemDataDAO dataDAO = new ItemDataDAO();
									if(objJSON1.has("id")) {
										dataDAO.setId(Integer.parseInt(objJSON1.getString("id")));
									}
									if(objJSON1.has("hkpDrugCode")) {
										dataDAO.setCode(Integer.parseInt(objJSON1.getString("hkpDrugCode")));
									}
									if(objJSON1.has("name")) {
										dataDAO.setName(objJSON1.getString("name"));
									}
									if(objJSON1.has("label")) {
										dataDAO.setLabel(objJSON1.getString("label"));
									}
									if(objJSON1.has("packSize")) {
										dataDAO.setPack(objJSON1.getString("packSize"));
									}
									if(objJSON1.has("manufacturer")) {
										dataDAO.setMfby(objJSON1.getString("manufacturer"));
									}
									if(objJSON1.has("mrp")) {
										dataDAO.setMrp(objJSON1.getString("mrp"));
									}
									if(objJSON1.has("packSize")) {
										dataDAO.setPackSize(objJSON1.getString("packSize"));
									}
									
									vecData.add(dataDAO);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public Object getData()
	{
		return vecData;
	}
}
