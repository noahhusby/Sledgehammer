package com.noahhusby.sledgehammer.addons.terramap;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.config.ConfigHandler;

/**
 * 
 * @author SmylerMC
 *
 */
public class PlayerDisplayPreferences {

	public static final String FILENAME = "terramap_display_preferences.json";

	private static File file = null;
	private static Preferences preferences = new Preferences();

	private static boolean loggedDebugError = false;
	private static long lastNullSaveTime = Long.MIN_VALUE;

	public static boolean shouldDisplayPlayer(UUID uuid) {
		try {
			synchronized(preferences) {
				return preferences.players.containsKey(uuid) ? preferences.players.get(uuid).display : ConfigHandler.terramapPlayersDisplayDefault;
			}
		} catch(Exception e) {
			if(!loggedDebugError) {
				Sledgehammer.logger.warning("Failed to get player display preferences. This error will only be displayed once.");
				e.printStackTrace();
				loggedDebugError = true;
			}
			return ConfigHandler.terramapPlayersDisplayDefault;
		}
	}

	public static void setShouldDisplayPlayer(UUID uuid, boolean yesNo) {
		try {
			synchronized(preferences) {
				PlayerPreferences pp = preferences.players.getOrDefault(uuid, new PlayerPreferences());
				pp.display = yesNo;
				if(!preferences.players.containsKey(uuid)) preferences.players.put(uuid, pp);
				save();
			}
		} catch(Exception e) {
			Sledgehammer.logger.warning("Failed to set player display preferences! See stack trace:");
			e.printStackTrace();
		}
	}

	public static void save() {
		if(file == null) {
			Sledgehammer.logger.warning("Trying to save player display preferences to a null file, aborting");
			return;
		}
		if(preferences == null) {
			long t = System.currentTimeMillis();
			if(t - lastNullSaveTime > 5*3600*1000) {
				Sledgehammer.logger.warning("Trying to save null player display preferences, this is not normal, aborting and reseting preferences! This message will not be logged again for 5mn.");
				lastNullSaveTime = t;
			}
			preferences = new Preferences();
			return;
		}
		try {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String str = gson.toJson(preferences);
			Files.write(str, file, Charset.defaultCharset());
		} catch (Exception e) {
			Sledgehammer.logger.warning("Failed to write player display preferences to " + file.getAbsolutePath());
			e.printStackTrace();
		}
	}

	public static void load() {
		if(!file.exists()) {
			preferences = new Preferences();
			Sledgehammer.logger.info("Player display preference file did not exist, used default");
		} else {
			try {
				String text = String.join("\n", Files.readLines(file, Charset.defaultCharset()));
				Gson gson = new Gson();
				preferences = gson.fromJson(text, Preferences.class);
			} catch (IOException | JsonSyntaxException e) {
				Sledgehammer.logger.warning("Failed to load player display preferences file, setting to default");
				e.printStackTrace();
				preferences = new Preferences();
			}
		}
	}

	public static void setFile(File file) {
		PlayerDisplayPreferences.file = file;
	}

	private static class Preferences {
		public Map<UUID, PlayerPreferences> players = new HashMap<UUID, PlayerPreferences>();
	}

	private static class PlayerPreferences {
		public boolean display = ConfigHandler.terramapPlayersDisplayDefault;
	}

}