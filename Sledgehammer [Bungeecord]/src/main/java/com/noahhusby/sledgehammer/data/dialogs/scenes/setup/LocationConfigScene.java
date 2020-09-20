package com.noahhusby.sledgehammer.data.dialogs.scenes.setup;

import com.noahhusby.sledgehammer.data.dialogs.components.setup.LocationMenuComponent;
import com.noahhusby.sledgehammer.data.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.data.dialogs.scenes.location.LocationSelectionScene;
import com.noahhusby.sledgehammer.data.dialogs.toolbars.IToolbar;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
import com.noahhusby.sledgehammer.util.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

public class LocationConfigScene extends DialogScene {

    private ServerInfo server;

    public LocationConfigScene(ServerInfo server) {
        this.server = server;
        registerComponent(new LocationMenuComponent());
    }

    @Override
    public TextElement[] getTitle() {
        return new TextElement[]{new TextElement("Editing Locations - ", ChatColor.GRAY),
        new TextElement(server.getName(), ChatColor.RED)};
    }

    @Override
    public void onFinish() {
        String v = getValue("location").trim().toLowerCase();
        if(v.equals("add")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new LocationSelectionScene(server, new LocationConfigScene(server)));
        } else if(v.equals("finish")) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), new ConfigScene(server, true));
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
