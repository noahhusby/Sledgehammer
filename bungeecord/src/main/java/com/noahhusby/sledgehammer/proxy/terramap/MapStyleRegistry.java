/*
 * MIT License
 *
 * Copyright 2020-2021 noahhusby
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

package com.noahhusby.sledgehammer.proxy.terramap;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;
import com.noahhusby.sledgehammer.proxy.terramap.network.packets.P2CMapStylePacket;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles loading map styles
 *
 * @author SmylerMC
 */
public class MapStyleRegistry {

    public static final String FILENAME = "terramap_user_styles.json";

    private static File configMapsFile;
    private static Map<String, P2CMapStylePacket> availableMaps = new HashMap<>();

    /**
     * @return the map styles that are available
     */
    public static Map<String, P2CMapStylePacket> getMaps() {
        return availableMaps;
    }

    /**
     * Loads map styles from the config file
     */
    public static void loadFromConfigFile() {
        if (configMapsFile == null) {
            Sledgehammer.logger.warning("Map config file was null! Did not load.");
            return;
        }
        availableMaps = new HashMap<>();
        if (!configMapsFile.exists()) {
            try {
                Sledgehammer.logger.info("Map config file did not exist, creating a blank one.");
                MapStyleFile mapFile = new MapStyleFile(new MapFileMetadata(0, "Add custom map styles here. See an example at styles.terramap.thesmyler.fr (open in your browser, do not add http or https prefix)"));
                GsonBuilder builder = new GsonBuilder();
                builder.setPrettyPrinting();
                Files.asCharSink(configMapsFile, Charset.defaultCharset()).write(builder.create().toJson(mapFile));
            } catch (IOException e) {
                Sledgehammer.logger.warning("Failed to create map style config file! " + e.getLocalizedMessage());
            }
        } else {
            try {
                availableMaps.putAll(loadFromFile(configMapsFile));
            } catch (Exception e) {
                Sledgehammer.logger.warning("Failed to read map style config file! " + e.getLocalizedMessage());
            }
        }
    }

    private static P2CMapStylePacket readFromSaved(String id, SavedMapStyle saved, long version, String comment) {
        return new P2CMapStylePacket(
                id,
                version,
                saved.url,
                saved.name,
                saved.copyright,
                saved.min_zoom,
                saved.max_zoom,
                saved.display_priority,
                saved.allow_on_minimap,
                comment
        );
    }

    private static Map<String, P2CMapStylePacket> loadFromFile(File file) throws IOException {
        String json = String.join("", java.nio.file.Files.readAllLines(file.toPath()));
        return loadFromJson(json);
    }

    private static Map<String, P2CMapStylePacket> loadFromJson(String json) {
        Gson gson = new Gson();
        MapStyleFile savedStyles = gson.fromJson(json, MapStyleFile.class);
        Map<String, P2CMapStylePacket> styles = new HashMap<>();
        for (String id : savedStyles.maps.keySet()) {
            P2CMapStylePacket style = readFromSaved(id, savedStyles.maps.get(id), savedStyles.metadata.version, savedStyles.metadata.comment);
            styles.put(id, style);
        }
        return styles;
    }

    /**
     * Set the map styles config file
     *
     * @param file
     */
    public static void setConfigMapFile(File file) {
        configMapsFile = file;
    }

    static class SavedMapStyle {

        String url;
        Map<String, String> name;
        Map<String, String> copyright;
        int min_zoom;
        int max_zoom;
        int display_priority;
        boolean allow_on_minimap;

    }

    static class MapStyleFile {

        Map<String, SavedMapStyle> maps;
        MapFileMetadata metadata;

        MapStyleFile(MapFileMetadata metadata) {
            this.metadata = metadata;
            this.maps = new HashMap<>();
        }

        MapStyleFile() {
            this(new MapFileMetadata());
        }

    }

    static class MapFileMetadata {

        long version;
        String comment;

        MapFileMetadata(long version, String comment) {
            this.comment = comment;
            this.version = version;
        }

        MapFileMetadata() {
            this(0, "");
        }

    }
}
