package com.noahhusby.sledgehammer.data.dialogs.scenes.location;

import com.noahhusby.sledgehammer.data.dialogs.components.location.LocationSelectionComponent;
import com.noahhusby.sledgehammer.data.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.handlers.DialogHandler;
import net.md_5.bungee.api.config.ServerInfo;

public class LocationSelectionScene extends DialogScene {

    private ServerInfo server;
    private DialogScene scene;

    public LocationSelectionScene(ServerInfo server) {
        this(server, null);
    }

    public LocationSelectionScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new LocationSelectionComponent());
    }

    @Override
    public void onFinish() {
        switch(getValue("locationmode").toLowerCase()) {
            case "city":
                DialogHandler.getInstance().startDialog(getCommandSender(), new CityScene(server, scene));
                break;
            case "county":
                DialogHandler.getInstance().startDialog(getCommandSender(), new CountyScene(server, scene));
                break;
            case "state":
                DialogHandler.getInstance().startDialog(getCommandSender(), new StateScene(server, scene));
                break;
            case "country":
                DialogHandler.getInstance().startDialog(getCommandSender(), new CountryScene(server, scene));
                break;
        }
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
