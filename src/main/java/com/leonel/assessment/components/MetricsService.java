package com.leonel.assessment.components;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

@Service
public class MetricsService {
 
    private ConcurrentMap<Integer, Integer> statusMetric;
 
    public MetricsService() {
        statusMetric = new ConcurrentHashMap<Integer, Integer>();
    }
     
    public void increaseCount(int status) {
        Integer statusCount = statusMetric.get(status);
        if (statusCount == null) {
            statusMetric.put(status, 1);
        } else {
            statusMetric.put(status, statusCount + 1);
        }
    }
 
    public Map getStatusMetric() {
        return statusMetric;
    }
}
