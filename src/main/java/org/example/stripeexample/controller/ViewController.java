package org.example.stripeexample.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/checkout")
    public String getCheckoutPage() {
        return "checkout";
    }
}
