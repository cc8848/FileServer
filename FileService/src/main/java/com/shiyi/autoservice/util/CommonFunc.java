package com.shiyi.autoservice.util;

import java.io.IOException;
import java.io.StringWriter;

import org.codehaus.jackson.JsonEncoding;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONObject;

public class CommonFunc {

	
	public static JSONObject asJSONObject(String json) throws Exception{
		JSONObject jsonObject = new JSONObject(json);
		return jsonObject;
	}
	
	
	public static  boolean isEmpty(String value){
		return value == null || "".equals(value);
	}

}
