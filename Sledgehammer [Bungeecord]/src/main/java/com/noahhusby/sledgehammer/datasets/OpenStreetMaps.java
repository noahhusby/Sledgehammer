package com.noahhusby.sledgehammer.datasets;


import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class OpenStreetMaps {
    private static OpenStreetMaps mInstance = null;

    private final String nominatimAPI = "https://nominatim.openstreetmap.org/reverse.php?osm_type=N&format=json&zoom=6";

    public static OpenStreetMaps getInstance() {
        if(mInstance == null) mInstance = new OpenStreetMaps();
        return mInstance;
    }

    private OpenStreetMaps() { }

    public String getState(double lon, double lat) {
        try {
            String fullRequest = nominatimAPI + "&lon="+lon+"&accept-language=en&lat="+lat;

            URL url = new URL(fullRequest);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestProperty("User-Agent", "sledgehammer/1.0");
            con.setRequestProperty("Accept", "application/json");

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JSONParser parser = new JSONParser();
                JSONObject geocode = (JSONObject) parser.parse(response.toString());
                JSONObject address = (JSONObject) geocode.get("address");

                return (String) address.get("state");
            }
        } catch (IOException | ParseException | NullPointerException e) {
            return null;
        }
    }
}
