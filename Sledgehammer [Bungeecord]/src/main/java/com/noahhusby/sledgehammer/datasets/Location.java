package com.noahhusby.sledgehammer.datasets;

import com.google.gson.annotations.Expose;

import java.time.LocalDateTime;

public class Location {
    @Expose
    public detail detailType;
    @Expose
    public String city = "";
    @Expose
    public String county = "";
    @Expose
    public String state = "";
    @Expose
    public String country = "";

    public Location(detail detailType, String city, String county, String state, String country) {
        this.detailType = detailType;
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

    public enum detail {
        none, city, county, state, country;
    }
}
