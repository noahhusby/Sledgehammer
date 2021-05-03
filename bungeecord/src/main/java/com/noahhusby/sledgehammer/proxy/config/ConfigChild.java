package com.noahhusby.sledgehammer.proxy.config;

/**
 * @author Noah Husby
 */
public interface ConfigChild {
    void onPreLoad();

    void onPostLoad();
}
