package com.infralabs.server.web.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infralabs.server.AlphaServerConfigs;

@RestController
@RequestMapping("/")
public class AgentPuller {
	@Autowired	public  AlphaServerConfigs alphaServerConfig;
	RestTemplate restTemplate= new RestTemplate();
	ParameterizedTypeReference<String> ptr= new ParameterizedTypeReference<String>() {};

@RequestMapping("/names")
	public Map<String,String> callAgents() {
		String retVal="";
		Map<String,String> data = new HashMap<String,String>();
		HttpHeaders headers = new HttpHeaders();
		System.out.println(alphaServerConfig);
//		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImF1dGgiOiJST0xFX0FETUlOLFJPTEVfVVNFUiIsImV4cCI6MTQ4MTMwMzQ3MX0.2KGV7bU4qz9ruhuTy4oCEfX7CXZn3d8Em0tigiBIhhOED4NI8Tdy38p-lubIcs5QgaIFCIz8QZMNB_zuAJ3n5g");

		HttpEntity<String> entity = new HttpEntity<String>(headers);
		retVal= restTemplate.exchange(String.format("http://%s:8761/api/eureka/applications", alphaServerConfig.getEurekaservice()) ,
			    HttpMethod.GET, entity, ptr).getBody();
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> map ;

		// convert JSON string to Map
		try {
			map = mapper.readValue(retVal, new TypeReference<Map<String, Object>>(){});
			ArrayList list= (ArrayList) map.get("applications");
			((LinkedHashMap)((ArrayList) ((LinkedHashMap)list.get(0)).get("instances")).get(0)).get("homePageUrl");
			if(((String) ((LinkedHashMap)list.get(0)).get("name")).equals("ALPHAAGENT")) {
				ArrayList<LinkedHashMap> instances=((ArrayList<LinkedHashMap>) ((LinkedHashMap)list.get(0)).get("instances"));
				if(alphaServerConfig.getTarget().keySet().size()>instances.size()) {
					data.put("error", "One of the agent is down");
				}
				for(LinkedHashMap each : instances) {
					String url = ((String) each.get("homePageUrl")).split("//")[1].split(":")[0];
					String publicip = alphaServerConfig.getTarget().get(url.replace(".", "-"));
					data.put(publicip, restTemplate.exchange( String.format("http://%s:8089/monitor", publicip),
						    HttpMethod.GET, entity, ptr).getBody());
				}
			}
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return data;
		
	}
}
