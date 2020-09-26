package com.noahhusby.sledgehammer;

import net.minecraftforge.common.config.Configuration;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;

    public static String authenticationCode = "";
    public static String tpllMode = "";
    public static int executionTimeout = 10000;
    public static String bungeecordName = "";

    public static void init(File f) {
        config = new Configuration(f);
        config.addCustomCategoryComment("General", "");
        tpllMode = config.getString("Teleportation Mode", "General", "internal"
                , "Use 'internal' for sledgehammer's internal interpreter. Use 'tpll' for terra121's interpreter, or 'cs' for BTE Tool's interpreter.");
        authenticationCode = config.getString("Network Authentication Code", "General", ""
                , "Use the same authentication code as the bungeecord server you are connecting to");
        executionTimeout = config.getInt("Execution Timeout", "General", 10000, 2000, 30000, "Raise this if sledgehammer's inter-server actions aren't executing. Default: 10000 (10 seconds)");

        config.save();
    }

    public static void registerConfig() {
        Sledgehammer.sledgehammer.getDataFolder().mkdir();
        init(new File(Sledgehammer.sledgehammer.getDataFolder(), Constants.MODID + ".cfg"));
    }
}
