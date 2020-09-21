package com.noahhusby.sledgehammer.data.dialogs.scenes.setup;

import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.data.dialogs.components.location.LocationListComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.location.LocationRemovalComponent;
import com.noahhusby.sledgehammer.data.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;

public class LocationListScene extends DialogScene {

    private ServerInfo server;
    private DialogScene scene;

    public LocationListScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new LocationListComponent(server));
    }

    @Override
    public TextElement[] getTitle() {
        return new TextElement[]{new TextElement("Locations - ", ChatColor.GRAY),
        new TextElement(server.getName(), ChatColor.RED)};
    }

    @Override
    public void onFinish() {
        DialogHandler.getInstance().discardDialog(this);
        if(scene != null) {
            DialogHandler.getInstance().startDialog(getCommandSender(), scene);
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
