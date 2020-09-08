package com.noahhusby.sledgehammer.data.dialogs;

import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.data.dialogs.components.location.CountryComponent;
import com.noahhusby.sledgehammer.datasets.Location;
import net.md_5.bungee.api.ProxyServer;

public class CountryScene extends DialogScene {

    private String server;

    public CountryScene(String server) {
        this.server = server;
        registerComponent(new CountryComponent());
    }

    @Override
    public void onFinish() {
        Location l = new Location(Location.detail.country, "", "", "", getValue("country"));
        Server s = ServerConfig.getInstance().getServer(server);

        if(s == null) s = new Server(server);

        s.locations.add(l);
        ServerConfig.getInstance().pushServer(s);
    }
}
