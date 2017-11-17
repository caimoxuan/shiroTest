package com.cmx.springbootshiro.domain.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

public class Place {

    @Id
    private ObjectId id;

    private String address;

    private GeoJsonPoint loc;

    public Place() {
    }

    public Place(ObjectId id, String address, GeoJsonPoint loc) {
        this.id = id;
        this.address = address;
        this.loc = loc;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoJsonPoint getLoc() {
        return loc;
    }

    public void setLoc(GeoJsonPoint loc) {
        this.loc = loc;
    }
}
