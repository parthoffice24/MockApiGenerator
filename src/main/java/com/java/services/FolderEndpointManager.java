package com.java.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

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
								endpoints.setPath(path);
								endpoints.setPayload("");
								endpoints.setResponse(jsonObject.toString());
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
}
