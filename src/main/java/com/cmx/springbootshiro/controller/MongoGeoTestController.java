package com.cmx.springbootshiro.controller;


import com.cmx.springbootshiro.domain.Point;
import com.cmx.springbootshiro.service.MongoService;
import com.mongodb.DBObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/mongo")
public class MongoGeoTestController {


    @Autowired
    MongoService mongoService;

    @RequestMapping("/getGeoNear")
    @ResponseBody
    public List<DBObject> getGeoNear(Integer distance){
        Point p = new Point(118.783799,31.979234);

        List<DBObject> result = mongoService.geoNear("mongoTest", null, p, 10, distance);

        return result;
    }


}

