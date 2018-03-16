package com.jsonchecker.JsonChecker;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;

@RestController
public class HelloController {

    @GetMapping("/hello")
    public String greeting() {
        return "Hello World";
    }
}
