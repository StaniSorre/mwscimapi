package com.enel.ipscan.bean;

import com.google.gson.JsonObject;

public class ControllerResponse {

	public int status;
	public JsonObject jsonResponse;
	
	public ControllerResponse(int status, JsonObject jsonResponse){
		this.status = status;
		this.jsonResponse = jsonResponse;
	}

}
