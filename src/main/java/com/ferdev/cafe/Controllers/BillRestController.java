package com.ferdev.cafe.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/bill")
public interface BillRestController {

    @PostMapping("/generate")
    ResponseEntity<String> generateReport(@RequestBody Map<String,Object> requestMap);

    @GetMapping("/getBills")
    ResponseEntity<List<Bill>> getBills();
}
