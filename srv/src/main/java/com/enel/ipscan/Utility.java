package com.enel.ipscan;

import com.google.gson.JsonObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class Utility {

	public static JsonObject setCustAttribute(String name, String description, String required) {
		JsonObject item = new JsonObject();
		item.addProperty("name", name);
		item.addProperty("type", "string");
		item.addProperty("multiValued", "true");
		item.addProperty("description", description);
		item.addProperty("required", required);
		item.addProperty("caseExact", "false");
		item.addProperty("mutability", "readWrite");
		item.addProperty("returned", "default");
		item.addProperty("uniqueness", "none");
		return item;
	}

	/*
	 * Crea la struttura di risposta contenente i campi custom il valore dei campi
	 * custom è recuperato dalla funzione "_queryCustom" Funzione richiamata dalla
	 * GET ENTITY
	 */
	public static JsonArray getArrayCustomAttributes(JsonArray jsonResponseArray) {

		JsonArray aAttributiCustomOut = new JsonArray();

		for (int i = 0; i < jsonResponseArray.size(); i++) {

			JsonElement oResponseLine = jsonResponseArray.get(i);
			JsonObject oResponseLineJO = oResponseLine.getAsJsonObject();

			JsonObject oNuovaRigaAttributiCustom = new JsonObject();
			oNuovaRigaAttributiCustom.addProperty("attributeId", oResponseLineJO.get("attributeId").getAsString());
			oNuovaRigaAttributiCustom.addProperty("attributeValue",
					oResponseLineJO.get("attributeValue").getAsString());
			oNuovaRigaAttributiCustom.addProperty("dateFrom", oResponseLineJO.get("dateFrom").getAsString());
			oNuovaRigaAttributiCustom.addProperty("dateTo", oResponseLineJO.get("dateTo").getAsString());

			aAttributiCustomOut.add(oNuovaRigaAttributiCustom);

		}

		return aAttributiCustomOut;
	}

}
