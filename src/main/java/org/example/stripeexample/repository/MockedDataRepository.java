package org.example.stripeexample.repository;

import org.example.stripeexample.model.ChargeRequest;

import java.util.ArrayList;
import java.util.List;

public class MockedDataRepository {

    private static MockedDataRepository mockedDataRepository;

    private List<ChargeRequest> chargeRequests;

    public ChargeRequest getChargeRequestBySessionId(String sessionId) {
        return chargeRequests.stream()
                .filter(request -> request.getSessionId().equals(sessionId) && request.getPaymentStatus() == ChargeRequest.Status.WAITING)
                .findAny()
                .orElseThrow(() -> new RuntimeException("there is no such charge requests"));
    }

    public void addChargeRequest(ChargeRequest chargeRequest) {
        chargeRequests.add(chargeRequest);
    }

    public void deleteChargeRequest(ChargeRequest chargeRequest) {
        chargeRequests.remove(chargeRequest);
    }

    public void updateChargeRequestStatusBySessionId(String sessionId, ChargeRequest.Status status) {
        chargeRequests.stream()
                .filter(charge -> charge.getSessionId().equals(sessionId) && charge.getPaymentStatus() == ChargeRequest.Status.WAITING)
                .findAny()
                .ifPresent(chargeRequest -> chargeRequest.setPaymentStatus(status));
    }

    public static MockedDataRepository getInstance() {
        if (mockedDataRepository == null) {
            return new MockedDataRepository();
        }

        return mockedDataRepository;
    }

    private MockedDataRepository() {
        this.chargeRequests = new ArrayList<>();
    }

}