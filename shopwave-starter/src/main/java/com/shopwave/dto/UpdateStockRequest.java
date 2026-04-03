// Student Number: [ATE/8291/14]
// Student Name : YESEHAK ABRAHAM
package com.shopwave.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateStockRequest {
    @NotNull(message = "Delta is required")
    private Integer delta;
}
