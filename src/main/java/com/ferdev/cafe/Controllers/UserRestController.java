package com.ferdev.cafe.Controllers;

import com.ferdev.cafe.Wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;

@RequestMapping("/user")
public interface UserRestController {

    @PostMapping("/signup")
    ResponseEntity<String> signUp(@RequestBody(required = true) Map<String,String> requestMap);

    @PostMapping("/login")
    ResponseEntity<String> login(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping("/test")
    ResponseEntity<String> test();

    @GetMapping("/get")
    ResponseEntity<List<UserWrapper>> getAllUser();

    @PostMapping("/update")
    ResponseEntity<String> update(@RequestBody(required = true) Map<String,String> requestMap);

    @GetMapping("/check-token")
    ResponseEntity<String> checkToken();

    @PostMapping("/change-password")
    ResponseEntity<String> changePassword(@RequestBody(required = true) Map<String,String> requestMap);

    @PostMapping("/forgot-password")
    ResponseEntity<String> forgotPassword(@RequestBody(required = true) Map<String,String> requestMap);
}
