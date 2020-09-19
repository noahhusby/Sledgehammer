package com.noahhusby.sledgehammer.data.dialogs;

import com.noahhusby.sledgehammer.data.dialogs.components.location.CityComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.location.CountryComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.location.StateComponent;

public class CityScene extends DialogScene {

    private String server;

    public CityScene(String server) {
        this.server = server;
        registerComponent(new CityComponent());
        registerComponent(new StateComponent());
        registerComponent(new CountryComponent());
    }

    @Override
    public void onFinish() {

    }
}
