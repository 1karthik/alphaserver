package com.infralabs.server;

import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "alphaserver")
public class AlphaServerConfigs {
	private String eurekaservice;

	private HashMap<String, String> target;

	public HashMap<String, String> getTarget() {
		return target;
	}

	public void setTarget(HashMap<String, String> somedata) {
		this.target = somedata;
	}

	public String getEurekaservice() {
		return eurekaservice;
	}

	public void setEurekaservice(String eurekaservice) {
		this.eurekaservice = eurekaservice;
	}
}
