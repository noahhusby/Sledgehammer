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

package com.noahhusby.sledgehammer.proxy.datasets;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.noahhusby.lib.data.JsonUtils;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.config.ServerHandler;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class OpenStreetMaps {
    private static OpenStreetMaps instance = null;

    public static OpenStreetMaps getInstance() {
        return instance == null ? instance = new OpenStreetMaps() : instance;
    }

    private OpenStreetMaps() {}

    public void init() {
        try {
            if(ConfigHandler.useOfflineMode && ConfigHandler.getInstance().getOfflineBin().exists())
                offlineGeocoder = new ReverseGeocoder(ConfigHandler.getInstance().getOfflineBin());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ReverseGeocoder offlineGeocoder;

    /**
     * Gets a server from geographical coordinates
     * @param lon Longitude
     * @param lat Latitude
     * @return Returns {@link ServerInfo} if a valid region is found, or null if not
     */
    public ServerInfo getServerFromLocation(double lon, double lat) {
        return getServerFromLocation(lon, lat, ConfigHandler.useOfflineMode);
    }

    /**
     * Gets a server from geographical coordinates
     * @param lon Longitude
     * @param lat Latitude
     * @param offline True to use offline database
     * @return Returns {@link ServerInfo} if a valid region is found, or null if not
     */
    public ServerInfo getServerFromLocation(double lon, double lat, boolean offline) {
        Location location = offline ? getOfflineLocation(lon, lat) : getLocation(lon, lat);
        if(location == null) return null;
        Map<Location.Detail, ServerInfo> serverInfoMap = Maps.newHashMap();

        for (SledgehammerServer s : ServerHandler.getInstance().getServers()) {
            if (!s.isEarthServer()) continue;
            if(s.getLocations() == null || s.getLocations().isEmpty()) continue;
            for (Location l : s.getLocations()) {
                switch (l.detailType) {
                    case city:
                        if(l.compare(location, Location.Detail.city)) {
                            serverInfoMap.put(l.detailType, s.getServerInfo());
                            continue;
                        }
                        break;
                    case county:
                        if(l.compare(location, Location.Detail.county)) {
                            serverInfoMap.put(l.detailType, s.getServerInfo());
                            continue;
                        }
                        break;
                    case state:
                        if(l.compare(location, Location.Detail.state)) {
                            serverInfoMap.put(l.detailType, s.getServerInfo());
                            continue;
                        }
                        break;
                    case country:
                        if(l.compare(location, Location.Detail.country)) {
                            serverInfoMap.put(l.detailType, s.getServerInfo());
                            continue;
                        }
                        break;
                }
            }
        }

        if (serverInfoMap.get(Location.Detail.city) != null)
            return serverInfoMap.get(Location.Detail.city);

        if (serverInfoMap.get(Location.Detail.county) != null)
            return serverInfoMap.get(Location.Detail.county);

        if (serverInfoMap.get(Location.Detail.state) != null)
            return serverInfoMap.get(Location.Detail.state);

        if (serverInfoMap.get(Location.Detail.country) != null)
            return serverInfoMap.get(Location.Detail.country);

        return null;
    }

    /**
     * Generates {@link Location} from geographical coordinates
     * @param lon Longitude
     * @param lat Latitude
     * @return {@link Location}
     */
    public Location getLocation(double lon, double lat) {
        return getLocation(lon, lat, ConfigHandler.zoom);
    }

    /**
     * Generates {@link Location} from geographical coordinates
     * @param lon Longitude
     * @param lat Latitude
     * @param zoom Zoom level
     * @return {@link Location}
     */
    public Location getLocation(double lon, double lat, int zoom) {
        try {
            String fullRequest = Constants.nominatimAPI.replace("{zoom}", String.valueOf(zoom)) + "&lon="+lon+"&accept-language=en&lat="+lat;

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

                JsonObject geocode = JsonUtils.parseString(response.toString()).getAsJsonObject();
                JsonObject address = geocode.getAsJsonObject("address");

                String city = address.get("city").getAsString();
                if(city == null && (address.get("town") != null)) {
                    city = address.get("town").getAsString();
                }
                String county = address.get("county").getAsString();
                String state = address.get("state").getAsString();
                if(state == null && (address.get("territory") != null)) {
                    state = address.get("territory").getAsString();
                }
                String country = address.get("country").getAsString();

                return new Location(Location.Detail.none, city, county, state, country);
            }
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Get location from geographical coordinates from an offline bin
     * @param lon Longitude
     * @param lat Latitude
     * @return {@link Location}
     */
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

        Location l = new Location(Location.Detail.none, city, county, state, country);
        return l;
    }

    /**
     * Gets estimated data from offline OSM database
     * @param f The parsed offline data field
     * @return {@link OfflineDataField}
     */
    private static OfflineDataField getDataField(String f) {
        if(f == null) return null;
        String[] data = f.trim().replaceAll(" ", "space").replaceAll("\\s+", ";;")
                .replaceAll("space", " ").trim().split(";;");

        String a = "";
        String b = "";
        String c = "";

        if(data.length > 1) a = data[1];
        if(data.length > 2) b = data[2];

        c = data[data.length-1].trim();

        return new OfflineDataField(a, b, c);
    }
}
