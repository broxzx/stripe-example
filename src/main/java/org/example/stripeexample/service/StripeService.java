package org.example.stripeexample.service;

import com.stripe.Stripe;
import com.stripe.model.checkout.Session;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.stripeexample.model.ChargeRequest;
import org.example.stripeexample.model.Product;
import org.example.stripeexample.repository.MockedDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    private final MockedDataRepository repository = MockedDataRepository.getInstance();

    @PostConstruct
    void init() {
        Stripe.apiKey = this.secretKey;
    }

    @SneakyThrows
    public String chargeUserWithPayment(List<Product> products) {
        List<Map<String, Object>> bodyProductList = products.stream()
                .map(product -> Map.of(
                        "price_data", Map.of(
                                "currency", "usd", // todo: create charge object to select which currency to use
                                "product_data", Map.of("name", product.getName()),
                                "unit_amount", product.getPrice() * 100
                        ),
                        "quantity", product.getQuantity()
                ))
                .toList();

        Map<String, Object> params = new HashMap<>();
        params.put("payment_method_types", List.of("card"));
        params.put("line_items", bodyProductList);
        params.put("mode", "payment");
        params.put("success_url", "http://localhost:8080/success?session_id={CHECKOUT_SESSION_ID}");
        params.put("cancel_url", "http://localhost:8080/cancel?session_id={CHECKOUT_SESSION_ID}");

        Session session = Session.create(params);
        log.info(session.getUrl());

        repository.addChargeRequest(ChargeRequest.builder()
                .sessionId(session.getId())
                .products(products)
                .paymentStatus(ChargeRequest.Status.WAITING)
                .build());

        return session.getUrl();
    }

    public void handleSuccessPayment(String sessionId) {
        ChargeRequest chargeRequestBySessionId = repository.getChargeRequestBySessionId(sessionId);
        log.info("{}", chargeRequestBySessionId);
        repository.updateChargeRequestStatusBySessionId(sessionId, ChargeRequest.Status.SUCCESS);
    }

    public void handleCancelPayment(String sessionId) {
        repository.updateChargeRequestStatusBySessionId(sessionId, ChargeRequest.Status.FAILURE);
    }
}
