package com.java.controller;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.model.Endpoints;
import com.java.services.EndpointFileManager;

/**
 * @author p.subhash.ghavghave
 */
@Controller
@RequestMapping("/clara-bff")
public class MockController {

	EndpointFileManager endpointFileManager = new EndpointFileManager();

	@Bean
	public void checkEndpointFile() {
		if (endpointFileManager.loadEndpointsFromFile()) {
			System.out.println("Mock Api file loaded");
		} else {
			System.out.println("Mock Api file not found");
		}
	}

	@PostMapping("/configure")
	public ResponseEntity<String> configureEndpoints(@RequestBody Endpoints endpoint) {
		System.out.println("Configuration Started...");
		System.out.println("Endpoint recieved\nRequest Body:\n" + "Path: " + endpoint.getPath() + "\n" + "Payload: "
				+ endpoint.getPayload() + "\n" + "Response: " + endpoint.getResponse());
		endpointFileManager.addEndpoint(endpoint);
		if (endpointFileManager.saveEndpointsToFile())
			return ResponseEntity.ok("Endpoints configured successfully");
		return ResponseEntity.status(HttpStatus.CONFLICT).body("MockApi File is open in another window");
	}

	@GetMapping("/endpoints")
	public ResponseEntity<List<Endpoints>> getEndpoints() {
		return ResponseEntity.ok(endpointFileManager.getEndpoint());
	}

	@RequestMapping(value = "/{path}", method = RequestMethod.POST)
	public ResponseEntity<Object> handleRequest(@PathVariable("path") String path, @RequestBody String payload) {
		System.out.println("Endpoint requested path: /" + path);
		Endpoints matchedEndpoint = null;
		List<Endpoints> fetchedEndpoints = endpointFileManager.getEndpoint();
		for (Endpoints endpoint : fetchedEndpoints) {
			System.out.println(endpoint.getPath());
			if (endpoint.getPath().equals(path)) {
				matchedEndpoint = endpoint;
				break;
			}
		}
		if (matchedEndpoint == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Endpoint not configured");
		}
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			Object json = objectMapper.readValue(matchedEndpoint.getResponse(), Object.class);
			System.out.println("Successfully responded for endpoint requested path: /" + path);
			return ResponseEntity.ok(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Problem with response");
	}
}
