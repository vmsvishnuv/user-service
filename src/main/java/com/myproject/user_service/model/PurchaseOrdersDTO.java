package com.myproject.user_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrdersDTO {
    private Long orderID;
    private List<PurchaseOrdersProductDTO> productsOrdered;
    private Double TotalAmount;
    private String status;
}
