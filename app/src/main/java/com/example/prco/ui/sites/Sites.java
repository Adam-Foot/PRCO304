package com.example.prco.ui.sites;

public class Sites {

    private String site_name, site_desc, site_url;
    private Double site_locationLat, site_locationLong;

    public Sites() {}

    public Sites(String site_name, Double site_locationLat, Double site_locationLong, String site_desc, String site_url) {
        this.site_name = site_name;
        this.site_locationLat = site_locationLat;
        this.site_locationLong = site_locationLong;
        this.site_desc = site_desc;
        this.site_url = site_url;
    }

    public String getSite_name() {
        return site_name;
    }

    public void setSite_name(String site_name) {
        this.site_name = site_name;
    }

    public Double getSite_locationLat() {
        return site_locationLat;
    }

    public void setSite_locationLat(Double site_locationLat) {
        this.site_locationLat = site_locationLat;
    }

    public Double getSite_locationLong() {
        return site_locationLong;
    }

    public void setSite_locationLong(Double site_locationLong) {
        this.site_locationLong = site_locationLong;
    }

    public String getSite_desc() {
        return site_desc;
    }

    public void setSite_desc(String site_desc) {
        this.site_desc = site_desc;
    }

    public String getSite_url() {
        return site_url;
    }

    public void setSite_url(String site_url) {
        this.site_url = site_url;
    }
}
