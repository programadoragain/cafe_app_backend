package com.ferdev.cafe.Service;

import org.springframework.http.ResponseEntity;

import java.util.Map;

public interface DashboardService {
    ResponseEntity<Map<String,Object>> getCount();
}
