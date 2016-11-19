package com.infralabs.server.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/")
public class AgentPuller {

	RestTemplate restTemplate= new RestTemplate();
	ParameterizedTypeReference<String> ptr
    = new ParameterizedTypeReference<String>() {
};

@RequestMapping("/names")
	public String callAgents() {
		String retVal="";
		HttpHeaders headers = new HttpHeaders();
//		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTQ4MTMwMzQ3MX0.2KGV7bU4qz9ruhuTy4oCEfX7CXZn3d8Em0tigiBIhhOED4NI8Tdy38p-lubIcs5QgaIFCIz8QZMNB_zuAJ3n5g");

		HttpEntity<String> entity = new HttpEntity<String>(headers);
		retVal= restTemplate.exchange( "http://localhost:8761/api/eureka/applications",
			    HttpMethod.GET, entity, ptr).getBody();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map ;

		// convert JSON string to Map
		try {
			map = mapper.readValue(retVal, new TypeReference<Map<String, Object>>(){});
			 map.get("applications");
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return retVal;
		
	}
}
