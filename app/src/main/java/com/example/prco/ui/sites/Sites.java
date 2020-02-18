package com.example.prco.ui.sites;

public class Sites {

    private String site_name, site_locationLat, site_locationLong, site_desc;

    public Sites() {}

    public Sites(String site_name, String site_locationLat, String site_locationLong, String site_desc) {
        this.site_name = site_name;
        this.site_locationLat = site_locationLat;
        this.site_locationLong = site_locationLong;
        this.site_desc = site_desc;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public String getSite_locationLat() {
        return site_locationLat;
    }

    public void setSite_locationLat(String site_locationLat) {
        this.site_locationLat = site_locationLat;
    }

    public String getSite_locationLong() {
        return site_locationLong;
    }

    public void setSite_locationLong(String site_locationLong) {
        this.site_locationLong = site_locationLong;
    }

    public String getSite_desc() {
        return site_desc;
    }

    public void setSite_desc(String site_desc) {
        this.site_desc = site_desc;
    }
}
