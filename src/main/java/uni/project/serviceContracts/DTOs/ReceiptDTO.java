package uni.project.serviceContracts.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record ReceiptDTO(String serialNumber,
                         String cashierName,
                         LocalDate issuedOn,
                         List<String> productNames,
                         BigDecimal totalSum) {

    public ReceiptDTO{
        if (serialNumber == null) {
            throw new IllegalArgumentException("serialNumber cannot be null");
        }

        if (cashierName == null) {
            throw new IllegalArgumentException("cashierName cannot be null");
        }

        if (issuedOn == null) {
            throw new IllegalArgumentException("issuedOn cannot be null");
        }

        if (totalSum == null) {
            throw new IllegalArgumentException("totalSum cannot be null");
        }

        if (productNames == null) {
            throw new IllegalArgumentException("products cannot be null");
        }
        productNames = List.copyOf(productNames);
    }
}