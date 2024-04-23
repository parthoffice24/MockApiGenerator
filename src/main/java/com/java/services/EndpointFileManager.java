package com.java.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.java.model.Endpoints;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class EndpointFileManager {
	private List<Endpoints> endpoint = new ArrayList<>();
    private final ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private final File file = new File(System.getProperty("user.home")+File.separator + "Downloads"+File.separator+"mockapi.json");

    public boolean saveEndpointsToFile() {
        try {
            objectMapper.writeValue(file, endpoint);
            System.out.println("Endpoints saved to file: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loadEndpointsFromFile() {
        try {
            endpoint = objectMapper.readValue(file, objectMapper.getTypeFactory().constructCollectionType(List.class, Endpoints.class));
            System.out.println("Endpoints loaded from file: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
        }
        return false;
    }

    public List<Endpoints> getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(List<Endpoints> endpoint) {
        this.endpoint = endpoint;
    }

    public void addEndpoint(Endpoints endpoint) {
        this.endpoint.add(endpoint);
    }

    public void removeEndpoint(Endpoints endpoint) {
        this.endpoint.remove(endpoint);
    }
}
