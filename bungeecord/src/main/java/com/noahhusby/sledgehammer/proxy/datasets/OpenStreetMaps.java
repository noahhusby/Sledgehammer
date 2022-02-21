/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.proxy.datasets;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import com.noahhusby.lib.data.JsonUtils;
import com.noahhusby.sledgehammer.proxy.Constants;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.config.SledgehammerConfig;
import com.noahhusby.sledgehammer.proxy.modules.Module;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import com.noahhusby.sledgehammer.proxy.servers.SledgehammerServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class OpenStreetMaps implements Module {
    private static OpenStreetMaps instance = null;
    private ReverseGeocoder offlineGeocoder;

    private OpenStreetMaps() {
    }

    public static OpenStreetMaps getInstance() {
        return instance == null ? instance = new OpenStreetMaps() : instance;
    }

    /**
     * Gets estimated data from offline OSM database
     *
     * @param f The parsed offline data field
     * @return {@link OfflineDataField}
     */
    private static OfflineDataField getDataField(String f) {
        if (f == null) {
            return null;
        }
        String[] data = f.trim().replaceAll(" ", "space").replaceAll("\\s+", ";;")
                .replaceAll("space", " ").trim().split(";;");
        String a = "";
        String b = "";
        if (data.length > 1) {
            a = data[1];
        }
        if (data.length > 2) {
            b = data[2];
        }
        String c = data[data.length - 1].trim();
        return new OfflineDataField(a, b, c);
    }

    /**
     * Gets a server from geographical coordinates
     *
     * @param lat Latitude
     * @param lon Longitude
     * @return Returns {@link ServerInfo} if a valid region is found, or null if not
     */
    public ServerInfo getServerFromLocation(double lat, double lon) {
        return getServerFromLocation(lat, lon, SledgehammerConfig.geography.useOfflineMode);
    }

    /**
     * Gets a server from geographical coordinates
     *
     * @param lat     Latitude
     * @param lon     Longitude
     * @param offline True to use offline database
     * @return Returns {@link ServerInfo} if a valid region is found, or null if not
     */
    public ServerInfo getServerFromLocation(double lat, double lon, boolean offline) {
        Location location = offline ? getOfflineLocation(lat, lon) : getLocation(lat, lon);
        if (location == null) {
            return null;
        }
        Map<Location.Detail, ServerInfo> serverInfoMap = Maps.newHashMap();

        for (SledgehammerServer s : ServerHandler.getInstance().getServers().values()) {
            if (!s.isEarthServer()) {
                continue;
            }
            if (s.getLocations() == null || s.getLocations().isEmpty()) {
                continue;
            }
            for (Location l : s.getLocations()) {
                switch (l.detailType) {
                    case city:
                        if (l.compare(location, Location.Detail.city)) {
                            serverInfoMap.put(l.detailType, s.getInfo());
                            continue;
                        }
                        break;
                    case county:
                        if (l.compare(location, Location.Detail.county)) {
                            serverInfoMap.put(l.detailType, s.getInfo());
                            continue;
                        }
                        break;
                    case state:
                        if (l.compare(location, Location.Detail.state)) {
                            serverInfoMap.put(l.detailType, s.getInfo());
                            continue;
                        }
                        break;
                    case country:
                        if (l.compare(location, Location.Detail.country)) {
                            serverInfoMap.put(l.detailType, s.getInfo());
                            continue;
                        }
                        break;
                }
            }
        }

        if (serverInfoMap.get(Location.Detail.city) != null) {
            return serverInfoMap.get(Location.Detail.city);
        }

        if (serverInfoMap.get(Location.Detail.county) != null) {
            return serverInfoMap.get(Location.Detail.county);
        }

        if (serverInfoMap.get(Location.Detail.state) != null) {
            return serverInfoMap.get(Location.Detail.state);
        }

        if (serverInfoMap.get(Location.Detail.country) != null) {
            return serverInfoMap.get(Location.Detail.country);
        }

        return null;
    }

    /**
     * Generates {@link Location} from geographical coordinates
     *
     * @param lat Latitude
     * @param lon Longitude
     * @return {@link Location}
     */
    public Location getLocation(double lat, double lon) {
        return getLocation(lat, lon, SledgehammerConfig.geography.zoom);
    }

    /**
     * Generates {@link Location} from geographical coordinates
     *
     * @param lat  Latitude
     * @param lon  Longitude
     * @param zoom Zoom level
     * @return {@link Location}
     */
    public Location getLocation(double lat, double lon, int zoom) {
        try {
            String fullRequest = Constants.nominatimAPI.replace("{zoom}", String.valueOf(zoom)) + "&lat=" + lat + "&accept-language=en&lon=" + lon;

            URL url = new URL(fullRequest);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", Constants.PLUGINID + "/" + Constants.VERSION);
            con.setRequestProperty("Accept", "application/json");

            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JsonObject geocode = JsonUtils.parseString(response.toString()).getAsJsonObject();
                JsonObject address = geocode.getAsJsonObject("address");

                String city = null;
                if (address.has("city")) {
                    city = address.get("city").getAsString();
                } else if (address.has("town")) {
                    city = address.get("town").getAsString();
                }
                String county = null;
                if (address.has("county")) {
                    county = address.get("county").getAsString();
                }
                String state = null;
                if (address.has("state")) {
                    state = address.get("state").getAsString();
                } else if (address.has("territory")) {
                    state = address.get("territory").getAsString();
                }
                String country = address.get("country").getAsString();

                return new Location(Location.Detail.none, city, county, state, country);
            }
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Get location from geographical coordinates from an offline bin
     *
     * @param lat Latitude
     * @param lon Longitude
     * @return {@link Location}
     */
    public Location getOfflineLocation(double lat, double lon) {
        String[] data = offlineGeocoder.lookup((float) lon, (float) lat);
        String city = null;
        String county = null;
        String state = null;
        String country = null;
        for (String datum : data) {
            OfflineDataField o = getDataField(datum);
            if (o.type.equalsIgnoreCase("city")) {
                city = o.data;
            } else if (o.type.equalsIgnoreCase("county")) {
                county = o.data;
            } else if (o.type.equalsIgnoreCase("state")) {
                state = o.data;
            } else if (o.type.equalsIgnoreCase("country")) {
                country = o.data;
            } else if (o.admin.equals("8") && city == null) {
                city = o.data;
            } else if (o.admin.equals("6") && county == null) {
                county = o.data;
            } else if (o.admin.equals("4") && state == null) {
                state = o.data;
            } else if (o.admin.equals("2") && country == null) {
                country = o.data;
            }
        }

        return new Location(Location.Detail.none, city, county, state, country);
    }

    @Override
    public void onEnable() {
        try {
            if (SledgehammerConfig.geography.useOfflineMode && ConfigHandler.getInstance().getOfflineBin().exists()) {
                offlineGeocoder = new ReverseGeocoder(ConfigHandler.getInstance().getOfflineBin());
            }
        } catch (IOException e) {
            Sledgehammer.logger.warning("Failed to initialize offline geocoder! Disabling offline features.");
            SledgehammerConfig.geography.useOfflineMode = false;
            SledgehammerConfig.geography.borderTeleportation = false;
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        if (offlineGeocoder != null) {
            try {
                offlineGeocoder.close();
                offlineGeocoder = null;
            } catch (IOException ignored) {
            }
        }
    }

    @Override
    public String getModuleName() {
        return "OpenStreetMaps";
    }
}
