package org.example.stripeexample.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ChargeRequest {

    public enum Status {
        WAITING, SUCCESS, FAILURE
    }

    private String sessionId;

    private List<Product> products;

    private Status paymentStatus;

}
