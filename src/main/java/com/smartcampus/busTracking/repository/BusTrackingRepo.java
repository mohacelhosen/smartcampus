package com.smartcampus.busTracking.repository;

import com.smartcampus.busTracking.model.BusTracking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BusTrackingRepo extends MongoRepository<BusTracking, String> {
}
