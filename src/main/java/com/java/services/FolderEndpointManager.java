package com.java.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;

import com.java.constants.Constants;
import com.java.model.Endpoints;

public class FolderEndpointManager {

	File endpointFolder = new File(Constants.ENDPOINT_FOLDER_PATH);

	private List<Endpoints> endpoint;

	public boolean processJSONFiles() {
		endpoint = new ArrayList<>();
		try {
			if (endpointFolder.exists() && endpointFolder.isDirectory()) {
				File[] files = endpointFolder.listFiles();

				if (files != null) {
					for (File file : files) {
						if (file.isFile() && file.getName().endsWith(".json")) {
							try {
								Endpoints endpoints = new Endpoints();
								String content = new String(Files.readAllBytes(file.toPath()));
								JSONObject jsonObject = new JSONObject(content);
								String path = file.getName().substring(0, file.getName().lastIndexOf('.'));
								for (int i = 0; i < path.length(); i++) {
									path = path.replace('_', '/');
								}
								endpoints.setPayload("");
								endpoints.setResponse(jsonObject.toString());
								String[] parts = path.split("-");
								String statusCode = parts[1];
								endpoints.setStatus(getHttpStatusFromCode(statusCode));
								String endpointName = parts[0];
								endpoints.setPath(endpointName);
								endpoint.add(endpoints);
								System.out.println("Api configured: " + path);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
				return true;
			} else {
				System.out.println("Folder not found or is not a directory.");
			}
		} catch (JSONException e) {
			System.out.println("Folder not Found");
		}
		return false;
	}

	public List<Endpoints> getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(List<Endpoints> endpoint) {
		this.endpoint = endpoint;
	}

	public static HttpStatus getHttpStatusFromCode(String code) {
		// Map to store status codes and their corresponding HttpStatus
		Map<String, HttpStatus> statusMap = new HashMap<>();
		statusMap.put("100", HttpStatus.CONTINUE);
		statusMap.put("101", HttpStatus.SWITCHING_PROTOCOLS);
		statusMap.put("200", HttpStatus.OK);
		statusMap.put("201", HttpStatus.CREATED);
		statusMap.put("202", HttpStatus.ACCEPTED);
		statusMap.put("203", HttpStatus.NON_AUTHORITATIVE_INFORMATION);
		statusMap.put("204", HttpStatus.NO_CONTENT);
		statusMap.put("205", HttpStatus.RESET_CONTENT);
		statusMap.put("206", HttpStatus.PARTIAL_CONTENT);
		statusMap.put("300", HttpStatus.MULTIPLE_CHOICES);
		statusMap.put("301", HttpStatus.MOVED_PERMANENTLY);
		statusMap.put("302", HttpStatus.FOUND);
		statusMap.put("303", HttpStatus.SEE_OTHER);
		statusMap.put("304", HttpStatus.NOT_MODIFIED);
		statusMap.put("305", HttpStatus.USE_PROXY);
		statusMap.put("307", HttpStatus.TEMPORARY_REDIRECT);
		statusMap.put("400", HttpStatus.BAD_REQUEST);
		statusMap.put("401", HttpStatus.UNAUTHORIZED);
		statusMap.put("402", HttpStatus.PAYMENT_REQUIRED);
		statusMap.put("403", HttpStatus.FORBIDDEN);
		statusMap.put("404", HttpStatus.NOT_FOUND);
		statusMap.put("405", HttpStatus.METHOD_NOT_ALLOWED);
		statusMap.put("406", HttpStatus.NOT_ACCEPTABLE);
		statusMap.put("407", HttpStatus.PROXY_AUTHENTICATION_REQUIRED);
		statusMap.put("408", HttpStatus.REQUEST_TIMEOUT);
		statusMap.put("409", HttpStatus.CONFLICT);
		statusMap.put("410", HttpStatus.GONE);
		statusMap.put("411", HttpStatus.LENGTH_REQUIRED);
		statusMap.put("412", HttpStatus.PRECONDITION_FAILED);
		statusMap.put("413", HttpStatus.PAYLOAD_TOO_LARGE);
		statusMap.put("414", HttpStatus.URI_TOO_LONG);
		statusMap.put("415", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
		statusMap.put("416", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
		statusMap.put("417", HttpStatus.EXPECTATION_FAILED);
		statusMap.put("426", HttpStatus.UPGRADE_REQUIRED);
		statusMap.put("428", HttpStatus.PRECONDITION_REQUIRED);
		statusMap.put("429", HttpStatus.TOO_MANY_REQUESTS);
		statusMap.put("431", HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
		statusMap.put("500", HttpStatus.INTERNAL_SERVER_ERROR);
		statusMap.put("501", HttpStatus.NOT_IMPLEMENTED);
		statusMap.put("502", HttpStatus.BAD_GATEWAY);
		statusMap.put("503", HttpStatus.SERVICE_UNAVAILABLE);
		statusMap.put("504", HttpStatus.GATEWAY_TIMEOUT);
		statusMap.put("505", HttpStatus.HTTP_VERSION_NOT_SUPPORTED);
		statusMap.put("511", HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);

		// Get HttpStatus corresponding to the code
		HttpStatus status = statusMap.get(code);
		if (status == null) {
			throw new IllegalArgumentException("Invalid HTTP status code: " + code);
		}
		return status;
	}
}
