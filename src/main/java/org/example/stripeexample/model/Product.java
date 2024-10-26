package org.example.stripeexample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Product {

    private String id;
    private String name;
    private String description;
    private String priceId;
    private int quantity;

}
