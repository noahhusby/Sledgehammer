package com.noahhusby.sledgehammer.config.types;

import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.datasets.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.List;

public class Server {
    @Expose
    public String name;
    @Expose
    public boolean earthServer;
    @Expose
    public String permission_type;
    @Expose
    public List<Location> locations = new ArrayList<>();

    private String shVersion = null;
    private String tpllMode = null;

    public Server(String name) {
        this.name = name;
    }

    public ServerInfo getServerInfo() {
        return ProxyServer.getInstance().getServerInfo(name);
    }

    public boolean isInitialized() {
        return shVersion != null;
    }

    public String getSledgehammerVersion() {
        return shVersion;
    }

    public String getTpllMode() {
        return tpllMode;
    }

    public void initialize(String version, String tpllMode) {
        this.shVersion = version;
        this.tpllMode = tpllMode;
    }
}
