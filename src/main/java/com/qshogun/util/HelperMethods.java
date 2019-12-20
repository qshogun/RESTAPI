package com.qshogun.util;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.io.IOException;

public class HelperMethods {

    public static JsonPath rawToJson(Response response) throws IOException {
        String responseString = response.asString();
        JsonPath jsonPath = new JsonPath(responseString);
        return jsonPath;
    }
}
