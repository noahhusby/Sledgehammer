/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - SmartObject.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.server;

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
