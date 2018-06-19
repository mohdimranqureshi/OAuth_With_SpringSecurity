package com.security.oauth.resttemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.client.RestTemplate;

import com.security.oauth.model.AuthTokenInfo;

public class RestClient {

	public static final String Rest_Service_URI = "http://localhost:2003/oAuthSpringSecurity";
	
	public static final String Auth_Service_URI = "http://localhost:2003/oAuthSpringSecurity/oauth/token";
	
	public static final String Password_Grant = "?grant_type=password&username=imran&password=imran";
	
	public static final String Access_Token = "?access_token=";
	
	
	private static HttpHeaders getHttpHeader(){
		
		HttpHeaders httpHeader = new HttpHeaders();
		httpHeader.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return httpHeader;
	}
	
	private static HttpHeaders getHeaderWithCredential(){
		
		String credentials = "my-trusted-client:secret";
		String base64Credential = new String(Base64.encode(credentials.getBytes()));
		
		HttpHeaders headers = getHttpHeader();
		headers.add("Authorization", "Basic " + base64Credential);
		return headers;
	}
	
	@SuppressWarnings("unchecked")
	private static AuthTokenInfo sendTokenRequest(){
		
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(getHeaderWithCredential());
		ResponseEntity<Object> response = restTemplate.exchange(Auth_Service_URI+Password_Grant, HttpMethod.POST,request,Object.class);
		
		LinkedHashMap<String, Object> tokenResponse = (LinkedHashMap<String, Object>) response.getBody();
		AuthTokenInfo oauthTokenInfo = new AuthTokenInfo();
		
		if(tokenResponse != null){
			oauthTokenInfo.setAccess_token((String)(tokenResponse.get("access_token")));
			oauthTokenInfo.setExpires_in((int)(tokenResponse.get("expires_in")));
			oauthTokenInfo.setRefresh_token((String)(tokenResponse.get("refresh_token")));
			oauthTokenInfo.setScope((String)(tokenResponse.get("scope")));
			oauthTokenInfo.setToken_type((String)(tokenResponse.get("token_type")));
		}else{
			System.out.println("No User Exist");
		}
		
		return oauthTokenInfo;
	}
	
	public static void getAllUsers(AuthTokenInfo authTokenInfo){
		
	
		RestTemplate restTemplate = new RestTemplate();
		HttpEntity<String> request = new HttpEntity<String>(getHttpHeader());
		
		ResponseEntity<List> response = restTemplate.exchange(Rest_Service_URI+"/user/"+Access_Token+authTokenInfo.getAccess_token(), HttpMethod.GET
											,request,List.class);
		List<LinkedHashMap<String, Object>> userMap = (List<LinkedHashMap<String, Object>>)response.getBody();
		
		 if(userMap!=null){
	            for(LinkedHashMap<String, Object> map : userMap){
	                System.out.println("User : id="+map.get("id")+", Name="+map.get("name")+", Age="+map.get("age")+", Salary="+map.get("salary"));;
	            }
	        }else{
	            System.out.println("No user exist----------");
	        }
	}
	
	public static void main(String[] args){
		
		AuthTokenInfo tokenInfo = sendTokenRequest();
		getAllUsers(tokenInfo);
	}
}
