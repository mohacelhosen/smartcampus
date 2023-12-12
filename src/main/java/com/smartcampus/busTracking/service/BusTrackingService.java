package com.smartcampus.busTracking.service;

import com.smartcampus.busTracking.model.BusTracking;
import com.smartcampus.busTracking.repository.BusTrackingRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BusTrackingService {
    @Autowired
    private BusTrackingRepo repo;

    public BusTracking save(BusTracking busTracking) {
        return repo.save(busTracking);
    }

    public BusTracking update(BusTracking busTracking) {
        if (busTracking.getId() != null || !busTracking.getId().isEmpty()) {
            Optional<BusTracking> byId = repo.findById(busTracking.getId());
            byId.get();
            BusTracking dbBusTrack = byId.get();
            dbBusTrack.setLongitude(busTracking.getLongitude());
            dbBusTrack.setLatitude(busTracking.getLatitude());
            return repo.save(dbBusTrack);
        } else {
            throw new RuntimeException("Bus-tracking id can't be null or empty");
        }
    }

}
