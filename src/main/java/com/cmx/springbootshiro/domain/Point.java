package com.cmx.springbootshiro.domain;


public class Point {

    private Double lng;

    private Double lat;

    public Point(Double lng, Double lat) {
        this.lng = lng;
        this.lat = lat;
    }

    public Point() {
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }
}