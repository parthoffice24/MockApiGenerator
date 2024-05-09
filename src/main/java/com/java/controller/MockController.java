package com.java.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.java.model.Endpoints;
import com.java.services.FolderEndpointManager;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @author p.subhash.ghavghave
 */
@Controller
public class MockController {

	FolderEndpointManager endpointFileManager = new FolderEndpointManager();

	@PostConstruct
	public void checkEndpointFile() {
		if (endpointFileManager.processJSONFiles()) {
			System.out.println("Mock Api file loaded");
		} else {
			System.out.println("Mock Api file not found");
		}
	}

	@GetMapping("/endpoints")
	public ResponseEntity<String[]> getEndpoints() {
		List<Endpoints> endpointList = endpointFileManager.getEndpoint();
		String[] endpointPaths = new String[endpointList.size()];
		for (int i = 0; i < endpointList.size(); i++) {
			endpointPaths[i] = endpointList.get(i).getPath();
		}
		return ResponseEntity.ok(endpointPaths);
	}

	@GetMapping("/loadfile")
	public ResponseEntity<String> loadEndpointFile() {
		if (endpointFileManager.processJSONFiles())
			return ResponseEntity.ok("File loaded");
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Try again");
	}

	@PostMapping("/clara-bff/**")
	@PutMapping("/clara-bff/**")
	public ResponseEntity<Object> handlePostRequest(HttpServletRequest request, @RequestBody String payload) {
		String requestURI = request.getRequestURI();
		int queryStringIndex = requestURI.indexOf("?");
		if (queryStringIndex != -1) {
			requestURI = requestURI.substring(0, queryStringIndex);
		}
		String[] pathSegments = request.getRequestURI().split("/");
		String path = pathSegments[pathSegments.length - 1];
		System.out.println("Endpoint requested path: /" + path);
		Endpoints matchedEndpoint = null;
		List<Endpoints> fetchedEndpoints = endpointFileManager.getEndpoint();
		for (Endpoints endpoint : fetchedEndpoints) {
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
			System.out.println("Http Status code: " + matchedEndpoint.getStatus().toString());
			System.out.println("Successfully responded for endpoint requested path: /" + path);
			return ResponseEntity.status(matchedEndpoint.getStatus()).body(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Problem with response");
	}

	@GetMapping("/clara-bff/**")
	public ResponseEntity<Object> handleGetRequest(HttpServletRequest request) {
		String requestURI = request.getRequestURI();
		int queryStringIndex = requestURI.indexOf("?");
		if (queryStringIndex != -1) {
			requestURI = requestURI.substring(0, queryStringIndex);
		}
		String[] pathSegments = request.getRequestURI().split("/");
		String path = pathSegments[pathSegments.length - 1];
		System.out.println("Endpoint requested path: /" + path);
		Endpoints matchedEndpoint = null;
		List<Endpoints> fetchedEndpoints = endpointFileManager.getEndpoint();
		for (Endpoints endpoint : fetchedEndpoints) {
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
			System.out.println("Http Status code: " + matchedEndpoint.getStatus().toString());
			System.out.println("Successfully responded for endpoint requested path: /" + path);
			return ResponseEntity.status(matchedEndpoint.getStatus()).body(json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.CONFLICT).body("Problem with response");
	}
}
