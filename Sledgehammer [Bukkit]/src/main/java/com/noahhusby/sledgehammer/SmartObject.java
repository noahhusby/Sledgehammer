/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bukkit] - SmartObject.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer;

import org.json.simple.JSONObject;

public class SmartObject {

    private final JSONObject o;

    private SmartObject(JSONObject o) {
        this.o = o;
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public String getStringOrDefault(String key, String def) {
        if(get(key) == null) return def;
        return (String) get(key);
    }

    public boolean getBoolean(String key) {
        return (boolean) get(key);
    }

    public boolean getBooleanOrDefault(String key, boolean def) {
        if(get(key) == null) return def;
        return (boolean) get(key);
    }

    public int getInteger(String key) {
        return (int) get(key);
    }

    public int getIntegerOrDefault(String key, int def) {
        if(get(key) == null) return def;
        return (int) get(key);
    }

    public double getDouble(String key) {
        return (double) get(key);
    }

    public Double getDoubleOrDefault(String key, Double def) {
        if(get(key) == null) return def;
        return (double) get(key);
    }

    public String toJSONString() {
        return o.toJSONString();
    }

    public Object get(String key) {
        return o.get(key);
    }

    public JSONObject toJSON() {
        return o;
    }

    public static SmartObject fromJSON(JSONObject object) {
        return new SmartObject(object);
    }
}
