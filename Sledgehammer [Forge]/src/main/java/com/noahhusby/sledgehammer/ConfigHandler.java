package com.noahhusby.sledgehammer;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ConfigHandler {
    public static Configuration config;

    public static String authenticationCode = "";

    public static void init(File f) {
        config = new Configuration(f);
        config.addCustomCategoryComment("General", "");
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
