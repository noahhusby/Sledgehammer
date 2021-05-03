package com.noahhusby.sledgehammer.proxy.modules;

/**
 * @author Noah Husby
 */
public interface Module {
    void onEnable();

    void onDisable();

    String getModuleName();
}
