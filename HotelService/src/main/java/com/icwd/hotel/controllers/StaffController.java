package com.icwd.hotel.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/staffs")
public class StaffController {

    @RequestMapping
    public ResponseEntity<List<String>> getStaffs(){
        List<String> staffNames = Arrays.asList("Ram", "Shyam", "Sita", "Krishna");
        return new ResponseEntity<>(staffNames, HttpStatus.OK);
    }
}
