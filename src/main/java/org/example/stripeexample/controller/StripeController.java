package org.example.stripeexample.controller;

import lombok.RequiredArgsConstructor;
import org.example.stripeexample.service.StripeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class StripeController {

    private final StripeService service;

    @GetMapping("/init")
    public void init() {
        service.testMethod();
    }

    @GetMapping("/success")
    public String getSuccess(@RequestParam String session_id) {
        System.out.println("session_id" + session_id);
        return session_id;
    }

    @GetMapping("/cancel")
    public void cancel() {
        System.out.println("cancel");
    }

}
