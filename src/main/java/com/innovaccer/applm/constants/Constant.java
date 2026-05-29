package com.innovaccer.applm.constants;

import java.util.Map;

import org.json.JSONObject;

import com.innovaccer.applm.enums.APICategory;
import com.jayway.restassured.response.Response;

/**
 * 
 * @author i0465
 *
 */

public class Constant {

	public static final String CURRENT_WORKING_DIRECTORY = System.getProperty("user.dir");
	public static String API_BASEURL ="" ;
	public static String FHIR_API_Base_URL="";
	public static String FHIR_API_Root_URL="";
	public static String MEMBER_PORTAL_Base_URL;
	public static String MEMBER_PORTAL_Root_URL;
	public static StringBuilder authorizationToken = null;
	public static String testName;
	public static String STATUS_CODE;
	public static String SegmentID = null;
	public static String FILE_PATH;
	public static String REQUESTBODY = null;
	public static String RESPONSEBODY = null;
	public static Response response = null;
	public static String Query_Param;
	public static String CONTENT_TYPE;
	public static String TestDataPath = System.getProperty("user.dir") + "/src//test/resources//";
	public static Map<APICategory, Object> mapOfResponses = null;
	public static String cookies;
	public static Map<String,Object> runtimeMapOfData;
	public static String createUserAPIBodyString=null;
	public static String enableUserAPIBodyString=null;
	public static String disableUserAPIBodyString=null;
	public static String changeEmailAPIBodyString=null;
	public static String adminChangePsswrdAPIBodyString=null;
	public static String requestPsswrdUserAPIBodyString=null;
	public static String setPermissionAPIBodyString=null;
	public static String TENANT_ID = null;
	public static String CentralTenantId=null;
	public static String PASSWORD = null;
	public static String TEMP_PASSWORD=null;
	public static String RESET_TEMP_PASSWORD=null;
	public static JSONObject tenantPermissionJSONObject = new JSONObject();
	public static String cms_newUser_email="testpramod1@yopmail.com";
	public static String cms_newUser_password="Demoo@123";
	public static String cms_newUser_otp=null;
	public static String client_id=null;
	public static String app_Name=null;
	public static String patientId=null;
	public static String resetlink=null;
    public static String FHIRResponse=null;
	public static String FHIRValidatorcommand=null;
	public static String authorizationTokenIG=null;
	public static int total_count=0;
    public static String authCode=null;
	public static String accessToken=null;
	public static String client_secret=null;
	public static String appReqId=null;
	public static String app_id = null;
	public static String app_url=null;
	public static String app_tenant_id=null;
	public static String appRedirectURL=null;
	public static String multitenant_url=null;
	public static String single_tenant_url=null;
	
	
}
