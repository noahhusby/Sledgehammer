package com.noahhusby.sledgehammer.config;

import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.datasets.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServerConfig {
    private static ServerConfig instance;

    public static ServerConfig getInstance() {
        return instance;
    }

    public static void setInstance(ServerConfig instance) {
        ServerConfig.instance = instance;
    }

    public ServerConfig() {
    }

    @Expose(serialize = true, deserialize = true)
    public List<Server> servers = new ArrayList<>();

    public List<Server> getServers() {
        return servers;
    }

    public void pushServer(Server server) {
        List<Server> remove = new ArrayList<>();
        for(Server s : servers) {
            if(s.name.toLowerCase().equals(server.name)) {
                remove.add(s);
            }
        }

        for(Server s : remove) {
            servers.remove(s);
        }

        servers.add(server);
        ConfigHandler.getInstance().saveServerDB();
    }


    public Server getServer(String name) {
        for(Server s : servers) {
            if(s.name.toLowerCase().equals(name.toLowerCase())) {
                return s;
            }
        }
        return null;
    }

    public List<Location> getLocationsFromServer(String server) {
        for(Server s : servers) {
            if(s.name.toLowerCase().equals(server.toLowerCase())) {
                return s.locations;
            }
        }

        return null;
    }
}
