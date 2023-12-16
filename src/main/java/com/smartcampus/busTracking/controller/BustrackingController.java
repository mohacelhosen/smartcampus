package com.smartcampus.busTracking.controller;

import com.smartcampus.busTracking.model.BusTracking;
import com.smartcampus.busTracking.service.BusTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/university/bus-tracking")
@CrossOrigin("*")
public class BustrackingController {
    @Autowired
    private BusTrackingService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody BusTracking tracking) {
        BusTracking save = service.save(tracking);
        return new ResponseEntity<>(save, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> update(@RequestBody BusTracking tracking) {
        BusTracking save = service.update(tracking);
        return new ResponseEntity<>(save, HttpStatus.OK);
    }
}
