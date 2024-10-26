package org.example.stripeexample.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.stripeexample.model.Product;
import org.example.stripeexample.service.StripeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class StripeController {

    private final StripeService service;

    @PostMapping("/charge")
    public String charge(@RequestBody List<Product> products, HttpServletRequest request) {
        log.info("product: {}", products);
        log.info("init: {}", request.getSession().getId());
        return service.chargeUserWithPayment(products);
    }

    @SneakyThrows
    @GetMapping("/success")
    public void getSuccess(@RequestParam("session_id") String sessionId, HttpServletResponse response) {
        log.info("session_id {}", sessionId);
        service.handleSuccessPayment(sessionId);
    }

    @GetMapping("/cancel")
    public void cancel(@RequestParam("session_id") String sessionId) {
        service.handleCancelPayment(sessionId);
    }

}
