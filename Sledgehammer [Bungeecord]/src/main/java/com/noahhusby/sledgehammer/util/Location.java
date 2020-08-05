package com.noahhusby.sledgehammer.util;

import java.time.LocalDateTime;

public class Location {
    public String city = "";
    public String county = "";
    public String state = "";
    public String country = "";

    public Location(String city, String county, String state, String country) {
        if(city != null) {
            this.city = city.toLowerCase();
        }

        if(county != null) {
            this.county = county.toLowerCase();
        }

        if(state != null) {
            this.state = state.toLowerCase();
        }

        if(country != null) {
            this.country = country.toLowerCase();
        }
    }
}
