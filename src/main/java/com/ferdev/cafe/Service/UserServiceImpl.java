package com.ferdev.cafe.Service;

import com.ferdev.cafe.Jwt.JwtFilter;
import com.ferdev.cafe.Jwt.JwtUtil;
import com.ferdev.cafe.Security.CustomUserDetailsService;
import com.ferdev.cafe.Constants.CafeConstanst;
import com.ferdev.cafe.Entities.User;
import com.ferdev.cafe.Repositories.UserRepository;
import com.ferdev.cafe.Util.CafeUtils;
import com.ferdev.cafe.Util.EmailUtils;
import com.ferdev.cafe.Wrapper.UserWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder passwordEncode;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    EmailUtils emailUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("Inside signUp {}", requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = userRepository.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    userRepository.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity("Successfully registered", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exist", HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstanst.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login {}", requestMap);
        try {
             Authentication authentication= authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));

            if (authentication.isAuthenticated()) {
                if (customUserDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true"))
                    return new ResponseEntity<String>("{\"token\":\"" + jwtUtil.generateToken(customUserDetailsService.getUserDetail().getEmail(), customUserDetailsService.getUserDetail().getRole()) + "\"}", HttpStatus.OK);
                else
                    return new ResponseEntity<String>("{\"message\"" + "Wait for admin approval" + "\"}", HttpStatus.OK);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<String>("{\"message\"" + "Bad credential" + "\"}", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userRepository.getAllUser(), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);

        } catch (Exception io){
          io.printStackTrace();
        }
        return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> userToUpdate= userRepository.findById(Integer.parseInt(requestMap.get("id")));
                if (!userToUpdate.isEmpty()) {
                    userRepository.updateStatus(requestMap.get("status"), Integer.parseInt(requestMap.get("id")));
                    sendMailToAllAdmin(requestMap.get("status"), userToUpdate.get().getEmail(), userRepository.getAllAdmin());
                    return CafeUtils.getResponseEntity("User Status updated.", HttpStatus.OK);
                }
                else
                    return CafeUtils.getResponseEntity("Id user not exist.", HttpStatus.OK);
            }
            else
                return new ResponseEntity<String>("User not allowed.", HttpStatus.UNAUTHORIZED);

        } catch (Exception io){
            io.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> checkToken() {
        return new ResponseEntity<>("function not implemented yet", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try {
            User user= userRepository.findByEmail(jwtFilter.getCurrentUser());
            if (!user.equals(null)) {
                if (user.getPassword().equals(requestMap.get("oldPassword"))) {
                    user.setPassword(requestMap.get("newPassword"));
                    userRepository.save(user);
                    return CafeUtils.getResponseEntity("Password Updated Succesfully.", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect Old Password.", HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try {
            User user= userRepository.findByEmail(requestMap.get("email"));
            if (!Objects.isNull(user)) {
                emailUtils.forgotMail(user.getEmail(), "Credentials for Cafe Management System", user.getPassword());
                return CafeUtils.getResponseEntity("Check your mail for credentials.", HttpStatus.OK);
            }
            else
                return CafeUtils.getResponseEntity("Incorrect data.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstanst.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void sendMailToAllAdmin(String status, String email, List<String> allAdmin) {
        String adminSender= jwtFilter.getCurrentUser();
        allAdmin.remove(adminSender);

        if (status != null && status.equalsIgnoreCase("true")) {
            String text= "USER:- " + email + " \n is approved by \nADMIN:- " + adminSender;
            emailUtils.sendSimpleMessage(adminSender,"Account Approved", text, allAdmin);
        }
        else {
            String text= "USER:- " + email + " \n is disapproved by \nADMIN:- " + adminSender;
            emailUtils.sendSimpleMessage(adminSender, "Account Disapproved", text, allAdmin);
        }
    }

    private boolean validateSignUpMap(Map<String,String> requestMap) {
        if (requestMap.containsKey("name") && requestMap.containsKey("phone")
                && requestMap.containsKey("email") && requestMap.containsKey("password")) {
            return true;
        }
        else
            return false;
    }

    private User getUserFromMap(Map<String,String> requestMap) {
        User user= new User();
        user.setName(requestMap.get("name"));
        user.setPhone(requestMap.get("phone"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(passwordEncode.encode(requestMap.get("password")));
        user.setStatus("false");
        user.setRole("user");

        return user;
    }
}


