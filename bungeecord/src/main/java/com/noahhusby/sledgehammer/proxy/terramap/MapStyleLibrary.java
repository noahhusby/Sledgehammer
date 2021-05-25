package com.noahhusby.sledgehammer.proxy.terramap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.noahhusby.sledgehammer.proxy.Sledgehammer;

/**
 * Handles loading map styles
 *
 * @author SmylerMC
 */
//TODO Better handling so errors are visible in /reloadmapstyles
public class MapStyleLibrary {

    public static final String FILENAME = "terramap_user_styles.json";

    private static File configMapsFile;
    private static Map<String, MapStyle> availableMaps = new HashMap<>();

    /**
     * @return the map styles that are available
     */
    public static Map<String, MapStyle> getMaps() {
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
                Files.write(configMapsFile.toPath(), builder.create().toJson(mapFile).getBytes(Charset.defaultCharset()));
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

    private static Map<String, MapStyle> loadFromFile(File file) throws IOException {
        String json = String.join("", Files.readAllLines(file.toPath()));
        return loadFromJson(json);
    }

    private static Map<String, MapStyle> loadFromJson(String json) {
        Gson gson = new Gson();
        MapStyleFile savedStyles = gson.fromJson(json, MapStyleFile.class);
        Map<String, MapStyle> styles = new HashMap<>();
        for (String id : savedStyles.maps.keySet()) {
            MapStyle style = readFromSaved(id, savedStyles.maps.get(id), savedStyles.metadata.version, savedStyles.metadata.comment);
            styles.put(id, style);
        }
        return styles;
    }
    
    private static MapStyle readFromSaved(String id, SavedMapStyle style, long version, String comment) {
        String[] patterns = style.urls;
        if(patterns == null || patterns.length <= 0) {
            if(style.url != null) {
                // This is a legacy source, it only has one url
                patterns = new String[] {style.url};
            } else throw new IllegalArgumentException("Could not find any valid url for map style " + id + "v" + version);
        }
        return new MapStyle(
                id,
                patterns,
                version,
                comment,
                style.name,
                style.copyright,
                style.min_zoom,
                style.max_zoom,
                style.display_priority,
                style.allow_on_minimap,
                style.max_concurrent_requests,
                style.debug);
    }

    /**
     * Set the map styles config file
     *
     * @param file
     */
    public static void setConfigMapFile(File file) {
        configMapsFile = file;
    }

    private static class SavedMapStyle {

        String url; // Used by legacy versions
        String[] urls;
        Map<String, String> name;
        Map<String, String> copyright;
        int min_zoom;
        int max_zoom;
        int display_priority;
        boolean allow_on_minimap;
        int max_concurrent_requests = 2;
        boolean debug;

    }
    
    public static class MapStyle {
        
        public final String id;
        public final String[] urls;
        public final long version;
        public final String comment;
        public final Map<String, String> name;
        public final Map<String, String> copyright;
        public final int minZoom;
        public final int maxZoom;
        public final int displayPriority;
        public final boolean allowOnMinimap;
        public final int maxConcurrentRequests ;
        public final boolean debug;
        
        public MapStyle(String id, String[] urls, long version, String comment,
                Map<String, String> name, Map<String, String> copyright, int min_zoom, int max_zoom,
                int display_priority, boolean allow_on_minimap, int maxConcurentRequests, boolean debug) {
            Preconditions.checkArgument(urls.length > 0, "At least one url pattern needed");
            Preconditions.checkArgument(min_zoom >= 0, "Zoom level must be at least 0");
            Preconditions.checkArgument(max_zoom >= 0 && max_zoom <= 25, "Zoom level must be at most 25");
            Preconditions.checkArgument(!Strings.isNullOrEmpty(id), "A valid map id needs to be provided");
            Preconditions.checkArgument(name != null, "Valid map names needs to be provided");
            Preconditions.checkArgument(copyright != null, "Valid map coprights needs to be provided");
            Preconditions.checkArgument(version >= 0, "Map version number must be positive");
            Preconditions.checkArgument(comment != null, "A valid map comment needs to be provided");
            Preconditions.checkArgument(maxConcurentRequests > 0 ,"Max concurent downloads must be at least 1");
            this.id = id;
            this.urls = urls;
            this.version = version;
            this.comment = comment;
            this.name = name;
            this.copyright = copyright;
            this.minZoom = min_zoom;
            this.maxZoom = max_zoom;
            this.displayPriority = display_priority;
            this.allowOnMinimap = allow_on_minimap;
            this.maxConcurrentRequests = maxConcurentRequests;
            this.debug = debug;
        }

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
