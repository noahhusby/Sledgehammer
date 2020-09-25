package com.noahhusby.sledgehammer;

import com.noahhusby.sledgehammer.communication.CommunicationManager;
import com.noahhusby.sledgehammer.eventhandler.ServerEventHandler;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Sledgehammer extends JavaPlugin implements Listener {

    public static Logger logger;
    public static Plugin sledgehammer;

    @Override
    public void onEnable() {
        logger = getLogger();
        sledgehammer = this;
        ConfigHandler.registerConfig();
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new ServerEventHandler(), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "sledgehammer:channel");
        getServer().getMessenger().registerIncomingPluginChannel( this, "sledgehammer:channel", new CommunicationManager());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        TaskHandler.getInstance().playerJoined(event.getPlayer());
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        System.out.println(e.getMessage());
        if(e.getMessage().toLowerCase().equals("poop") || e.getMessage().toLowerCase().equals("/poop")) {
            Location x = e.getPlayer().getLocation();
            Block b = e.getPlayer().getWorld().getBlockAt(x);
            System.out.println(b.getType().name());
        }
    }


}
