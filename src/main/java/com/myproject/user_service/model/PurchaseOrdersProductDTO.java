package com.myproject.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrdersProductDTO {
    private String productName;
    private Integer quantity;
}
