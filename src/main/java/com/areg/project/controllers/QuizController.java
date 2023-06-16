/**
 * Copyright (c) 2023 Areg Abgaryan
 */

package com.areg.project.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuizController {

    @GetMapping("/")
    public String index() {
        return "Welcome to music quiz!";
    }


    //  Run with :  http://localhost:8080/hello?name=Areg
    @GetMapping("/hello")
    public String sayHello(@RequestParam(value = "name", defaultValue = "World") String name) {
        return String.format("Hello %s!", name);
    }
}
