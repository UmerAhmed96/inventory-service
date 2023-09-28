package com.example.inventorytask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
@Builder
@AllArgsConstructor
public class ItemDTO {
    private int itemId;

    @NotBlank(message = "Item name cannot be blank")
    @Size(min = 3, max = 50, message = "Item name must be between 3 and 50 characters")
    private String itemName;

    @DecimalMin(value = "0.0", message = "Buying price must be greater than or equal to 0")
    private double itemBuyingPrice;

    @DecimalMin(value = "0.0", message = "Selling price must be greater than or equal to 0")
    private double itemSellingPrice;

    @Pattern(regexp = "^(AVAILABLE)$", message = "Invalid item status")
    private String itemStatus;

    private String itemLastModifiedDate;
    private String itemLastModifiedByUser;
    private String itemEnteredDate;

    public ItemDTO() {
    }

    private String itemEnteredByUser;


}
