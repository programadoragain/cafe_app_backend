package com.ferdev.cafe.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface BillService{

    ResponseEntity<String> generateReport(Map<String,Object> requestMap);
}
