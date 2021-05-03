package com.noahhusby.sledgehammer.proxy.config;

import com.google.common.collect.Lists;

import java.io.File;
import java.util.List;

/**
 * @author Noah Husby
 */
public abstract class Config {

    private final List<ConfigChild> children = Lists.newArrayList();

    protected void onPreLoad() {
        children.forEach(ConfigChild::onPreLoad);
    }

    protected void onPostLoad() {
        children.forEach(ConfigChild::onPostLoad);
    }

    public abstract void init(File configFolder);

    public abstract void load();

    public abstract void unload();

    public abstract void reload();

    public void addChild(ConfigChild child) {
        children.add(child);
    }
}
