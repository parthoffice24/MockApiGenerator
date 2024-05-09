package com.java.model;

import org.springframework.http.HttpStatus;

import lombok.Data;

/**
 * @author p.subhash.ghavghave
 */
@Data
public class Endpoints {
	
	private String path;
	private String payload;
	private HttpStatus status;
	private String response;
}
