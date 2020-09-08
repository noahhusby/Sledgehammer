package com.noahhusby.sledgehammer.data.dialogs;

import com.noahhusby.sledgehammer.data.dialogs.components.location.LocationSelectionComponent;
import com.noahhusby.sledgehammer.handlers.DialogHandler;

public class LocationSelectionScene extends DialogScene {

    private String server;

    public LocationSelectionScene(String server) {
        this.server = server;
        registerComponent(new LocationSelectionComponent());
    }

    @Override
    public void onFinish() {
        switch(getValue("locationmode").toLowerCase()) {
            case "city":
                DialogHandler.getInstance().startDialog(getCommandSender(), new CityScene(server));
                break;
            case "county":
                DialogHandler.getInstance().startDialog(getCommandSender(), new CountyScene(server));
                break;
            case "state":
                DialogHandler.getInstance().startDialog(getCommandSender(), new StateScene(server));
                break;
            case "country":
                DialogHandler.getInstance().startDialog(getCommandSender(), new CountryScene(server));
                break;
        }
    }
}
