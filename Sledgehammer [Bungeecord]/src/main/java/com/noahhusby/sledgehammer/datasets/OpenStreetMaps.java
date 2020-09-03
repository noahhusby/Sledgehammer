package com.noahhusby.sledgehammer.datasets;


import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.util.ProxyUtil;
import net.md_5.bungee.api.config.ServerInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;

public class OpenStreetMaps {
    private static OpenStreetMaps mInstance = null;

    private final String nominatimAPI = "https://nominatim.openstreetmap.org/reverse.php?osm_type=N&format=json&zoom=12";

    public static OpenStreetMaps getInstance() {
        if(mInstance == null) mInstance = new OpenStreetMaps();
        return mInstance;
    }

    private OpenStreetMaps() { }

    public ServerInfo getServerFromLocation(double lon, double lat) {
        Location location = getLocation(lon, lat);

        List<Server> servers = ServerConfig.getInstance().getServers();
        for(Server s : servers) {
            for(Location l : s.locations) {
                switch (l.detailType) {
                    case city:
                        if(location.city.equals(l.city) &&
                                (location.state.equals(l.state) ||
                                location.country.equals(l.country))) {
                            return ProxyUtil.getServerFromName(s.name);
                        }
                        break;
                    case county:
                        if(!l.country.equals("")) {
                            if(location.county.equals(l.county) &&
                                    location.state.equals(l.state) &&
                                    location.country.equals(l.country)) {
                                return ProxyUtil.getServerFromName(s.name);
                            }
                        } else {
                            if(location.county.equals(l.county) &&
                                    location.state.equals(l.state)) {
                                return ProxyUtil.getServerFromName(s.name);
                            }
                        }
                        break;
                    case state:
                        if(location.state.equals(l.state) &&
                                location.country.equals(l.country)) {
                            return ProxyUtil.getServerFromName(s.name);
                        }
                        break;
                    case country:
                        if(location.country.equals(l.country)) {
                            return ProxyUtil.getServerFromName(s.name);
                        }
                        break;
                }
            }
        }
        return null;
    }

    private Location getLocation(double lon, double lat) {
        try {
            String fullRequest = nominatimAPI + "&lon="+lon+"&accept-language=en&lat="+lat;

            URL url = new URL(fullRequest);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestProperty("User-Agent", Constants.PLUGINID+"/"+Constants.VERSION);
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

                String city = (String) address.get("city");
                if(city == null && ((String) address.get("town") != null)) {
                    city = (String) address.get("town");
                }
                String county = (String) address.get("county");
                String state = (String) address.get("state");
                String country = (String) address.get("country");

                return new Location(Location.detail.none, city, county, state, country);
            }
        } catch (IOException | ParseException | NullPointerException e) {
            return null;
        }
    }


}
