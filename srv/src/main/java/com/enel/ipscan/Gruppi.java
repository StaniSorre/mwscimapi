package com.enel.ipscan;

import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
//import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpGet;
import com.google.gson.JsonArray;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.client.methods.HttpPatch;
import java.io.BufferedReader;
import java.util.Date;

//
@WebServlet("/Groups/*")
@ServletSecurity(@HttpConstraint(rolesAllowed = { "ApiScimExternal_SC" }))
public class Gruppi extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private JsonObject globalResponse;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/scim+json");
		try {

			String param = new String();
			if (request.getPathInfo() != null) {
				param = request.getPathInfo().toString();

				if (param.charAt(0) == '/') {
					param = param.substring(1);
					String id = param;
					// HttpClient httpClient =
					// HttpClientAccessor.getHttpClient(DestinationAccessor.getDestination("apiAccess"));
					// // "scimCross" su sorgente
					// // SCIM
					HttpClient httpClient = HttpClientAccessor
							.getHttpClient(DestinationAccessor.getDestination("apiAccessCross"));
					HttpGet getScim = new HttpGet("/Groups/" + id);
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
						// version update
						JsonArray membersArray = jsonResponse.getAsJsonArray("members");
						if (membersArray != null) {
							JsonArray membersNewArray = new JsonArray();
							for (int i = 0; i < membersArray.size(); i++) {
								JsonObject member = new JsonObject();
								member = (JsonObject) membersArray.get(i);
								member.remove("origin");
								member.remove("type");
								member.addProperty("type", "User");
								membersNewArray.add(member);
							}
							jsonResponse.remove("members");
							jsonResponse.add("members", membersNewArray);
						}
						jsonResponse.remove("zoneId");
						jsonResponse.remove("description");
						jsonResponse.remove("schemas");
						JsonArray schemasArray2 = new JsonArray();
						schemasArray2.add("urn:ietf:params:scim:schemas:core:2.0:Group");
						jsonResponse.add("schemas", schemasArray2);
						response.getWriter().write(new Gson().toJson(jsonResponse));

					} else {
						response.setStatus(scimResponse.getStatusLine().getStatusCode());
						JsonObject jsonErrResp = (JsonObject) new JsonParser()
								.parse(EntityUtils.toString(scimResponse.getEntity()));
						//// response.getWriter().write(new Gson().toJson(jsonErrResp));
					}
				}
			} else {
				param = request.getQueryString();
				// HttpClient httpClient =
				// HttpClientAccessor.getHttpClient(DestinationAccessor.getDestination("apiAccess"));
				// "scimCross" su sorgente SCIM
				HttpClient httpClient = HttpClientAccessor
						.getHttpClient(DestinationAccessor.getDestination("apiAccessCross"));
				HttpGet getScim;
				if (param != null) {
					getScim = new HttpGet(
							"/Groups/" + param.split("%20eq%20")[1].replace("%27", "").replace("%22", ""));
				} else {
					getScim = new HttpGet("/Groups");
				}
				getScim.addHeader("Accept", "application/scim+json");
				HttpResponse scimResponse = httpClient.execute(getScim);
				HttpEntity entityScim = scimResponse.getEntity();
				JsonObject jsonResponseScim = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityScim));
				JsonObject jsonResponseScim2 = new JsonObject();
				if (param != null) {
					JsonArray newResources1 = new JsonArray();

					// version update
					String version = jsonResponseScim.getAsJsonObject("meta").get("version").toString();
					JsonObject meta2 = jsonResponseScim.getAsJsonObject("meta");
					meta2.remove("version");
					meta2.addProperty("version", version);
					jsonResponseScim.remove("meta");
					jsonResponseScim.add("meta", meta2);
					// version update
					JsonArray membersArray = jsonResponseScim.getAsJsonArray("members");

					JsonObject prova = new JsonObject();// prova
					jsonResponseScim2.addProperty("startIndex", 1);
					jsonResponseScim2.addProperty("itemsPerPage", 100);
					if (membersArray != null) {
						JsonArray membersNewArray = new JsonArray();
						for (int j = 0; j < membersArray.size(); j++) {
							JsonObject member = new JsonObject();
							member = (JsonObject) membersArray.get(j);
							member.remove("origin");
							member.remove("type");
							member.addProperty("type", "User");
							membersNewArray.add(member);
						}
						jsonResponseScim.remove("members");
						jsonResponseScim.add("members", membersNewArray);
						jsonResponseScim.remove("description");
						jsonResponseScim.remove("zoneId");
						jsonResponseScim.remove("schemas");
						JsonArray schemasArray2 = new JsonArray();
						schemasArray2.add("urn:ietf:params:scim:schemas:core:2.0:Group");
						jsonResponseScim.add("schemas", schemasArray2);
						newResources1.add(jsonResponseScim);
						jsonResponseScim2.addProperty("totalResults", 1);
					} else {
						jsonResponseScim2.addProperty("totalResults", 0);
					}

					jsonResponseScim2.add("Resources", newResources1);
					JsonArray schemasArrayList = new JsonArray();
					schemasArrayList.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
					jsonResponseScim2.add("schemas", schemasArrayList);

					response.getWriter().write(new Gson().toJson(jsonResponseScim2));
				} else {
					JsonArray resources = jsonResponseScim.getAsJsonArray("resources");
					JsonArray newResources = new JsonArray();
					if (resources.size() > 0) {
						Integer numGroups = 0;
						for (int i = 0; i < resources.size(); i++) {

							JsonObject scimRes = (JsonObject) resources.get(i);
							if (scimRes.get("id").toString().replace("\"", "").startsWith("IPSCAN")) {
								// response.getWriter().write(new Gson().toJson(scimRes));
								numGroups = numGroups + 1;
								String timeStamp = new java.text.SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss'Z'").format(new Date());
								// version update
								String version = scimRes.getAsJsonObject("meta").get("version").toString();
								JsonObject meta2 = scimRes.getAsJsonObject("meta");
								meta2.remove("version");
								meta2.addProperty("version", version);
								meta2.addProperty("resourceType", "Group");
								meta2.addProperty("lastModified", timeStamp);
								meta2.addProperty("location", request.getRequestURL().toString() + "/" + scimRes.get("id").toString().replace("\"", ""));
								scimRes.remove("meta");
								scimRes.add("meta", meta2);
								// version update
								JsonArray membersArray = scimRes.getAsJsonArray("members");
								if (membersArray != null) {
									JsonArray membersNewArray = new JsonArray();
									for (int j = 0; j < membersArray.size(); j++) {
										JsonObject member = new JsonObject();
										member = (JsonObject) membersArray.get(j);
										member.remove("origin");
										member.remove("type");
										member.addProperty("type", "User");
										membersNewArray.add(member);
									}
									scimRes.remove("members");
									scimRes.add("members", membersNewArray);
									jsonResponseScim.remove("description");
								}
								scimRes.remove("zoneId");
								scimRes.remove("schemas");
								JsonArray schemasArray2 = new JsonArray();
								schemasArray2.add("urn:ietf:params:scim:schemas:core:2.0:Group");
								scimRes.add("schemas", schemasArray2);
								newResources.add(scimRes);
							}
						}
						jsonResponseScim.remove("resources");
						jsonResponseScim.add("Resources", newResources);
						jsonResponseScim.remove("totalResults");
						jsonResponseScim.addProperty("totalResults", numGroups);

						jsonResponseScim.remove("schemas");
						JsonArray schemasArray2 = new JsonArray();
						schemasArray2.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
						jsonResponseScim.add("schemas", schemasArray2);
						response.getWriter().write(new Gson().toJson(jsonResponseScim));
					}
				}
			}
		} catch (Exception e) {
			response.setStatus(500);
			// response.getWriter().append("Error: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}

	public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if (request.getMethod().equalsIgnoreCase("PATCH")) {
			doPatch(request, response);
		} else {
			super.service(request, response);
		}
	}

	private Integer patch(String destination, String trust, String master, String param, StringBuffer jb) {
		try {
			Integer status = 0;
			JsonObject newObject = new JsonParser().parse(jb.toString()).getAsJsonObject();
			JsonObject jsonResponse = new JsonObject();
			if (param.charAt(0) == '/') {
				param = param.substring(1);
				HttpClient httpClient = HttpClientAccessor
						.getHttpClient(DestinationAccessor.getDestination(destination));
				HttpPatch requestScim = new HttpPatch("/Groups/" + param);
				requestScim.addHeader("content-type", "application/scim+json");
				// questo parametro deve essere passato nell'header, obbligatorio
				requestScim.addHeader("If-Match", "*");
				newObject.remove("schemas");
				JsonArray schemasArray = new JsonArray();
				schemasArray.add("urn:scim:schemas:core:1.0");
				newObject.add("schemas", schemasArray);

				JsonArray operationsArray = newObject.getAsJsonArray("Operations");
				if (operationsArray != null) {
					JsonArray membersNewArray = new JsonArray();
					String operationString;
					JsonObject operationObj;
					JsonObject objectValue;
					String newParam = "";
					for (int j = 0; j < operationsArray.size(); j++) {

						JsonObject member = new JsonObject();
						operationObj = (JsonObject) operationsArray.get(j);
						// if (operationObj.get("path").toString().replace("\"", "") == "members") {
						operationString = operationObj.get("op").toString().replace("\"", "");
						if (operationString.equals("add")) {
							JsonArray usersArray = operationObj.getAsJsonArray("value");
							if (usersArray != null) {

								for (int k = 0; k < usersArray.size(); k++) {
									objectValue = (JsonObject) usersArray.get(k);

									// member.addProperty("value",
									// objectValue.get("value").toString().replace("\"",""));cancellato ora
									/// inseriamo id corretto
									// HttpClient httpClientCross = HttpClientAccessor
									// .getHttpClient(DestinationAccessor.getDestination("apiAccess"));
									// //"scimCross" su sorgente SCIM
									HttpClient httpClientCross = HttpClientAccessor
											.getHttpClient(DestinationAccessor.getDestination("apiAccessCross"));
									HttpGet getScim = new HttpGet(
											"/Users/" + objectValue.get("value").toString().replace("\"", ""));
									getScim.addHeader("Accept", "application/scim+json");
									HttpResponse scimResponse = httpClientCross.execute(getScim);
									HttpEntity entityScim = scimResponse.getEntity();
									JsonObject jsonUser = (JsonObject) new JsonParser()
											.parse(EntityUtils.toString(entityScim));
									String id = jsonUser.get("id").toString().replace("\"", "'");
									String filterUser = "?filter=id%20eq%20" + id;
									HttpGet getListUsers = new HttpGet("/Users" + filterUser);
									getListUsers.addHeader("Accept", "application/scim+json");
									HttpResponse getListUsersResponse = httpClient.execute(getListUsers);
									HttpEntity entityListUsers = getListUsersResponse.getEntity();
									JsonObject jsonResponseUsers = (JsonObject) new JsonParser()
											.parse(EntityUtils.toString(entityListUsers));
									JsonArray jsonResponseUsersArray = jsonResponseUsers.getAsJsonArray("resources");
									for (int i = 0; i < jsonResponseUsersArray.size(); i++) {
										JsonObject userEl = (JsonObject) jsonResponseUsersArray.get(i);
										if (userEl.get("origin").toString().replace("\"", "").equals(trust)) {
											newParam = userEl.get("id").toString().replace("\"", "");
											break;
										}
									}
									member.addProperty("value", newParam);
									// inseriamo id corretto
									member.addProperty("origin", trust);
									member.addProperty("type", "USER");
									member.addProperty("operation", "add");
									membersNewArray.add(member);
								}
							}
						} else if (operationString.equals("remove")) {
							String pathString = operationObj.get("path").toString();
							// member.addProperty("value", pathString.substring(20, pathString.length() -
							// 4));
							/// inseriamo id corretto
							// HttpClient httpClientCross = HttpClientAccessor
							// .getHttpClient(DestinationAccessor.getDestination("apiAccess")); //
							// "scimCross" su sorgente SCIM
							HttpClient httpClientCross = HttpClientAccessor
									.getHttpClient(DestinationAccessor.getDestination("apiAccessCross"));
							HttpGet getScim = new HttpGet(
									"/Users/" + pathString.substring(20, pathString.length() - 4).replace("\"", ""));
							getScim.addHeader("Accept", "application/scim+json");
							HttpResponse scimResponse = httpClientCross.execute(getScim);
							HttpEntity entityScim = scimResponse.getEntity();
							JsonObject jsonUser = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityScim));
							String id = jsonUser.get("id").toString().replace("\"", "'");
							String filterUser = "?filter=id%20eq%20" + id;
							HttpGet getListUsers = new HttpGet("/Users" + filterUser);
							getListUsers.addHeader("Accept", "application/scim+json");
							HttpResponse getListUsersResponse = httpClient.execute(getListUsers);
							HttpEntity entityListUsers = getListUsersResponse.getEntity();
							JsonObject jsonResponseUsers = (JsonObject) new JsonParser()
									.parse(EntityUtils.toString(entityListUsers));
							JsonArray jsonResponseUsersArray = jsonResponseUsers.getAsJsonArray("resources");
							for (int i = 0; i < jsonResponseUsersArray.size(); i++) {
								JsonObject userEl = (JsonObject) jsonResponseUsersArray.get(i);
								if (userEl.get("origin").toString().replace("\"", "").equals(trust)) {
									newParam = userEl.get("id").toString().replace("\"", "");
									break;
								}
							}
							member.addProperty("value", newParam);
							// inseriamo id corretto

							member.addProperty("origin", trust);
							member.addProperty("type", "USER");
							member.addProperty("operation", "delete");
							membersNewArray.add(member);
						}
					}
					// }
					// newObject.remove("members");
					newObject.add("members", membersNewArray);
					newObject.remove("Operations");
				}

				StringEntity paramsScim = new StringEntity(newObject.toString());
				requestScim.setEntity(paramsScim);
				HttpResponse responseScim = httpClient.execute(requestScim);
				if (responseScim.getStatusLine().getStatusCode() == 200 && master == "X") {
					HttpEntity entityScim = responseScim.getEntity();
					String responseScimJson = EntityUtils.toString(entityScim);
					status = 200;
					jsonResponse = (JsonObject) new JsonParser().parse(responseScimJson);
					// version update
					String version = jsonResponse.getAsJsonObject("meta").get("version").toString();
					JsonObject meta2 = jsonResponse.getAsJsonObject("meta");
					meta2.remove("version");
					meta2.addProperty("version", version);
					jsonResponse.remove("meta");
					jsonResponse.add("meta", meta2);
					// version update
					JsonArray membersArrayPatch = jsonResponse.getAsJsonArray("members");
					if (membersArrayPatch != null) {
						JsonArray membersNewArrayPatch = new JsonArray();
						for (int i = 0; i < membersArrayPatch.size(); i++) {
							JsonObject memberPatch = new JsonObject();
							memberPatch = (JsonObject) membersArrayPatch.get(i);
							memberPatch.remove("origin");
							memberPatch.remove("type");
							memberPatch.addProperty("type", "User");
							membersNewArrayPatch.add(memberPatch);
						}
						jsonResponse.remove("members");
						jsonResponse.add("members", membersNewArrayPatch);
					}
					jsonResponse.remove("zoneId");
					jsonResponse.remove("schemas");
					JsonArray schemasArray2 = new JsonArray();
					schemasArray2.add("urn:ietf:params:scim:schemas:core:2.0:Group");
					jsonResponse.add("schemas", schemasArray2);
					globalResponse = jsonResponse;
					// response.getWriter().write(new Gson().toJson(jsonResponse));
				} else {
					status = responseScim.getStatusLine().getStatusCode();
					// JsonObject jsonErrResp = (JsonObject) new JsonParser()
					// .parse(EntityUtils.toString(responseScim.getEntity()));
					// response.getWriter().write(new Gson().toJson(jsonErrResp));
				}
			}

			return status;
		} catch (Exception e) {
			// response.getWriter().append("Error: " + e.getMessage().toString());
			e.printStackTrace();
			return 500;
		}
	}

	protected void doPatch(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/scim+json");
		try {
			// JsonObject newObject = new JsonObject();
			StringBuffer jb = new StringBuffer();
			String line = null;
			BufferedReader reader = request.getReader();
			while ((line = reader.readLine()) != null) {
				jb.append(line);
			}
			// newObject = new JsonParser().parse(jb.toString()).getAsJsonObject();
			String param = request.getPathInfo().toString();
			// JsonObject jsonResponse = new JsonObject();
			if (this.patch("apiAccess", "anev3ox8b.accounts.ondemand.com", "", param, jb) == 200) {
				// if (this.patch("scim", "afqxozn24.accounts.ondemand.com", "", param, jb) ==
				// 200) { su sorgente SCIM
				if (this.patch("apiAccess", "cfapps.eu10.hana.ondemand.com4a795a7", "", param, jb) == 200) {
					// if (this.patch("scim", "cfapps.eu10.hana.ondemand.com46ae131", "", param, jb)
					// == 200) { su sorgente SCIM
					Integer stat = this.patch("apiAccessCross", "anev3ox8b.accounts.ondemand.com", "X", param, jb);
					// Integer stat = this.patch("scimCross", "afqxozn24.accounts.ondemand.com",
					// "X", param, jb); su sorgente SCIM
					response.setStatus(stat);
					if (stat == 200)
						response.getWriter().write(new Gson().toJson(globalResponse));
				} else
					response.setStatus(500);
			} else
				response.setStatus(500);
			/*
			 * if (param.charAt(0) == '/') { param = param.substring(1); HttpClient
			 * httpClient =
			 * HttpClientAccessor.getHttpClient(DestinationAccessor.getDestination("scim"));
			 * HttpPatch requestScim = new HttpPatch("/Groups/" + param);
			 * requestScim.addHeader("content-type", "application/scim+json"); // questo
			 * parametro deve essere passato nell'header, obbligatorio
			 * requestScim.addHeader("If-Match", "*"); newObject.remove("schemas");
			 * JsonArray schemasArray = new JsonArray();
			 * schemasArray.add("urn:scim:schemas:core:1.0"); newObject.add("schemas",
			 * schemasArray); JsonArray operationsArray =
			 * newObject.getAsJsonArray("Operations"); if (operationsArray != null) {
			 * JsonArray membersNewArray = new JsonArray(); String operationString;
			 * 
			 * JsonObject operationObj; JsonObject objectValue; for (int j = 0; j <
			 * operationsArray.size(); j++) { JsonObject member = new JsonObject();
			 * operationObj = (JsonObject) operationsArray.get(j); // if
			 * (operationObj.get("path").toString().replace("\"", "") == "members") {
			 * operationString = operationObj.get("op").toString().replace("\"", "");
			 * 
			 * if (operationString.equals("add")) { JsonArray usersArray =
			 * operationObj.getAsJsonArray("value"); if (usersArray != null) { for (int k =
			 * 0; k < usersArray.size(); k++) { objectValue = (JsonObject)
			 * usersArray.get(k); // if (objectValue.get("value").toString().replace("\"",
			 * "") == "User") { member.addProperty("value",
			 * objectValue.get("value").toString().replace("\"", ""));
			 * member.addProperty("origin", "afqxozn24.accounts.ondemand.com");
			 * member.addProperty("type", "USER"); member.addProperty("operation", "add");
			 * membersNewArray.add(member); } } } else if (operationString.equals("remove"))
			 * { String pathString = operationObj.get("path").toString();
			 * member.addProperty("value", pathString.substring(20, pathString.length() -
			 * 4)); member.addProperty("origin", "afqxozn24.accounts.ondemand.com");
			 * member.addProperty("type", "USER"); member.addProperty("operation",
			 * "delete"); membersNewArray.add(member); } } // } //
			 * newObject.remove("members"); newObject.add("members", membersNewArray);
			 * newObject.remove("Operations"); } StringEntity paramsScim = new
			 * StringEntity(newObject.toString()); requestScim.setEntity(paramsScim);
			 * HttpResponse responseScim = httpClient.execute(requestScim); if
			 * (responseScim.getStatusLine().getStatusCode() == 200) { HttpEntity entityScim
			 * = responseScim.getEntity(); String responseScimJson =
			 * EntityUtils.toString(entityScim); response.setStatus(200); JsonObject
			 * jsonResponse = (JsonObject) new JsonParser().parse(responseScimJson);
			 * 
			 * JsonArray membersArrayPatch = jsonResponse.getAsJsonArray("members"); if
			 * (membersArrayPatch != null) { JsonArray membersNewArrayPatch = new
			 * JsonArray(); for (int i = 0; i < membersArrayPatch.size(); i++) { JsonObject
			 * memberPatch = new JsonObject(); memberPatch = (JsonObject)
			 * membersArrayPatch.get(i); memberPatch.remove("origin");
			 * memberPatch.remove("type"); memberPatch.addProperty("type", "User");
			 * membersNewArrayPatch.add(memberPatch); } jsonResponse.remove("members");
			 * jsonResponse.add("members", membersNewArrayPatch); }
			 * jsonResponse.remove("zoneId"); jsonResponse.remove("schemas"); JsonArray
			 * schemasArray2 = new JsonArray();
			 * schemasArray2.add("urn:ietf:params:scim:schemas:core:2.0:Group");
			 * jsonResponse.add("schemas", schemasArray2); response.getWriter().write(new
			 * Gson().toJson(jsonResponse)); } else {
			 * response.setStatus(responseScim.getStatusLine().getStatusCode()); JsonObject
			 * jsonErrResp = (JsonObject) new JsonParser()
			 * .parse(EntityUtils.toString(responseScim.getEntity())); //
			 * response.getWriter().write(new Gson().toJson(jsonErrResp)); } }
			 */
		} catch (Exception e) {
			response.setStatus(500);
			// response.getWriter().append("Error: " + e.getMessage().toString());
			e.printStackTrace();
		}
	}

}
