package uni.project.entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public record Receipt(String serialNumber,
                      Cashier cashier,
                      LocalDate issuedOn,
                      List<SaleItem> products,
                      BigDecimal totalSum) implements Serializable {
    private static final long serialVersionUID = 1L;

    public Receipt{
        if (serialNumber == null || serialNumber.isBlank()) {
            throw new IllegalArgumentException("Serial number cannot be null or blank");
        }
        if (cashier == null) {
            throw new IllegalArgumentException("Cashier cannot be null");
        }
        if (products == null || products.isEmpty()) {
            throw new IllegalArgumentException("Products cannot be null or empty");
        }
        if (totalSum == null || totalSum.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Total sum cannot be null or negative");
        }
        if (issuedOn == null) {
            throw new IllegalArgumentException("Issued date cannot be null");
        }
        products = List.copyOf(products);
    }

    @Override
    public String toString(){
        return "Receipt: " + serialNumber + "\n" +
                "issued on: " + issuedOn + "\n" +
                "bought products: " + "\n" +
                products.stream()
                        .map(s -> s.stock().toString())
                        .collect(Collectors.joining("\n")) +
                "total: " + totalSum
                ;
    }

}
