package com.ebcr.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    @GetMapping
   public String greeting(){
       return "Hello user well come to the authentication part";
   }
}
