package com.noahhusby.sledgehammer.data.dialogs;

import com.noahhusby.sledgehammer.data.dialogs.components.location.CityComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.location.CountryComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.location.CountyComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.location.StateComponent;

public class CountyScene extends DialogScene {

    private String server;

    public CountyScene(String server) {
        this.server = server;
        registerComponent(new CountyComponent());
        registerComponent(new StateComponent());
        registerComponent(new CountryComponent());
    }

    @Override
    public void onFinish() {
        System.out.println(getValue("county"));
        System.out.println(getValue("state"));
        System.out.println(getValue("country"));
    }
}
