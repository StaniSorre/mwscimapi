package com.enel.ipscan;

import java.util.List;
import java.util.UUID;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import vdm.namespaces.zscpaidasrv.HR_MNG;
import vdm.namespaces.zscpaidasrv.HR_SIA;
import vdm.namespaces.zscpaidasrv.Hrorg;
import vdm.services.DefaultZSCPAIDASRVService;

import java.io.IOException;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.odatav2.connectivity.ODataException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpGet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpDelete;
import java.io.BufferedReader;

import com.sap.cloud.sdk.s4hana.connectivity.ErpConfigContext;

//
@WebServlet("/Users/*")
@ServletSecurity(@HttpConstraint(rolesAllowed = { "ApiScimExternal_SC" }))
public class Utenti extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected JsonArray membersNewArray;
	protected String direct;
	private JsonObject globalResponse = new JsonObject();
	private String globalError;
	private JsonObject customAttributes;
	private JsonObject customOut;
	private JsonArray customOutArray;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/scim+json");
		try {
			String param = request.getPathInfo();
			if (param != null) {
				if (param.charAt(0) == '/') {
					String id = param.substring(1);

					// Leggo l'API standard
					// HttpClient httpClient = HttpClientAccessor
					// .getHttpClient(DestinationAccessor.getDestination("apiAccess"));
					// //"scimCross" su sorgente SCIM
					HttpClient httpClient = HttpClientAccessor
							.getHttpClient(DestinationAccessor.getDestination("apiAccessCross"));
					HttpGet getScim = new HttpGet("/Users/" + id);
					getScim.addHeader("Accept", "application/scim+json");
					HttpResponse scimResponse = httpClient.execute(getScim);

					if (scimResponse.getStatusLine().getStatusCode() == 200) {

						HttpEntity entityScim = scimResponse.getEntity();
						JsonObject jsonResponse = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityScim));

						// version update
						String version = jsonResponse.getAsJsonObject("meta").get("version").toString();
						JsonObject meta2 = jsonResponse.getAsJsonObject("meta");
						meta2.remove("version");
						meta2.addProperty("version", version);
						jsonResponse.remove("meta");
						jsonResponse.add("meta", meta2);

						JsonArray membersArray = jsonResponse.getAsJsonArray("groups");
						if (membersArray != null) {
							membersNewArray = new JsonArray();
							String direct;
							for (int i = 0; i < membersArray.size(); i++) {
								JsonObject member = new JsonObject();
								member = (JsonObject) membersArray.get(i);
								if (member.get("value").toString().replace("\"", "").startsWith("IPSCAN") == true) {
									direct = member.get("type").toString().replace("\"", "").toLowerCase();
									member.remove("type");
									member.addProperty("type", direct);
									membersNewArray.add(member);
								}
							}

							jsonResponse.remove("groups");
							jsonResponse.add("groups", membersNewArray);
						}

						if (jsonResponse.get("id") != null) {

							// Get dei campi custom

							String idScim = jsonResponse.get("id").toString().replace("\"", "'");
							String filter = "?$filter=id%20eq%20(guid" + idScim + ")&$format=json";
							String stringUrl = "/UsersRolesAndAttributes" + filter;

							HttpClient httpClientCustom = HttpClientAccessor
									.getHttpClient(DestinationAccessor.getDestination("destinazioneSCIM")); // "destinazioneApiManScimProt"
																											// su
																											// sorgente
																											// SCIM
							HttpGet getScimCustom = new HttpGet(stringUrl);
							HttpResponse scimResponseCustom = httpClientCustom.execute(getScimCustom);

							HttpEntity entityScimCustom = scimResponseCustom.getEntity();
							JsonObject jsonResponseCustom = (JsonObject) new JsonParser()
									.parse(EntityUtils.toString(entityScimCustom));
							JsonArray jsonResponseCustomArray = jsonResponseCustom.getAsJsonObject("d")
									.getAsJsonArray("results");

							// Mapping dei campi custom
							JsonArray aCustomAttributes = new JsonArray();
							aCustomAttributes = Utility.getArrayCustomAttributes(jsonResponseCustomArray);

							// Adeguamento SCIM 1.1 -> 2.0
							jsonResponse.remove("schemas");
							jsonResponse.remove("approvals");
							jsonResponse.remove("verified");
							jsonResponse.remove("origin");
							jsonResponse.remove("previousLogonTime");
							jsonResponse.remove("lastLogonTime");
							jsonResponse.remove("zoneId");
							jsonResponse.remove("passwordLastModified");

							// Output chiamata
							JsonArray schemasArray2 = new JsonArray();
							schemasArray2.add("urn:ietf:params:scim:schemas:core:2.0:User");
							schemasArray2.add("urn:ietf:params:scim:schemas:extension:UAA:2.0:User");
							jsonResponse.add("schemas", schemasArray2);
							jsonResponse.add("urn:ietf:params:scim:schemas:extension:UAA:2.0:User", aCustomAttributes);

							response.getWriter().write(new Gson().toJson(jsonResponse));
						}
					} else {
						response.setStatus(scimResponse.getStatusLine().getStatusCode());
						JsonObject jsonErrResp = (JsonObject) new JsonParser()
								.parse(EntityUtils.toString(scimResponse.getEntity()));
						// response.getWriter().write(new Gson().toJson(jsonErrResp));
					}
				}
			} else {
				param = request.getQueryString();
				String sRequest = request.getRequestURL().toString();
				JsonObject jsonQueryResponse = this.queryScim(param, response, sRequest);

				if (jsonQueryResponse != null) {
					response.getWriter().write(new Gson().toJson(jsonQueryResponse));
				}
			}
		} catch (Exception e) {
			response.setStatus(500);
			response.getWriter().append("Error: " + e.getMessage().toString());
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			// JsonObject newObject = new JsonObject();
			StringBuffer jb = new StringBuffer();
			String line = null;
			response.setContentType("application/scim+json");
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
			JsonObject jsonResponse = new JsonObject();
			// Integer cr1 = this.post("scimCross", "afqxozn24.accounts.ondemand.com", "",
			// jb); su sorgente SCIM
			Integer cr1 = this.post("apiAccessCross", "anev3ox8b.accounts.ondemand.com", "", jb);
			if (cr1 == 201 || cr1 == 409) {
				// Integer cr2 = this.post("scim", "afqxozn24.accounts.ondemand.com", "", jb);
				// su sorgente SCIM
				Integer cr2 = this.post("apiAccess", "cfapps.eu10.hana.ondemand.com4a795a7", "", jb);
				if (cr2 == 201 || cr2 == 409) {
					// Integer stat = this.post("scim", "cfapps.eu10.hana.ondemand.com46ae131", "X",
					// jb); su sorgente SCIM
					Integer stat = this.post("apiAccess", "anev3ox8b.accounts.ondemand.com", "X", jb);
					response.setStatus(stat);
					response.getWriter().write(new Gson().toJson(globalResponse));
				} else
					response.setStatus(500);
			} else
				response.setStatus(500);

		} catch (Exception e) {
			response.setStatus(500);
			response.getWriter().append("Error: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/scim+json");
		try {
			// JsonObject newObject = new JsonObject();
			StringBuffer jb = new StringBuffer();
			String line = null;
			response.setContentType("application/scim+json");
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}

			// newObject = new JsonParser().parse(jb.toString()).getAsJsonObject();
			// JsonObject newObject2 = newObject.deepCopy();
			String param = request.getPathInfo().toString();
			if (param.charAt(0) == '/') {
				JsonObject user = new JsonParser().parse(jb.toString()).getAsJsonObject();
				String id = user.get("id").toString().replace("\"", "'");
				param = param.substring(1);
				JsonObject jsonResponse = new JsonObject();
				// if (this.put("scim", "afqxozn24.accounts.ondemand.com", "", jb, userName) ==
				// 200) { su sorgente SCIM
				if (this.put("apiAccess", "anev3ox8b.accounts.ondemand.com", "", jb, id) == 200) {
					// if (this.put("scim", "cfapps.eu10.hana.ondemand.com46ae131", "", jb,
					// userName) == 200) { su sorgente SCIM
					if (this.put("apiAccess", "cfapps.eu10.hana.ondemand.com4a795a7", "", jb, id) == 200) {
						// Integer stat = this.put("scimCross", "afqxozn24.accounts.ondemand.com", "X",
						// jb, userName); su sorgente SCIM
						Integer stat = this.put("apiAccessCross", "anev3ox8b.accounts.ondemand.com", "X", jb, id);
						response.setStatus(stat);
						if (stat == 200)
							response.getWriter().write(new Gson().toJson(globalResponse));
						else {

							JsonObject error = new JsonObject();
							error.addProperty("error3", globalError);
							response.getWriter().write(new Gson().toJson(error));
							response.setStatus(500);

						}
					} else {

						JsonObject error = new JsonObject();
						error.addProperty("error2", globalError);
						response.getWriter().write(new Gson().toJson(error));

						response.setStatus(500);
					}
				} else {

					JsonObject error = new JsonObject();
					error.addProperty("error1", globalError);
					response.getWriter().write(new Gson().toJson(error));
					response.setStatus(500);
				}

			}
		} catch (Exception e) {
			response.setStatus(500);
			JsonObject error = new JsonObject();
			error.addProperty("error_catch", "X");
			response.getWriter().write(new Gson().toJson(error));
			e.printStackTrace();
		}
	}

	/*
	 * Crea la struttura di risposta contenente i campi custom il valore dei campi
	 * custom è recuperato dalla funzione "_queryCustom" Funzione richiamata dalla
	 * GET ENTITY
	 */
	private JsonArray _getArrayCustomAttributes(JsonArray jsonResponseArray) {

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

	private JsonObject queryScim(String param, HttpServletResponse response, String sRequest) throws IOException {
		// Leggo l'API standard
		// HttpClient httpClient =
		// HttpClientAccessor.getHttpClient(DestinationAccessor.getDestination("scimCross"));
		// su sorgente SCIM
		HttpClient httpClient = HttpClientAccessor.getHttpClient(DestinationAccessor.getDestination("apiAccessCross"));
		HttpGet getScim;
		if (param != null) {
			getScim = new HttpGet("/Users?" + param);
		} else {
			getScim = new HttpGet("/Users");
		}
		getScim.addHeader("Accept", "application/scim+json");
		HttpResponse scimResponse;

		try {

			scimResponse = httpClient.execute(getScim);
			HttpEntity entityScim = scimResponse.getEntity();
			JsonObject jsonResponseScim = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityScim));
			JsonArray resources = jsonResponseScim.getAsJsonArray("resources");
			JsonArray newResources = new JsonArray();

			if (resources != null) {
				for (int i = 0; i < resources.size(); i++) {
					// Mapping dei campi custom
					JsonObject scimRes = (JsonObject) resources.get(i);
					JsonArray aCustomAttributes = new JsonArray();
					// aCustomAttributes =
					// this._queryCustom(scimRes.get("id").toString().replace("\"", "'"));
					aCustomAttributes = this._queryCustom(scimRes.get("id").toString().replace("\"", "'"));

					// Adeguamento SCIM 1.1 -> 2.0
					scimRes.remove("previousLogonTime");
					scimRes.remove("lastLogonTime");
					scimRes.remove("approvals");
					scimRes.remove("verified");
					scimRes.remove("origin");
					scimRes.remove("zoneId");
					scimRes.remove("passwordLastModified");

					// version update
					String version = scimRes.getAsJsonObject("meta").get("version").toString();
					JsonObject meta2 = scimRes.getAsJsonObject("meta");
					meta2.remove("version");
					meta2.addProperty("version", version);
					meta2.addProperty("resourceType", "User");
					meta2.addProperty("location", sRequest + "/" + scimRes.get("id").toString().replace("\"", ""));
					scimRes.remove("meta");
					scimRes.add("meta", meta2);

					// Recupero i gruppi a cui appartiene il singolo utente
					JsonArray membersArray = scimRes.getAsJsonArray("groups");
					if (membersArray != null) {
						membersNewArray = new JsonArray();
						for (int j = 0; j < membersArray.size(); j++) {
							JsonObject member = new JsonObject();
							member = (JsonObject) membersArray.get(j);
							if (member.get("value").toString().replace("\"", "").startsWith("IPSCAN") == true) {
								direct = member.get("type").toString().replace("\"", "").toLowerCase();
								member.remove("type");
								member.addProperty("type", direct);
								membersNewArray.add(member);
							}
						}
						scimRes.remove("groups");
						scimRes.add("groups", membersNewArray);
					}

					// Output chiamata per singolo utente
					scimRes.remove("schemas");
					JsonArray schemasArray2 = new JsonArray();
					schemasArray2.add("urn:ietf:params:scim:schemas:core:2.0:User");
					schemasArray2.add("urn:ietf:params:scim:schemas:extension:UAA:2.0:User");
					scimRes.add("schemas", schemasArray2);
					scimRes.add("urn:ietf:params:scim:schemas:extension:UAA:2.0:User", aCustomAttributes);

					if ((param != null && param.contains("=userName")) || scimRes.getAsJsonArray("groups").size() > 0
							|| membersArray == null || membersArray.size() == 0)
						newResources.add(scimRes);
				}
				jsonResponseScim.remove("resources");
				jsonResponseScim.add("Resources", newResources);

			}
			// Output finale della query
			jsonResponseScim.remove("schemas");
			JsonArray schemasArray2 = new JsonArray();
			schemasArray2.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
			jsonResponseScim.add("schemas", schemasArray2);
			// jsonResponseScim.remove("totalResults"); // commentato su sorgente SCIM
			// jsonResponseScim.addProperty("totalResults",
			// jsonResponseScim.getAsJsonArray("Resources").size()); // commentato su
			// sorgente SCIM
			return jsonResponseScim;
		} catch (IOException e) {
			response.setStatus(500);
			response.getWriter().append("Error: " + e.getMessage().toString());
			e.printStackTrace();
			return null;
		}
	}

	/*
	 * Recupera i campi custom dalle tabelle di processo. legge la tabella custom a
	 * DB
	 */
	private JsonArray _queryCustom(String idScim) {

		JsonArray aAttributiCustomOut = new JsonArray();

		try {

			String filter = "?$filter=id%20eq%20(guid" + idScim + ")&$format=json";
			String stringUrl = "/UsersRolesAndAttributes" + filter;
			HttpClient httpClientCustom = HttpClientAccessor
					.getHttpClient(DestinationAccessor.getDestination("destinazioneSCIM"));
			HttpGet getScimCustom = new HttpGet(stringUrl);
			HttpResponse scimResponseCustom = httpClientCustom.execute(getScimCustom);

			HttpEntity entityScimCustom = scimResponseCustom.getEntity();
			JsonObject jsonResponseCustom = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityScimCustom));
			JsonArray jsonResponseCustomArray = jsonResponseCustom.getAsJsonObject("d").getAsJsonArray("results");

			// Mappa i campi custom nella risposta
			aAttributiCustomOut = this._getArrayCustomAttributes(jsonResponseCustomArray);

		}

		catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return aAttributiCustomOut;

	}

	private Integer put(String destination, String trust, String master, StringBuffer jb, String id)
			throws ServletException, IOException {
		try {
			String param;
			JsonObject jsonResponse = new JsonObject();
			JsonObject newObject = new JsonParser().parse(jb.toString()).getAsJsonObject();
			JsonObject newObject2 = newObject.deepCopy();
			Integer status = 0;
			HttpClient httpClient = HttpClientAccessor.getHttpClient(DestinationAccessor.getDestination(destination));
			JsonArray membersNewArray = new JsonArray();
			/////////////////////////// inserire i gruppi diversi da AIDA
			String filterUser = "?$filter=id%20eq%20" + id;
			HttpGet getGroups = new HttpGet("/Users" + filterUser);
			getGroups.addHeader("Accept", "application/scim+json");
			HttpResponse getGroupsResponse = httpClient.execute(getGroups);
			HttpEntity entityGroups = getGroupsResponse.getEntity();
			JsonObject jsonResponseGroups = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityGroups));
			JsonArray resArray = jsonResponseGroups.getAsJsonArray("resources");
			JsonObject jsonResponseGroups0 = new JsonObject();
			for (int i = 0; i < resArray.size(); i++) {
				JsonObject resArrayObject = (JsonObject) resArray.get(i);
				if (resArrayObject.get("origin").toString().replace("\"", "").equals(trust)) {
					jsonResponseGroups0 = resArrayObject;
					break;
				}
			}

			JsonArray membersArrayGroups = jsonResponseGroups0.getAsJsonArray("groups");
			// proviamo a non far aggiungere gruppi vecchi
			if (membersArrayGroups != null) {
				for (int i = 0; i < membersArrayGroups.size(); i++) {
					JsonObject memberGroup = new JsonObject();
					memberGroup = (JsonObject) membersArrayGroups.get(i);
					if (memberGroup.get("value").toString().replace("\"", "").startsWith("IPSCAN") == false)
						membersNewArray.add(memberGroup);
				}
			}

			param = jsonResponseGroups0.get("id").toString().replace("\"", "");
			HttpPut requestScim = new HttpPut("/Users/" + param);
			requestScim.addHeader("content-type", "application/scim+json");

			JsonArray membersArray = newObject2.getAsJsonArray("groups");
			if (membersArray != null) {
				for (int i = 0; i < membersArray.size(); i++) {
					JsonObject member = new JsonObject();
					member = (JsonObject) membersArray.get(i);
					direct = member.get("type").toString().replace("\"", "").toUpperCase();
					member.remove("type");
					member.addProperty("type", direct);
					membersNewArray.add(member);
				}
				newObject.remove("groups");
				newObject.add("groups", membersNewArray);
			}

			// direct in put
			requestScim.addHeader("If-Match", "*");
			newObject.remove("urn:ietf:params:scim:schemas:extension:UAA:2.0:User");
			newObject.remove("schemas");
			JsonArray schemasArray = new JsonArray();
			schemasArray.add("urn:scim:schemas:core:1.0");
			newObject.add("schemas", schemasArray);
			newObject.addProperty("origin", trust);
			newObject.remove("id");
			StringEntity paramsScim = new StringEntity(newObject.toString());
			requestScim.setEntity(paramsScim);

			HttpResponse responseScim = httpClient.execute(requestScim);
			if (responseScim.getStatusLine().getStatusCode() == 200 && master == "X") {

				HttpEntity entityScim = responseScim.getEntity();
				if (entityScim != null) {
					String responseScimJson = EntityUtils.toString(entityScim);
					status = 200;
					jsonResponse = (JsonObject) new JsonParser().parse(responseScimJson);
					JsonArray custom = newObject2.getAsJsonArray("urn:ietf:params:scim:schemas:extension:UAA:2.0:User");
					// JsonObject customOut = new JsonObject();
					// JsonArray extraArrayOut = new JsonArray();

					if (custom != null) {

						// Cancello i record esistenti sulle tabelle custom USERS e USERSATTRIBUTES
						this._deleteCustomUser(newObject.get("id").getAsString());
						this._deleteCustomControllore(jsonResponse.get("userName").toString().replace("\"", ""));
						this._deleteCustomUsersAttributes(newObject.get("id").getAsString());

						this._insertCustomUser(jsonResponse.get("id").toString().replace("\"", ""),
								jsonResponse.get("userName").toString().replace("\"", ""),
								jsonResponse.getAsJsonObject("name").get("familyName").getAsString(),
								jsonResponse.getAsJsonObject("name").get("givenName").getAsString(), jsonResponse
										.getAsJsonArray("emails").get(0).getAsJsonObject().get("value").getAsString());

						// Inserisco i nuovi record passati in input --> TABELLA Controllori
						customOutArray = this._insertControllori(jsonResponse, custom);

						// Inserisco i nuovi record passati in input --> TABELLA USERS ATTRIBUTES
						customOutArray = this._insertCustomUsersAttributes(newObject.get("id").getAsString(), custom);

						// cancello da aida con filtro
						// this.deleteAida(newObject.get("id").toString());
						// JsonArray extraArray = custom.getAsJsonArray("extra");
						// insertAida(newObject.get("userName").toString(), custom);

					}

					jsonResponse.add("urn:ietf:params:scim:schemas:extension:UAA:2.0:User", this.customOutArray);
					jsonResponse.remove("schemas");
					jsonResponse.remove("approvals");
					jsonResponse.remove("verified");
					jsonResponse.remove("origin");
					jsonResponse.remove("zoneId");
					jsonResponse.remove("previousLogonTime");
					jsonResponse.remove("lastLogonTime");
					jsonResponse.remove("passwordLastModified");
					JsonArray schemasArray2 = new JsonArray();
					schemasArray2.add("urn:ietf:params:scim:schemas:core:2.0:User");
					schemasArray2.add("urn:ietf:params:scim:schemas:extension:UAA:2.0:User");
					jsonResponse.add("schemas", schemasArray2);
					membersNewArray = new JsonArray();
					// version update
					String version = jsonResponse.getAsJsonObject("meta").get("version").toString();
					JsonObject meta2 = jsonResponse.getAsJsonObject("meta");
					meta2.remove("version");
					meta2.addProperty("version", version);
					jsonResponse.remove("meta");
					jsonResponse.add("meta", meta2);
					// version update
					membersArray = jsonResponse.getAsJsonArray("groups");

					if (membersArray != null) {

						for (int i = 0; i < membersArray.size(); i++) {
							JsonObject member = new JsonObject();
							member = (JsonObject) membersArray.get(i);
							if (member.get("value").toString().replace("\"", "").startsWith("IPSCAN") == true) {
								direct = member.get("type").toString().replace("\"", "").toLowerCase();
								member.remove("type");
								member.addProperty("type", direct);
								membersNewArray.add(member);
							}
						}
						jsonResponse.remove("groups");
						jsonResponse.add("groups", membersNewArray);
					}
					// response.getWriter().write(new Gson().toJson(jsonResponse));
					globalResponse = jsonResponse;
				}
			} else {
				status = responseScim.getStatusLine().getStatusCode();
				this.globalError = status.toString();
				// JsonObject jsonErrResp = (JsonObject) new JsonParser()
				// .parse(EntityUtils.toString(responseScim.getEntity()));
				// response.getWriter().write(new Gson().toJson(jsonErrResp));
			}
			return status;
		} catch (Exception e) {
			this.globalError = e.getMessage().toString();
			e.printStackTrace();
			return 500;

		}
	}

	/*
	 * vecchio metodo per inserimento dati in struttura aida private void
	 * insertSmartControl(String userName, JsonObject custom) {
	 * 
	 * // Username To Uppercase
	 * 
	 * String userNameUp = userName; userNameUp.toUpperCase();
	 * 
	 * this.customOut = new JsonObject(); JsonArray areaArray =
	 * custom.getAsJsonArray("area"); JsonArray zonaArray =
	 * custom.getAsJsonArray("zona"); JsonArray unitaArray =
	 * custom.getAsJsonArray("unita_operativa"); JsonArray areaArrayOut = new
	 * JsonArray(); JsonArray zonaArrayOut = new JsonArray(); JsonArray
	 * unitaArrayOut = new JsonArray(); if (areaArray != null) { for (int i = 0; i <
	 * areaArray.size(); i++) { if (createRecordAida(userNameUp,
	 * areaArray.get(i).toString(), "AreaCo")) {
	 * areaArrayOut.add(areaArray.get(i).toString().replace("\"", "'")); } } } if
	 * (zonaArray != null) { for (int i = 0; i < zonaArray.size(); i++) { if
	 * (createRecordAida(userNameUp, zonaArray.get(i).toString(), "ZonaCo"))
	 * 
	 * zonaArrayOut.add(zonaArray.get(i).toString().replace("\"", "'")); } } if
	 * (unitaArray != null) { for (int i = 0; i < unitaArray.size(); i++) { if
	 * (createRecordAida(userNameUp, unitaArray.get(i).toString(), "UnopCo"))
	 * 
	 * unitaArrayOut.add(unitaArray.get(i).toString().replace("\"", "'")); } }
	 * this.customOut.add("area", areaArrayOut); this.customOut.add("zona",
	 * zonaArrayOut); this.customOut.add("unita_operativa", unitaArrayOut); }
	 * 
	 * private boolean createRecordAida(String user, String value, String attribute)
	 * { JsonObject customAttrAida = new JsonObject();
	 * customAttrAida.addProperty("USER", user.replace("\"", ""));
	 * customAttrAida.addProperty("ATTRIBUTE_VALUE", value.replace("\"", ""));
	 * customAttrAida.addProperty("ATTRIBUTE_NAME", attribute.replace("\"", ""));
	 * StringEntity paramsAida = new StringEntity(customAttrAida.toString(),
	 * "UTF-8"); HttpPost requestAida = new HttpPost("/ROLES_ATTRIBUTES");
	 * requestAida.addHeader("content-type", "application/json");
	 * requestAida.setEntity(paramsAida); HttpClient httpClientAida =
	 * HttpClientAccessor .getHttpClient(DestinationAccessor.getDestination(
	 * "destinazioneApiManScimProt")); HttpResponse responseAida; try { responseAida
	 * = httpClientAida.execute(requestAida); if
	 * (responseAida.getStatusLine().getStatusCode() == 201) return true; else
	 * return false; } catch (ClientProtocolException e) { // TODO Auto-generated
	 * catch block return false; } catch (IOException e) { // TODO Auto-generated
	 * catch block return false; }
	 * 
	 * }
	 */

	private Integer delete(String destination, String master, String param, String trust, String id) {
		Integer status = 0;
		try {
			HttpClient httpClient = HttpClientAccessor.getHttpClient(DestinationAccessor.getDestination(destination));
			/////////// recupero id corretto

			String filterUser = "?filter=id%20eq%20" + id;
			HttpGet getListUsers = new HttpGet("/Users" + filterUser);
			getListUsers.addHeader("Accept", "application/scim+json");
			HttpResponse getListUsersResponse = httpClient.execute(getListUsers);
			HttpEntity entityListUsers = getListUsersResponse.getEntity();
			JsonObject jsonResponseUsers = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityListUsers));
			JsonArray jsonResponseUsersArray = jsonResponseUsers.getAsJsonArray("resources");
			String ok = "";
			for (int i = 0; i < jsonResponseUsersArray.size(); i++) {
				JsonObject userEl = (JsonObject) jsonResponseUsersArray.get(i);
				if (userEl.get("origin").toString().replace("\"", "").equals(trust)) {
					String newParam = userEl.get("id").toString().replace("\"", "");
					/////////// recupero id corretto
					HttpDelete requestScim = new HttpDelete("/Users/" + newParam);
					requestScim.addHeader("If-Match", "*");
					HttpResponse responseScim = httpClient.execute(requestScim);
					if (responseScim.getStatusLine().getStatusCode() == 200
							|| responseScim.getStatusLine().getStatusCode() == 404) {
						ok = "X";
						if (master == "X") {
							HttpEntity entityScim = responseScim.getEntity();
							JsonObject jsonResponseScim = (JsonObject) new JsonParser()
									.parse(EntityUtils.toString(entityScim));
							if (jsonResponseScim.get("id") != null) {
								this._deleteCustomUser(param);
								this._deleteCustomControllore(
										jsonResponseScim.get("userName").toString().replace("\"", ""));
								this._deleteCustomUsersAttributes(param);
							}
						}
						break;
					}
				}
			}
			if (ok == "X") {
				status = 200;
				if (master == "X")
					status = 204;
				return status;
			} else
				return 500;
		} catch (Exception e) {
			e.printStackTrace();
			return 500;
		}
	}

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/scim+json");
		try {
			String param = request.getPathInfo().toString();
			if (param.charAt(0) == '/') {

				param = param.substring(1);
				HttpClient httpClientCross = HttpClientAccessor
						.getHttpClient(DestinationAccessor.getDestination("apiAccessCross")); // "scimCross" su sorgente
																								// SCIM
				HttpGet getUser = new HttpGet("/Users/" + param);
				getUser.addHeader("Accept", "application/scim+json");
				HttpResponse userResponse = httpClientCross.execute(getUser);
				HttpEntity entityUser = userResponse.getEntity();
				JsonObject jsonResponseUser = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityUser));
				String id = jsonResponseUser.get("id").toString().replace("\"", "'");
				// Integer c1 = this.delete("scim", "", param,
				// "anev3ox8b.accounts.ondemand.com", id); su sorgente SCIM
				Integer c1 = this.delete("apiAccess", "", param, "anev3ox8b.accounts.ondemand.com", id);
				if (c1 == 200) {
					// Integer c2 = this.delete("scim", "", param,
					// "cfapps.eu10.hana.ondemand.com4a795a7", id); su sorgente SCIM
					Integer c2 = this.delete("apiAccess", "", param, "cfapps.eu10.hana.ondemand.com4a795a7", id);
					if (c2 == 200) {
						// response.setStatus(this.delete("scimCross", "X", param,
						// "anev3ox8b.accounts.ondemand.com", id)); su sorgente SCIM
						response.setStatus(
								this.delete("apiAccessCross", "X", param, "anev3ox8b.accounts.ondemand.com", id));
					} else
						response.setStatus(500);
				} else
					response.setStatus(500);

			}
		} catch (Exception e) {
			response.setStatus(500);
			response.getWriter().append("Error: " + e.getMessage().toString());
			e.printStackTrace();
		}

	}

	private Integer post(String destination, String trust, String master, StringBuffer jb) {
		Integer status = 0;
		JsonObject jsonResponse = new JsonObject();
		try {
			JsonObject newObject = new JsonParser().parse(jb.toString()).getAsJsonObject();
			JsonObject newObject2 = newObject.deepCopy();
			membersNewArray = new JsonArray();

			// direct in post
			JsonArray membersArray = newObject2.getAsJsonArray("groups");
			if (membersArray != null) {

				for (int i = 0; i < membersArray.size(); i++) {
					JsonObject member = new JsonObject();
					member = (JsonObject) membersArray.get(i);
					direct = member.get("type").toString().replace("\"", "").toUpperCase();
					member.remove("type");
					member.addProperty("type", direct);
					membersNewArray.add(member);
				}
				newObject.remove("groups");
				newObject.add("groups", membersNewArray);
			}
			// direct in post
			HttpClient httpClient = HttpClientAccessor.getHttpClient(DestinationAccessor.getDestination(destination));
			HttpPost requestScim = new HttpPost("/Users");
			requestScim.addHeader("content-type", "application/scim+json");
			newObject.remove("urn:ietf:params:scim:schemas:extension:UAA:2.0:User");
			newObject.remove("schemas");
			newObject.addProperty("ID", UUID.randomUUID().toString());
			newObject.addProperty("origin", trust);
			JsonArray schemasArray = new JsonArray();
			schemasArray.add("urn:scim:schemas:core:1.0");
			newObject.add("schemas", schemasArray);
			StringEntity paramsScim = new StringEntity(newObject.toString());
			requestScim.setEntity(paramsScim);
			HttpResponse responseScim = httpClient.execute(requestScim);

			if (responseScim.getStatusLine().getStatusCode() == 201 && master == "X") {
				// chiamata Smart Control
				HttpEntity entityScim = responseScim.getEntity();
				if (entityScim != null) {
					String responseScimJson = EntityUtils.toString(entityScim);
					status = 201;
					jsonResponse = (JsonObject) new JsonParser().parse(responseScimJson);
					// HttpClient httpClientAida = HttpClientAccessor
					// .getHttpClient(DestinationAccessor.getDestination("destinazioneApiManScimProt"));

					JsonArray custom = newObject2.getAsJsonArray("urn:ietf:params:scim:schemas:extension:UAA:2.0:User");

					if ((custom != null) && (custom.size() > 0)) {

						// Inserisco i nuovi record passati in input --> TABELLA USERS

						this._insertCustomUser(jsonResponse.get("id").toString().replace("\"", ""),
								jsonResponse.get("userName").toString().replace("\"", ""),
								jsonResponse.getAsJsonObject("name").get("familyName").getAsString(),
								jsonResponse.getAsJsonObject("name").get("givenName").getAsString(), jsonResponse
										.getAsJsonArray("emails").get(0).getAsJsonObject().get("value").getAsString());

						// Inserisco i nuovi record passati in input --> TABELLA Controllori
						customOutArray = this._insertControllori(jsonResponse, custom);

						// Inserisco i nuovi record passati in input --> TABELLA USERS ATTRIBUTES
						customOutArray = this._insertCustomUsersAttributes(
								jsonResponse.get("id").toString().replace("\"", ""), custom);

					}

					// Conversione della chiamata 1.1 in 2.0 per compac
					jsonResponse.add("urn:ietf:params:scim:schemas:extension:UAA:2.0:User", this.customOutArray);
					jsonResponse.remove("schemas");
					jsonResponse.remove("approvals");
					jsonResponse.remove("verified");
					jsonResponse.remove("previousLogonTime");
					jsonResponse.remove("lastLogonTime");
					jsonResponse.remove("origin");
					jsonResponse.remove("zoneId");
					jsonResponse.remove("passwordLastModified");

					membersArray = jsonResponse.getAsJsonArray("groups");
					membersNewArray = new JsonArray();

					if (membersArray != null) {

						for (int i = 0; i < membersArray.size(); i++) {
							JsonObject member = new JsonObject();
							member = (JsonObject) membersArray.get(i);
							direct = member.get("type").toString().replace("\"", "").toLowerCase();
							member.remove("type");
							member.addProperty("type", direct);
							membersNewArray.add(member);
						}
						jsonResponse.remove("groups");
						jsonResponse.add("groups", membersNewArray);
					}
					// jsonResponse.remove("externalId");
					JsonArray schemasArray2 = new JsonArray();
					schemasArray2.add("urn:ietf:params:scim:schemas:core:2.0:User");
					schemasArray2.add("urn:ietf:params:scim:schemas:extension:UAA:2.0:User");
					jsonResponse.add("schemas", schemasArray2);
					// response.getWriter().write(new Gson().toJson(jsonResponse));
					globalResponse = jsonResponse;
				}
			} else {
				status = responseScim.getStatusLine().getStatusCode();
			}

			return status;
		} catch (Exception e) {
			e.printStackTrace();
			return 500;
		}
	}

	/*
	 * Inserisce i nuovi record nella tabella di processo USER
	 */
	private void _insertCustomUser(String sId, String sExternalId, String sCognome, String sNome, String sMail) {

		String stringUrl = "/Users";
		HttpClient httpClientCustom = HttpClientAccessor
				.getHttpClient(DestinationAccessor.getDestination("destinazioneSCIM"));

		// Mappo i campi di processo
		JsonObject oNewUser = new JsonObject();
		oNewUser.addProperty("externalId", sExternalId);
		oNewUser.addProperty("id", sId);
		oNewUser.addProperty("familyName", sCognome);
		oNewUser.addProperty("givenName", sNome);
		oNewUser.addProperty("mail", sMail);

		// Converto in stringa
		StringEntity sAttributoDaScrivere = new StringEntity(oNewUser.toString(), "UTF-8");

		// Eseguo la post di scrittura sulla tabella custom
		HttpPost postCustomEntity = new HttpPost(stringUrl);
		postCustomEntity.addHeader("content-type", "application/json");
		postCustomEntity.setEntity(sAttributoDaScrivere);

		try {
			httpClientCustom.execute(postCustomEntity);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * Inserisce i nuovi record nella tabella di processo USERATTRIBUTES e
	 * restituisce tutti i record inseriti correttamente
	 */
	private JsonArray _insertCustomUsersAttributes(String sID, JsonArray aCustomAttribute) {

		JsonArray aJOOut = new JsonArray();

		String stringUrl = "/UsersRolesAndAttributes";
		HttpClient httpClientCustom = HttpClientAccessor
				.getHttpClient(DestinationAccessor.getDestination("destinazioneSCIM"));

		for (int i = 0; i < aCustomAttribute.size(); i++) {

			// Recupero l'elemento che ho ricevuto in input
			JsonElement oNuovoAttributoCustom = aCustomAttribute.get(i);
			JsonObject oNuovoAttributoCustomJO = oNuovoAttributoCustom.getAsJsonObject();

			String sAttributeId = "";
			String sAttributeValue = "";
			if (!oNuovoAttributoCustomJO.get("unitaOperativa").getAsString().equals("")) {
				sAttributeId = "003";
				sAttributeValue = oNuovoAttributoCustomJO.get("unitaOperativa").getAsString();
			} else if (!oNuovoAttributoCustomJO.get("zona").getAsString().equals("")) {
				sAttributeId = "002";
				sAttributeValue = oNuovoAttributoCustomJO.get("zona").getAsString();
			} else if (!oNuovoAttributoCustomJO.get("area").getAsString().equals("")) {
				sAttributeId = "001";
				sAttributeValue = oNuovoAttributoCustomJO.get("area").getAsString();
			}

			// Mappo i campi di processo
			JsonObject oAttributoDaScrivere = new JsonObject();
			oAttributoDaScrivere.addProperty("id", sID);
			oAttributoDaScrivere.addProperty("attributeId", sAttributeId);
			oAttributoDaScrivere.addProperty("attributeValue", sAttributeValue);
			oAttributoDaScrivere.addProperty("dateFrom", oNuovoAttributoCustomJO.get("dateFrom").getAsString());
			oAttributoDaScrivere.addProperty("dateTo", oNuovoAttributoCustomJO.get("dateTo").getAsString());
			oAttributoDaScrivere.addProperty("project", "SmartControl");
			oAttributoDaScrivere.addProperty("role", oNuovoAttributoCustomJO.get("ruolo").getAsString());

			// Converto in stringa
			StringEntity sAttributoDaScrivere = new StringEntity(oAttributoDaScrivere.toString(), "UTF-8");

			// Eseguo la post di scrittura sulla tabella custom
			HttpPost postCustomEntity = new HttpPost(stringUrl);
			postCustomEntity.addHeader("content-type", "application/json");
			postCustomEntity.setEntity(sAttributoDaScrivere);

			try {
				HttpResponse scimResponseCustom = httpClientCustom.execute(postCustomEntity);
				if (scimResponseCustom.getStatusLine().getStatusCode() == 201) {
					aJOOut.add(oAttributoDaScrivere);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return aJOOut;

	}

	// insert all'interno della tabella controllori
	private JsonArray _insertControllori(JsonObject jsonResponse, JsonArray aCustomAttribute) throws ODataException {

		JsonArray aJOOut = new JsonArray();

		String stringUrl = "/Controllori";
		HttpClient httpClientCustom = HttpClientAccessor
				.getHttpClient(DestinationAccessor.getDestination("destinazioneControllori"));

		/**
		 * controllo se è presente il dato prima dell'inserimento o lo cancello in
		 * delete
		 * 
		 * // Costruisco la chiamata per recuperare tutti i record di quell' ID String
		 * filter = "?$filter=id%20eq%20(guid'" + sID + "')&$format=json"; String
		 * stringUrl = "/Users" + filter; HttpGet getScimCustom = new
		 * HttpGet(stringUrl); HttpResponse scimResponseCustom =
		 * httpClientCustom.execute(getScimCustom);
		 * 
		 * HttpEntity entityScimCustom = scimResponseCustom.getEntity(); JsonObject
		 * jsonResponseCustom = (JsonObject) new
		 * JsonParser().parse(EntityUtils.toString(entityScimCustom)); JsonArray
		 * jsonResponseCustomArray =
		 * jsonResponseCustom.getAsJsonObject("d").getAsJsonArray("results");
		 * 
		 * // Cancello tutti i record recuperati for (int i = 0; i <
		 * jsonResponseCustomArray.size(); i++) {
		 */

		for (int i = 0; i < aCustomAttribute.size(); i++) {

			// Recupero l'elemento che ho ricevuto in input
			JsonElement oNuovoAttributoCustom = aCustomAttribute.get(i);
			JsonObject oNuovoAttributoCustomJO = oNuovoAttributoCustom.getAsJsonObject();

			List<HR_MNG> hr_MNG = new DefaultZSCPAIDASRVService().getAllHR_MNG().filter(HR_MNG.I_UOSAP.eq("30026815"))
					.filter(HR_MNG.I_LEVEL.eq("10")).select().execute(new ErpConfigContext("oDataSapECC"));

			List<HR_SIA> hr_SIA = new DefaultZSCPAIDASRVService().getAllHR_SIA().filter(HR_SIA.I_UOSAP.eq("30026815"))
					.filter(HR_SIA.I_LEVEL.eq("10")).select().execute(new ErpConfigContext("oDataSapECC"));

			List<Hrorg> hrorgGer = new DefaultZSCPAIDASRVService().getAllHrorg()
					.filter(Hrorg.I_USRID.eq(hr_MNG.get(0).getUsrid())).select()
					.execute(new ErpConfigContext("oDataSapECC"));

			List<Hrorg> hrorgHSE = new DefaultZSCPAIDASRVService().getAllHrorg()
					.filter(Hrorg.I_USRID.eq(hr_SIA.get(0).getUsrid())).select()
					.execute(new ErpConfigContext("oDataSapECC"));

			// Mappo i campi di processo
			JsonObject oAttributoDaScrivere = new JsonObject();
			oAttributoDaScrivere.addProperty("user", jsonResponse.get("userName").toString().replace("\"", ""));
			oAttributoDaScrivere.addProperty("nome",
					jsonResponse.getAsJsonObject("name").get("familyName").getAsString());
			oAttributoDaScrivere.addProperty("cognome",
					jsonResponse.getAsJsonObject("name").get("givenName").getAsString());
			oAttributoDaScrivere.addProperty("email",
					jsonResponse.getAsJsonArray("emails").get(0).getAsJsonObject().get("value").getAsString());
			oAttributoDaScrivere.addProperty("ruolo", oNuovoAttributoCustomJO.get("ruolo").getAsString());
			oAttributoDaScrivere.addProperty("area_operativa", oNuovoAttributoCustomJO.get("area").getAsString());
			oAttributoDaScrivere.addProperty("zona", oNuovoAttributoCustomJO.get("zona").getAsString());
			oAttributoDaScrivere.addProperty("unita_operativa",
					oNuovoAttributoCustomJO.get("unitaOperativa").getAsString());
			oAttributoDaScrivere.addProperty("matricolaResponsabile", hrorgGer.get(0).getUsrid());
			oAttributoDaScrivere.addProperty("nominativoResponsabile", hrorgGer.get(0).getEname());
			// oAttributoDaScrivere.addProperty("cognomeResponsabile", );
			oAttributoDaScrivere.addProperty("emailResponsabile", hrorgGer.get(0).getMail());
			oAttributoDaScrivere.addProperty("matricolaResponsabileHSE", hrorgHSE.get(0).getUsrid());
			oAttributoDaScrivere.addProperty("nominativoResponsabileHSE", hrorgHSE.get(0).getEname());
			// oAttributoDaScrivere.addProperty("cognomeResponsabileHSE", );
			oAttributoDaScrivere.addProperty("emailResponsabileHSE", hrorgHSE.get(0).getMail());

			// Converto in stringa
			StringEntity sAttributoDaScrivere = new StringEntity(oAttributoDaScrivere.toString(), "UTF-8");

			// Eseguo la post di scrittura sulla tabella custom
			HttpPost postCustomEntity = new HttpPost(stringUrl);
			postCustomEntity.addHeader("content-type", "application/json");
			postCustomEntity.setEntity(sAttributoDaScrivere);

			try {
				HttpResponse scimResponseCustom = httpClientCustom.execute(postCustomEntity);
				if (scimResponseCustom.getStatusLine().getStatusCode() == 201) {
					aJOOut.add(oAttributoDaScrivere);
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}

		return aJOOut;

	}

	/*
	 * Cancella dalla tabella di processo USER tutti i record che hanno l'ID passato
	 * in input
	 */
	private void _deleteCustomUser(String sID) {
		try {

			// Costruisco la chiamata per recuperare tutti i record di quell' ID
			String filter = "?$filter=id%20eq%20(guid'" + sID + "')&$format=json";
			String stringUrl = "/Users" + filter;
			HttpClient httpClientCustom = HttpClientAccessor
					.getHttpClient(DestinationAccessor.getDestination("destinazioneSCIM"));
			HttpGet getScimCustom = new HttpGet(stringUrl);
			HttpResponse scimResponseCustom = httpClientCustom.execute(getScimCustom);

			HttpEntity entityScimCustom = scimResponseCustom.getEntity();
			JsonObject jsonResponseCustom = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityScimCustom));
			JsonArray jsonResponseCustomArray = jsonResponseCustom.getAsJsonObject("d").getAsJsonArray("results");

			// Cancello tutti i record recuperati
			for (int i = 0; i < jsonResponseCustomArray.size(); i++) {

				JsonElement oResponseLine = jsonResponseCustomArray.get(i);
				JsonObject oResponseLineJO = oResponseLine.getAsJsonObject();

				String sPathDelete = "(externalId=" + oResponseLineJO.get("externalId").toString().replace("\"", "'")
						+ ")";

				HttpDelete requestCustomDelete = new HttpDelete("/Users" + sPathDelete);
				httpClientCustom.execute(requestCustomDelete);

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Cancella dalla tabella di processo USERATTRIBUTES tutti i record che hanno
	 * l'ID passato in input
	 */
	private void _deleteCustomUsersAttributes(String sID) {
		try {

			// Costruisco la chiamata per recuperare tutti i record di quell' ID
			String filter = "?$filter=id%20eq%20(guid'" + sID + "')&$format=json";
			String stringUrl = "/UsersRolesAndAttributes" + filter;
			HttpClient httpClientCustom = HttpClientAccessor
					.getHttpClient(DestinationAccessor.getDestination("destinazioneSCIM"));
			HttpGet getScimCustom = new HttpGet(stringUrl);
			HttpResponse scimResponseCustom = httpClientCustom.execute(getScimCustom);

			HttpEntity entityScimCustom = scimResponseCustom.getEntity();
			JsonObject jsonResponseCustom = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityScimCustom));
			JsonArray jsonResponseCustomArray = jsonResponseCustom.getAsJsonObject("d").getAsJsonArray("results");

			// Cancello tutti i record recuperati
			for (int i = 0; i < jsonResponseCustomArray.size(); i++) {

				JsonElement oResponseLine = jsonResponseCustomArray.get(i);
				JsonObject oResponseLineJO = oResponseLine.getAsJsonObject();

				String sIDDelete = oResponseLineJO.get("id").toString().replace("\"", "'");
				String sAttributeId = oResponseLineJO.get("attributeId").toString().replace("\"", "'");
				String sAttributeValue = oResponseLineJO.get("attributeValue").toString().replace("\"", "'");
				String sDateFrom = oResponseLineJO.get("dateFrom").toString().replace("\"", "'");
				String sDateTo = oResponseLineJO.get("dateTo").toString().replace("\"", "'");

				String sPathDelete = "(id=guid" + sIDDelete + ",attributeId=" + sAttributeId + ",attributeValue="
						+ sAttributeValue + ",dateFrom=" + sDateFrom + ",dateTo=" + sDateTo + ")";

				HttpDelete requestCustomDelete = new HttpDelete("/UsersRolesAndAttributes" + sPathDelete);
				httpClientCustom.execute(requestCustomDelete);
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Cancella dalla tabella di processo USER tutti i record che hanno l'ID passato
	 * in input
	 */
	private void _deleteCustomControllore(String user) {
		try {

			// Costruisco la chiamata per recuperare tutti i record di quell' ID
			String filter = "?$filter=user%20eq%20" + user + "&$format=json";
			String stringUrl = "/Controllori" + filter;
			HttpClient httpClientCustom = HttpClientAccessor
					.getHttpClient(DestinationAccessor.getDestination("destinazioneControllori"));
			HttpGet getScimCustom = new HttpGet(stringUrl);
			HttpResponse scimResponseCustom = httpClientCustom.execute(getScimCustom);

			HttpEntity entityScimCustom = scimResponseCustom.getEntity();
			JsonObject jsonResponseCustom = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityScimCustom));
			JsonArray jsonResponseCustomArray = jsonResponseCustom.getAsJsonObject("d").getAsJsonArray("results");

			// Cancello tutti i record recuperati
			for (int i = 0; i < jsonResponseCustomArray.size(); i++) {

				String sPathDelete = "(user=" + user + ")";

				HttpDelete requestCustomDelete = new HttpDelete("/Controllori" + sPathDelete);
				httpClientCustom.execute(requestCustomDelete);

			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
