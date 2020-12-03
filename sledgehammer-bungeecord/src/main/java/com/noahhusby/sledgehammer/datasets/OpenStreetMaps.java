/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - OpenStreetMaps.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.datasets;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
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
import java.util.List;

public class OpenStreetMaps {
    private static OpenStreetMaps mInstance = null;

    private final String nominatimAPI = "https://nominatim.openstreetmap.org/reverse.php?osm_type=N&format=json&zoom={zoom}";

    public static OpenStreetMaps getInstance() {
        if(mInstance == null) mInstance = new OpenStreetMaps();
        return mInstance;
    }

    private OpenStreetMaps() {}

    public void init() {
        try {
            if(ConfigHandler.borderTeleportation && ConfigHandler.getInstance().getOfflineBin().exists())
                offlineGeocoder = new ReverseGeocoder(ConfigHandler.getInstance().getOfflineBin());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ReverseGeocoder offlineGeocoder;

    public ServerInfo getServerFromLocation(double lon, double lat) {
        return getServerFromLocation(lon, lat, false);
    }

    public ServerInfo getServerFromLocation(double lon, double lat, boolean offline) {
        Location location;

        if(offline) {
            location = getOfflineLocation(lon, lat);
        } else {
            location = getLocation(lon, lat);
        }

        List<SledgehammerServer> servers = ServerConfig.getInstance().getServers();
        for(SledgehammerServer s : servers) {
            if(s.earthServer) {
                for(Location l : s.locations) {
                    switch (l.detailType) {
                        case city:
                            if(location.city.equals(l.city) &&
                                    (location.state.equals(l.state) ||
                                            location.country.equals(l.country))) {
                                return SledgehammerUtil.getServerFromName(s.name);
                            }
                            break;
                        case county:
                            if(!l.country.equals("")) {
                                if(location.county.equals(l.county) &&
                                        location.state.equals(l.state) &&
                                        location.country.equals(l.country)) {
                                    return SledgehammerUtil.getServerFromName(s.name);
                                }
                            } else {
                                if(location.county.equals(l.county) &&
                                        location.state.equals(l.state)) {
                                    return SledgehammerUtil.getServerFromName(s.name);
                                }
                            }
                            break;
                        case state:
                            if(location.state.equals(l.state) &&
                                    location.country.equals(l.country)) {
                                return SledgehammerUtil.getServerFromName(s.name);
                            }
                            break;
                        case country:
                            if(location.country.equals(l.country)) {
                                return SledgehammerUtil.getServerFromName(s.name);
                            }
                            break;
                    }
                }
            }
        }
        return null;
    }

    public Location getLocation(double lon, double lat) {
        return getLocation(lon, lat, ConfigHandler.zoom);
    }

    public Location getLocation(double lon, double lat, int zoom) {
        try {
            String fullRequest = nominatimAPI.replace("{zoom}", String.valueOf(zoom)) + "&lon="+lon+"&accept-language=en&lat="+lat;

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
                if(state == null && ((String) address.get("territory") != null)) {
                    state = (String) address.get("territory");
                }
                String country = (String) address.get("country");

                return new Location(Location.detail.none, city, county, state, country);
            }
        } catch (IOException | ParseException | NullPointerException e) {
            return null;
        }
    }

    public Location getOfflineLocation(double lon, double lat) {
        String[] data = offlineGeocoder.lookup((float) lon, (float) lat);
        String city = null;
        String county = null;
        String state = null;
        String country = null;
        for(int x = 0; x < data.length; x++) {
            OfflineDataField o = getDataField(data[x]);
            if (o.type.equalsIgnoreCase("city")) {
                city = o.data;
            } else if(o.type.equalsIgnoreCase("county")) {
                county = o.data;
            } else if(o.type.equalsIgnoreCase("state")) {
                state = o.data;
            } else if(o.type.equalsIgnoreCase("country")) {
                country = o.data;
            } else if (o.admin.equals("8") && city == null) {
                city = o.data;
            } else if(o.admin.equals("6") && county == null) {
                county = o.data;
            } else if(o.admin.equals("4") && state == null) {
                state = o.data;
            } else if(o.admin.equals("2") && country == null) {
                country = o.data;
            }
        }

        /*
        for(int x = 0; x < data.length; x++) {
            OfflineDataField o = getDataField(data[x]);
            if (city == null) {
                city = o.data;
            } else if(county == null) {
                county = o.data;
            } else if(state == null) {
                state = o.data;
            } else if(country == null) {
                country = o.data;
            }
        }
         */

        Location l = new Location(Location.detail.none, city, county, state, country);
        return l;
    }

    private static OfflineDataField getDataField(String f) {
        if(f == null) return null;
        String[] data = f.trim().replaceAll(" ", "space").replaceAll("\\s+", ";;")
                .replaceAll("space", " ").trim().split(";;");

        String a = "";
        String b = "";
        String c = "";

        if(data.length > 1) {
            a = data[1];
        }

        if(data.length > 2) {
            b = data[2];
        }

        c = data[data.length-1].trim();

        return new OfflineDataField(a, b, c);
    }
}
