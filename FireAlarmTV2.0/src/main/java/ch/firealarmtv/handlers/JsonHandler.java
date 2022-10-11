package ch.firealarmtv.handlers;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonHandler {
	
	public static JsonNode getJson(URL url) {
		ObjectMapper mapper = new ObjectMapper();
		
		try {
			return mapper.readTree(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
}
