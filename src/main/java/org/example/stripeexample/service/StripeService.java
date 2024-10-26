package org.example.stripeexample.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.stripeexample.model.ChargeRequest;
import org.example.stripeexample.model.Product;
import org.example.stripeexample.repository.MockedDataRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    @Value("${stripe.priceId}")
    private String priceId;

    private MockedDataRepository repository = MockedDataRepository.getInstance();

    @PostConstruct
    void init() {
        Stripe.apiKey = this.secretKey;
    }

    @SneakyThrows
    public String chargeUserWithPayment(List<Product> products) {
        List<SessionCreateParams.LineItem> lineItems = products.stream()
                .map(product -> SessionCreateParams.LineItem.builder()
                        .setPrice(product.getPriceId())  // Используем priceId для создания строки чекаута
                        .setQuantity(1L)
                        .build())
                .toList();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8080/success?session_id={CHECKOUT_SESSION_ID}")
//                        .setCancelUrl("http://localhost:8080/cancel?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl("http://localhost:8080/cancel")
                        .addAllLineItem(lineItems)
                        .build();

        Session session = null;
        try {
            session = Session.create(params);
        } catch (StripeException e) {
            throw new RuntimeException(e);
        }

        String sessionId = session.getId();
        repository.addChargeRequest(new ChargeRequest(sessionId, products, ChargeRequest.Status.WAITING));

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
