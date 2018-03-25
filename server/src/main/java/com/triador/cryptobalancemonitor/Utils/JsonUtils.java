package com.triador.cryptobalancemonitor.Utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class JsonUtils {
    public static JsonObject convertFileToJSON(String fileName) {

        JsonObject jsonObject = new JsonObject();
        try {

            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(fileName));
            jsonObject = jsonElement.getAsJsonObject();

        } catch (FileNotFoundException ignored) {
        }
        return jsonObject;
    }

    public static JsonObject convertStringToJsonObject(String jsonString) {
        JsonParser jsonParser = new JsonParser();
        if (jsonString.startsWith("{")) {
            return jsonParser.parse(jsonString).getAsJsonObject();
        }
        else if (jsonString.startsWith("[")) {
            return jsonParser.parse(jsonString).getAsJsonArray().get(0).getAsJsonObject();
        }
        else return null;
    }

    public static JsonArray converStringToJsonArray(String jsonString) {
        if (jsonString.startsWith("[")) {
            return new JsonParser().parse(jsonString).getAsJsonArray();
        }
        else return null;
    }
}
