package com.noahhusby.sledgehammer;

import com.noahhusby.sledgehammer.communication.SimpleChannelManager;
import com.noahhusby.sledgehammer.handlers.PlayerLocationHandler;
import com.noahhusby.sledgehammer.handlers.TaskHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.relauncher.Side;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.List;

@Mod(modid = Reference.MODID, name = "BTE Utilities", version = Reference.VERSION, dependencies = "", updateJSON = "", acceptableRemoteVersions = "*", acceptedMinecraftVersions = "1.12.2")
public class Sledgehammer
{
    public static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass().getSimpleName());
    public static File config;

    public Sledgehammer() { }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e)
    {
        SimpleChannelManager.init();
        ConfigHandler.registerConfig(e);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartedEvent event)
    {
        if (event.getSide() == Side.CLIENT)
        {
            return;
        }
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        if (event.getSide() == Side.CLIENT)
        {
            //TODO: Replace with logger
            System.out.println("Disabled on client side as set by config. (server starting)");
            return;
        }

        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    void logIn(PlayerEvent.PlayerLoggedInEvent event) {
        TaskHandler.getInstance().playerJoined(event.player);
        List<EntityPlayerMP> players = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayers();
        for(EntityPlayerMP player : players) {
            if(player.getName().equals(event.player.getName())) {
                PlayerLocationHandler.getInstance().onPlayerJoin(player);
            }
        }
    }

    @SubscribeEvent
    void logOut(PlayerEvent.PlayerLoggedInEvent event) { }

}
