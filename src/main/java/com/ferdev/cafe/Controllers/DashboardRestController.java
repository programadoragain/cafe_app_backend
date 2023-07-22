package com.ferdev.cafe.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RequestMapping("/dashboard")
public interface DashboardRestController {

    @GetMapping("/details")
    ResponseEntity<Map<String,Object>> getCount();
}
