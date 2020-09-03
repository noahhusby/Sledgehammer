package com.noahhusby.sledgehammer.config.types;

import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.datasets.Location;

import java.util.ArrayList;
import java.util.List;

public class Server {
    @Expose
    public String name;
    @Expose
    public String permission_type;
    @Expose
    public List<Location> locations = new ArrayList<>();

    public Server(String name) {
        this.name = name;
    }
}
