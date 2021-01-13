package com.enel.ipscan.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.enel.ipscan.Utility;
import com.enel.ipscan.builder.Builder;
import com.enel.ipscan.bean.ControllerResponse;

import com.sap.cloud.sdk.cloudplatform.connectivity.HttpClientAccessor;
import com.sap.cloud.sdk.cloudplatform.connectivity.DestinationAccessor;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import javax.servlet.http.HttpServletRequest;

public class Controller {
	
	private JsonArray membersNewArray;
	
	/*
	public ControllerResponse getUsersController(HttpServletRequest request){
	
		String param = request.getPathInfo();
		if (param != null) {
			param = param.toString();
			if (param.charAt(0) == '/') {
				String id = param.substring(1);
				HttpClient httpClient = HttpClientAccessor.getHttpClient(DestinationAccessor.getDestination("apiAccess"));
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
					
					// version update
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
						
						String idScim = jsonResponse.get("id").toString().replace("\"", "'");
						String filter = "?$filter=id%20eq%20(guid" + idScim + ")&$format=json";
						String stringUrl = "/UsersRolesAndAttributes" + filter;
							
						HttpClient httpClientCustom = HttpClientAccessor.getHttpClient(DestinationAccessor.getDestination("destinazioneSCIM"));
						HttpGet getScimCustom = new HttpGet(stringUrl);
						HttpResponse scimResponseCustom = httpClientCustom.execute(getScimCustom);
						
						HttpEntity entityScimCustom = scimResponseCustom.getEntity();
						JsonObject jsonResponseCustom = (JsonObject) new JsonParser().parse(EntityUtils.toString(entityScimCustom));
						JsonArray jsonResponseCustomArray = jsonResponseCustom.getAsJsonObject("d").getAsJsonArray("results");

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
						
						ControllerResponse controllerResponse = new ControllerResponse(scimResponse.getStatusLine().getStatusCode(), jsonResponse);
						
						return controllerResponse;
					}
				} else {
					JsonObject jsonErrResp = (JsonObject) new JsonParser()
							.parse(EntityUtils.toString(scimResponse.getEntity()));
					ControllerResponse controllerResponse = new ControllerResponse(scimResponse.getStatusLine().getStatusCode(), jsonErrResp);
					return controllerResponse;
				}
			}
		} else {

			param = request.getQueryString();
			JsonObject jsonQueryResponse = this.queryScim(param, response);

			if (jsonQueryResponse != null) {
				response.getWriter().write(new Gson().toJson(jsonQueryResponse));
			}
		}
	}
	*/

	public JsonObject getSchemaController(String param){
		
		JsonObject responseJson = new JsonObject();
		String singleSchemas;
		if (param != null) {

			if (param.equals("/urn:ietf:params:scim:schemas:extension:UAA:2.0:User")) {
				singleSchemas = param.toString().substring(1);
				
				JsonObject metaJson = Builder.buildMetaJson();
				
				JsonArray arraySchemas = Builder.buildArraySchemas();
				
				JsonArray arrayAttr = Builder.buildArrayAttr();
				
				responseJson = Builder.buildResponseJson(arraySchemas, singleSchemas, metaJson, arrayAttr);
				
			} else if (param.toString().equals("/urn:ietf:params:scim:schemas:core:2.0:Group")) {
				responseJson = (JsonObject) new JsonParser().parse(
						"{       \"id\": \"urn:ietf:params:scim:schemas:core:2.0:Group\",       \"name\": \"Group\",       \"description\": \"Group\",       \"attributes\": [         {           \"name\": \"displayName\",           \"type\": \"string\",           \"multiValued\": false,           \"description\": \"A human-readable name for the Group. REQUIRED.\",           \"required\": false,           \"caseExact\": false,           \"mutability\": \"readWrite\",           \"returned\": \"default\",           \"uniqueness\": \"none\"         },         {           \"name\": \"members\",           \"type\": \"complex\",           \"multiValued\": true,           \"description\": \"A list of members of the Group.\",           \"required\": false,           \"subAttributes\": [             {                \"name\":  \"value\",                \"type\":  \"string\",                \"multiValued\": false,                \"description\":  \"Identifier of the member of this Group.\",                \"required\": false,                \"caseExact\": false,                \"mutability\":  \"immutable\",                \"returned\":  \"default\",                \"uniqueness\":  \"none \"             },             {                \"name\": \"$ref\",                \"type\":  \"reference\",                \"referenceTypes\": [                  \"User\",                  \"Group \"               ],                \"multiValued\": false,                \"description\":  \"The URI corresponding to a SCIM resource that is a member of this Group.\",                \"required\": false,                \"caseExact\": false,                \"mutability\":  \"immutable\",                \"returned\":  \"default\",                \"uniqueness\":  \"none \"             },             {                \"name\":  \"type\",                \"type\":  \"string\",                \"multiValued\": false,                \"description\":  \"A label indicating the type of resource, e.g., 'User' or 'Group'.\",                \"required\": false,                \"caseExact\": false,                \"canonicalValues\": [                  \"User\",                  \"Group \"               ],                \"mutability\":  \"immutable\",                \"returned\":  \"default\",                \"uniqueness\":  \"none \"             }           ],            \"mutability\":  \"readWrite\",            \"returned\":  \"default \"         }       ],        \"meta\": {          \"resourceType\":  \"Schema\",          \"location\":  \"DA INSERIRE \"       }     }");
			} else if (param.toString().equals("/urn:ietf:params:scim:schemas:core:2.0:User")) {
				responseJson = (JsonObject) new JsonParser().parse(
						"{ \"id\": \"urn:ietf:params:scim:schemas:core:2.0:User\", \"name\": \"User\", \"description\": \"User Account\", \"attributes\": [ { \"name\": \"userName\", \"type\": \"string\", \"multiValued\": false, \"description\": \"Unique identifier for the User, typically used by the user to directly authenticate to the service provider. Each User MUST include a non-empty userName value.  This identifier MUST be unique across the service provider's entire set of Users.REQUIRED.\", \"required\": true, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"server\" }, { \"name\": \"name\", \"type\": \"complex\", \"multiValued\": false, \"description\": \"The components of the user's real name. Providers MAY return just the full name as a single string in the formatted sub-attribute, or they MAY return just the individual component attributes using the other sub-attributes, or they MAY return both.  If both variants are returned, they SHOULD be describing the same name, with the formatted name indicating how the component attributes should be combined.\", \"required\": false, \"subAttributes\": [ { \"name\": \"formatted\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The full name, including all middle names, titles, and suffixes as appropriate, formatted for display (e.g., 'Ms. Barbara J Jensen, III').\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"familyName\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The family name of the User, or last name in most Western languages (e.g., 'Jensen' given the full name 'Ms. Barbara J Jensen, III').\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"givenName\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The given name of the User, or first name in most Western languages (e.g., 'Barbara' given the full name 'Ms. Barbara J Jensen, III').\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"middleName\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The middle name(s) of the User (e.g., 'Jane' given the full name 'Ms. Barbara J Jensen, III').\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"honorificPrefix\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The honorific prefix(es) of the User, or title in most Western languages (e.g., 'Ms.' given the full name 'Ms. Barbara J Jensen, III').\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"honorificSuffix\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The honorific suffix(es) of the User, or suffix in most Western languages (e.g., 'III' given the full name 'Ms. Barbara J Jensen, III').\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" } ], \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"displayName\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The name of the User, suitable for display to end-users.  The name SHOULD be the full name of the User being described, if known.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"nickName\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The casual way to address the user in real life, e.g., 'Bob' or 'Bobby' instead of 'Robert'.  This attribute SHOULD NOT be used to represent a User's username (e.g., 'bjensen' or 'mpepperidge').\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"profileUrl\", \"type\": \"reference\", \"referenceTypes\": [ \"external\" ], \"multiValued\": false, \"description\": \"\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"title\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The user's title\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"userType\", \"type\": \"string\", \"multiValued\": false, \"description\": \"Used to identify the relationship between the organization and the user.  Typical values used might be 'Contractor', 'Employee', 'Intern', 'Temp', 'External', and 'Unknown', but any value may be used.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"preferredLanguage\", \"type\": \"string\", \"multiValued\": false, \"description\": \"Indicates the User's preferred written or spoken language.  Generally used for selecting a localized user interface; e.g., 'en_US' specifies the language English and country US.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"locale\", \"type\": \"string\", \"multiValued\": false, \"description\": \"Used to indicate the User's default location for purposes of localizing items such as currency, date time format, or numerical representations.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"timezone\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The User's time zone in the 'Olson' time zone database format, e.g., 'America/Los_Angeles'.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"active\", \"type\": \"boolean\", \"multiValued\": false, \"description\": \"A Boolean value indicating the User's administrative status.\", \"required\": false, \"mutability\": \"readWrite\", \"returned\": \"default\" }, { \"name\": \"password\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The User's cleartext password.  This attribute is intended to be used as a means to specify an initial password when creating a new User or to reset an existing User's password.\", \"required\": false, \"caseExact\": false, \"mutability\": \"writeOnly\", \"returned\": \"never\", \"uniqueness\": \"none\" }, { \"name\": \"emails\", \"type\": \"complex\", \"multiValued\": true, \"description\": \"Email addresses for the user.  The value SHOULD be canonicalized by the service provider, e.g., 'bjensen@example.com' instead of 'bjensen@EXAMPLE.COM'. Canonical type values of 'work', 'home', and 'other'.\", \"required\": false, \"subAttributes\": [ { \"name\": \"value\", \"type\": \"string\", \"multiValued\": false, \"description\": \"Email addresses for the user.  The value SHOULD be canonicalized by the service provider, e.g., 'bjensen@example.com' instead of 'bjensen@EXAMPLE.COM'. Canonical type values of 'work', 'home', and 'other'.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"display\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A human-readable name, primarily used for display purposes.  READ-ONLY.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"type\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A label indicating the attribute's function, e.g., 'work' or 'home'.\", \"required\": false, \"caseExact\": false, \"canonicalValues\": [ \"work\", \"home\", \"other\" ], \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"primary\", \"type\": \"boolean\", \"multiValued\": false, \"description\": \"A Boolean value indicating the 'primary' or preferred attribute value for this attribute, e.g., the preferred mailing address or primary email address.  The primary attribute value 'true' MUST appear no more than once.\", \"required\": false, \"mutability\": \"readWrite\", \"returned\": \"default\" } ], \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"phoneNumbers\", \"type\": \"complex\", \"multiValued\": true, \"description\": \"Phone numbers for the User.  The value SHOULD be canonicalized by the service provider according to the format specified in RFC 3966, e.g., 'tel:+1-201-555-0123'. Canonical type values of 'work', 'home', 'mobile', 'fax', 'pager',  and 'other'.\", \"required\": false, \"subAttributes\": [ { \"name\": \"value\", \"type\": \"string\", \"multiValued\": false, \"description\": \"Phone number of the User.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"display\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A human-readable name, primarily used for display purposes.  READ-ONLY.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"type\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A label indicating the attribute's function, e.g., 'work', 'home', 'mobile'.\", \"required\": false, \"caseExact\": false, \"canonicalValues\": [ \"work\", \"home\", \"mobile\", \"fax\", \"pager\", \"other\" ], \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"primary\", \"type\": \"boolean\", \"multiValued\": false, \"description\": \"A Boolean value indicating the 'primary' or preferred attribute value for this attribute, e.g., the preferred phone number or primary phone number.  The primary attribute value 'true' MUST appear no more than once.\", \"required\": false, \"mutability\": \"readWrite\", \"returned\": \"default\" } ], \"mutability\": \"readWrite\", \"returned\": \"default\" }, { \"name\": \"ims\", \"type\": \"complex\", \"multiValued\": true, \"description\": \"Instant messaging addresses for the User.\", \"required\": false, \"subAttributes\": [ { \"name\": \"value\", \"type\": \"string\", \"multiValued\": false, \"description\": \"Instant messaging address for the User.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"display\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A human-readable name, primarily used for display purposes.  READ-ONLY.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"type\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A label indicating the attribute's function, e.g., 'aim', 'gtalk', 'xmpp'.\", \"required\": false, \"caseExact\": false, \"canonicalValues\": [ \"aim\", \"gtalk\", \"icq\", \"xmpp\", \"msn\", \"skype\", \"qq\", \"yahoo\" ], \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"primary\", \"type\": \"boolean\", \"multiValued\": false, \"description\": \"A Boolean value indicating the 'primary' or preferred attribute value for this attribute, e.g., the preferred messenger or primary messenger.  The primary attribute value 'true' MUST appear no more than once.\", \"required\": false, \"mutability\": \"readWrite\", \"returned\": \"default\" } ], \"mutability\": \"readWrite\", \"returned\": \"default\" }, { \"name\": \"photos\", \"type\": \"complex\", \"multiValued\": true, \"description\": \"URLs of photos of the User.\", \"required\": false, \"subAttributes\": [ { \"name\": \"value\", \"type\": \"reference\", \"referenceTypes\": [ \"external\" ], \"multiValued\": false, \"description\": \"URL of a photo of the User.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"display\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A human-readable name, primarily used for display purposes.  READ-ONLY.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"type\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A label indicating the attribute's function, i.e., 'photo' or 'thumbnail'.\", \"required\": false, \"caseExact\": false, \"canonicalValues\": [ \"photo\", \"thumbnail\" ], \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"primary\", \"type\": \"boolean\", \"multiValued\": false, \"description\": \"A Boolean value indicating the 'primary' or preferred attribute value for this attribute, e.g., the preferred photo or thumbnail.  The primary attribute value 'true' MUST appear no more than once.\", \"required\": false, \"mutability\": \"readWrite\", \"returned\": \"default\" } ], \"mutability\": \"readWrite\", \"returned\": \"default\" }, { \"name\": \"addresses\", \"type\": \"complex\", \"multiValued\": true, \"description\": \"A physical mailing address for this User. Canonical type values of 'work', 'home', and 'other'.  This attribute is a complex type with the following sub-attributes.\", \"required\": false, \"subAttributes\": [ { \"name\": \"formatted\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The full mailing address, formatted for display or use with a mailing label.  This attribute MAY contain newlines.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"streetAddress\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The full street address component, which may include house number, street name, P.O. box, and multi-line extended street address information.  This attribute MAY contain newlines.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"locality\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The city or locality component.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"region\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The state or region component.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"postalCode\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The zip code or postal code component.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"country\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The country name component.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"type\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A label indicating the attribute's function, e.g., 'work' or 'home'.\", \"required\": false, \"caseExact\": false, \"canonicalValues\": [ \"work\", \"home\", \"other\" ], \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" } ], \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"groups\", \"type\": \"complex\", \"multiValued\": true, \"description\": \"A list of groups to which the user belongs, either through direct membership, through nested groups, or dynamically calculated.\", \"required\": false, \"subAttributes\": [ { \"name\": \"value\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The identifier of the User's group.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readOnly\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"$ref\", \"type\": \"reference\", \"referenceTypes\": [ \"User\", \"Group\" ], \"multiValued\": false, \"description\": \"The URI of the corresponding 'Group' resource to which the user belongs.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readOnly\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"display\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A human-readable name, primarily used for display purposes.  READ-ONLY.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readOnly\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"type\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A label indicating the attribute's function, e.g., 'direct' or 'indirect'.\", \"required\": false, \"caseExact\": false, \"canonicalValues\": [ \"direct\", \"indirect\" ], \"mutability\": \"readOnly\", \"returned\": \"default\", \"uniqueness\": \"none\" } ], \"mutability\": \"readOnly\", \"returned\": \"default\" }, { \"name\": \"entitlements\", \"type\": \"complex\", \"multiValued\": true, \"description\": \"A list of entitlements for the User that represent a thing the User has.\", \"required\": false, \"subAttributes\": [ { \"name\": \"value\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The value of an entitlement.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"display\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A human-readable name, primarily used for display purposes.  READ-ONLY.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"type\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A label indicating the attribute's function.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"primary\", \"type\": \"boolean\", \"multiValued\": false, \"description\": \"A Boolean value indicating the 'primary' or preferred attribute value for this attribute.  The primary attribute value 'true' MUST appear no more than once.\", \"required\": false, \"mutability\": \"readWrite\", \"returned\": \"default\" } ], \"mutability\": \"readWrite\", \"returned\": \"default\" }, { \"name\": \"roles\", \"type\": \"complex\", \"multiValued\": true, \"description\": \"A list of roles for the User that collectively represent who the User is, e.g., 'Student', 'Faculty'.\", \"required\": false, \"subAttributes\": [ { \"name\": \"value\", \"type\": \"string\", \"multiValued\": false, \"description\": \"The value of a role.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"display\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A human-readable name, primarily used for display purposes.  READ-ONLY.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"type\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A label indicating the attribute's function.\", \"required\": false, \"caseExact\": false, \"canonicalValues\": [], \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"primary\", \"type\": \"boolean\", \"multiValued\": false, \"description\": \"A Boolean value indicating the 'primary' or preferred attribute value for this attribute.  The primary attribute value 'true' MUST appear no more than once.\", \"required\": false, \"mutability\": \"readWrite\", \"returned\": \"default\" } ], \"mutability\": \"readWrite\", \"returned\": \"default\" }, { \"name\": \"x509Certificates\", \"type\": \"complex\", \"multiValued\": true, \"description\": \"A list of certificates issued to the User.\", \"required\": false, \"caseExact\": false, \"subAttributes\": [ { \"name\": \"value\", \"type\": \"binary\", \"multiValued\": false, \"description\": \"The value of an X.509 certificate.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"display\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A human-readable name, primarily used for display purposes.  READ-ONLY.\", \"required\": false, \"caseExact\": false, \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"type\", \"type\": \"string\", \"multiValued\": false, \"description\": \"A label indicating the attribute's function.\", \"required\": false, \"caseExact\": false, \"canonicalValues\": [], \"mutability\": \"readWrite\", \"returned\": \"default\", \"uniqueness\": \"none\" }, { \"name\": \"primary\", \"type\": \"boolean\", \"multiValued\": false, \"description\": \"A Boolean value indicating the 'primary' or preferred attribute value for this attribute.  The primary attribute value 'true' MUST appear no more than once.\", \"required\": false, \"mutability\": \"readWrite\", \"returned\": \"default\" } ], \"mutability\": \"readWrite\", \"returned\": \"default\" } ], \"meta\": { \"resourceType\": \"Schema\", \"location\": \"DA INSERIRE\" } }");
			}
			return responseJson;

		} else {
			// get lista schemas
			JsonArray schemasArray = new JsonArray();
			schemasArray.add("urn:ietf:params:scim:api:messages:2.0:ListResponse");
			
			JsonObject metaJson2 = Builder.buildMetaJson();
			
			JsonArray arraySchemas = Builder.buildArraySchemas();
			
			JsonArray arrayAttr = Builder.buildArrayAttr();

			JsonArray resourceArray = Builder.buildResourceArray(arraySchemas, arrayAttr);
			
			responseJson.addProperty("totalResults", "3");
			responseJson.add("schemas", schemasArray);
			responseJson.add("Resources", resourceArray);
		}
		return responseJson;
		
	}

}
