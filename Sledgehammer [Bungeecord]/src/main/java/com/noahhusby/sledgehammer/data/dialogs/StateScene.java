package com.noahhusby.sledgehammer.data.dialogs;

import com.noahhusby.sledgehammer.data.dialogs.components.location.CountryComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.location.CountyComponent;
import com.noahhusby.sledgehammer.data.dialogs.components.location.StateComponent;

public class StateScene extends DialogScene {

    private String server;

    public StateScene(String server) {
        this.server = server;
        registerComponent(new StateComponent());
        registerComponent(new CountryComponent());
    }

    @Override
    public void onFinish() {
        System.out.println(getValue("state"));
        System.out.println(getValue("country"));
    }
}
