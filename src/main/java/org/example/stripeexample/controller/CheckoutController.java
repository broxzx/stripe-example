package org.example.stripeexample.controller;

import org.example.stripeexample.model.ChargeRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {

    @Value("${stripe.publicKey}")
    private String stripePublicKey;

    @GetMapping("/checkout")
    public String checkout(Model model) {
        model.addAttribute("amount", 505 * 100);
        model.addAttribute("stripePublicKey", stripePublicKey);
        model.addAttribute("currency", ChargeRequest.Currency.EUR);
        return "checkout";
    }
}
