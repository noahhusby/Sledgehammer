/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Location.java
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
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.proxy.datasets;

import com.google.gson.annotations.Expose;

public class Location {
    @Expose
    public Detail detailType;
    @Expose
    public String city = "";
    @Expose
    public String county = "";
    @Expose
    public String state = "";
    @Expose
    public String country = "";

    public Location() {}

    public Location(Detail detailType, String city, String county, String state, String country) {
        this.detailType = detailType;
        if(city != null) this.city = city.toLowerCase();
        if(county != null) this.county = county.toLowerCase();
        if(state != null) this.state = state.toLowerCase();
        if(country != null) this.country = country.toLowerCase();
    }

    public boolean compare(Location location, Detail detail) {
        switch (detail) {
            case city:
                return location.city.equalsIgnoreCase(city) && (location.state.equalsIgnoreCase(state) || location.country.equalsIgnoreCase(country));
            case county:
                if (!country.equals("")) {
                    return location.county.equalsIgnoreCase(county) && location.state.equalsIgnoreCase(state) && location.country.equalsIgnoreCase(country);
                } else {
                    return location.county.equalsIgnoreCase(county) && location.state.equalsIgnoreCase(state);
                }
            case state:
                return location.state.equalsIgnoreCase(state) && location.country.equalsIgnoreCase(country);
            case country:
                return location.country.equalsIgnoreCase(country);
        }
        return false;
    }

    public enum Detail {
        none, city, county, state, country
    }
}
