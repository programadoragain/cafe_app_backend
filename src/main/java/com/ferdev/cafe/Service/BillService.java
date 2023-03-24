package com.ferdev.cafe.Service;

import org.springframework.http.ResponseEntity;
import com.ferdev.cafe.Entities.Bill;
import java.util.List;

import java.util.Map;

public interface BillService{
    ResponseEntity<List<Bill>> getBills();
    ResponseEntity<String> generateReport(Map<String,Object> requestMap);
}
