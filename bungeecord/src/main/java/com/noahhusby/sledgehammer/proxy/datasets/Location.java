/*
 * MIT License
 *
 * Copyright 2020-2022 noahhusby
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS
 * BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package com.noahhusby.sledgehammer.proxy.datasets;

import com.google.gson.annotations.Expose;
import com.noahhusby.sledgehammer.proxy.ChatUtil;

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

    public Location() {
    }

    public Location(Detail detailType, String city, String county, String state, String country) {
        this.detailType = detailType;
        if (city != null) {
            this.city = city.toLowerCase();
        }
        if (county != null) {
            this.county = county.toLowerCase();
        }
        if (state != null) {
            this.state = state.toLowerCase();
        }
        if (country != null) {
            this.country = country.toLowerCase();
        }
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

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        if (!city.equals("")) {
            string.append(ChatUtil.capitalize(city)).append(", ");
        }
        if (!county.equals("")) {
            string.append(ChatUtil.capitalize(county)).append(", ");
        }
        if (!state.equals("")) {
            string.append(ChatUtil.capitalize(state)).append(", ");
        }
        if (!country.equals("")) {
            string.append(ChatUtil.capitalize(country)).append(", ");
        }
        return string.toString();
    }

    public enum Detail {
        none, city, county, state, country
    }
}
