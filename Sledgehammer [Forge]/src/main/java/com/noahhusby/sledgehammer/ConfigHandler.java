package com.noahhusby.sledgehammer;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;

    public static String authenticationCode = "";
    public static String tpllMode = "";

    public static void init(File f) {
        config = new Configuration(f);
        config.addCustomCategoryComment("General", "");
        tpllMode = config.getString("Teleportation Mode", "General", "internal"
                , "Use 'internal' for sledgehammer's internal interpreter. Use 'tpll' for terra121's interpreter, or 'cs' for BTE Tool's interpreter.");
        authenticationCode = config.getString("Network Authentication Code", "General", ""
                , "Use the same authentication code as the bungeecord server you are connecting to");

        config.save();
    }

    public static void registerConfig(FMLPreInitializationEvent e) {
        Sledgehammer.config = new File(e.getModConfigurationDirectory()+"/"+Reference.MODID);
        Sledgehammer.config.mkdirs();
        init(new File(Sledgehammer.config.getAbsolutePath(), Reference.MODID + ".cfg"));
    }
}
